package cc.mrbird.febs.server.system.service.impl;

import cc.mrbird.febs.common.core.entity.MenuTree;
import cc.mrbird.febs.common.core.entity.Tree;
import cc.mrbird.febs.common.core.entity.constant.FebsConstant;
import cc.mrbird.febs.common.core.entity.constant.PageConstant;
import cc.mrbird.febs.common.core.entity.constant.StringConstant;
import cc.mrbird.febs.common.core.entity.router.RouterMeta;
import cc.mrbird.febs.common.core.entity.router.VueRouter;
import cc.mrbird.febs.common.core.entity.system.*;
import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.common.core.utils.TreeUtil;
import cc.mrbird.febs.common.redis.service.RedisService;
import cc.mrbird.febs.server.system.mapper.MenuMapper;
import cc.mrbird.febs.server.system.mapper.OrgStructureMapper;
import cc.mrbird.febs.server.system.mapper.UserRoleMapper;
import cc.mrbird.febs.server.system.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.list.TreeList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author MrBird
 */
@Slf4j
@Service("menuService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private TButtonService tButtonService;

    @Autowired
    private TRoleButtonService tRoleButtonService;

    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private OrgStructureMapper orgStructureMapper;
    @Autowired
    private ThreadPoolTaskExecutor febsAsyncThreadPool;
    @Autowired
    private RedisService redisService;
    @Resource
    private IUserAuthService userAuthService;

    @Override
    public String findUserPermissions(String username) {
        List<Menu> userPermissions = this.baseMapper.findUserPermissions(username);
        return userPermissions.stream().map(Menu::getPerms).collect(Collectors.joining(StringConstant.COMMA));
    }

    @Override
    public List<Menu> findUserMenus(String username) {
        return this.baseMapper.findUserMenus(username);
    }

    @Override
    public Map<String, Object> getUserOldRouters(String username) {
        Long userId = FebsUtil.getCurrentUserId();
        Map<String, Object> result = new HashMap<>(2);
        List<VueRouter<Menu>> userRouters = this.getUserRouters(username);
        String userPermissions = this.findUserPermissions(username);
        AuthUserModel userModel = userAuthService.userFrontEndPermission(userId);
        try {
            result.put("routes", userRouters);
            result.put("buttonsPermission", userModel);
            // 清空数据
            if (userRouters.size() > 0) {
                userRouters.get(0).setAllButtonList(new ArrayList<>());
            }
            String[] permissionArray =
                    StringUtils.splitByWholeSeparatorPreserveAllTokens(userPermissions, StringConstant.COMMA);
            result.put("permissions", permissionArray == null ? new String[0] : permissionArray);
            return result;
        } catch (Exception e) {
            return Maps.newHashMap();
        }
    }

    @Override
    public Map<String, Object> findMenus(Menu menu) {
        Map<String, Object> result = new HashMap<>(2);
        try {
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.orderByAsc(Menu::getOrderNum);
            queryWrapper.ne(Menu::getType, 1).ne(Menu::getType, 2).ne(Menu::getType, 3);
            List<Menu> menus = baseMapper.selectList(queryWrapper);

            // 查询所有的按钮
            List<TButton> tButtonList =
                    tButtonService.list(new LambdaQueryWrapper<TButton>().eq(TButton::getButtonType, 1));

            for (Menu menu1 : menus) {
                menu1.setId("m_" + menu1.getMenuId());
                for (TButton tButton : tButtonList) {
                    tButton.setId("b_" + tButton.getButtonId());
                    if (menu1.getMenuId().equals(tButton.getButtonMenuid())) {
                        menu1.getTButtonList().add(tButton);
                    }
                }
            }

            List<MenuTree> trees = new ArrayList<>();
            buildTrees(trees, menus);

            List<? extends Tree<?>> menuTree = TreeUtil.build(trees);

            result.put(PageConstant.ROWS, menuTree);
            result.put("total", menus.size());
        } catch (NumberFormatException e) {
            log.error("查询菜单失败", e);
            result.put(PageConstant.ROWS, null);
            result.put(PageConstant.TOTAL, 0);
        }
        return result;
    }

    /**
     * 优化菜单
     *
     * @param menu    菜单实体
     * @param request 请求
     * @return {@link Map< String, Object>}
     */
    @Override
    public Map<String, Object> findNewMenus(Menu menu, HttpServletRequest request) throws FebsException {
        // [{type:1,menus:[{}]},{type:2,menus:[{}]}]
        Map<String, Object> maps = new HashMap<>(2);
        List<Menu> menus = baseMapper.getNewMenus();
        Map<Integer, List<Menu>> collect =
                menus.stream().collect(Collectors.groupingBy(Menu::getClassType, Collectors.toList()));
        List<Map<String, Object>> menuLists = new ArrayList<>(2);
        Set<Map.Entry<Integer, List<Menu>>> entries = collect.entrySet();
        entries.parallelStream().forEach(menu1 -> {
            Map<String, Object> map = new HashMap<>(2);
            map.put("type", menu1.getKey());
            List<MenuTree> trees = new ArrayList<>();
            buildTrees(trees, menu1.getValue());
            List<? extends Tree<?>> menuTree = TreeUtil.build(trees);
            map.put("menus", menuTree);
            menuLists.add(map);
        });
        List<Map<String, Object>> projects = baseMapper.selectProjectMaps();
        try {
            maps.put("menus", menuLists);
            maps.put("projects", projects);
            // 层数 0
            maps.put("layers", 0);
            // 层数 1
            // maps.put("layers", 1);
        } catch (Exception e) {
            throw new FebsException("错误");
        }

        return maps;
    }

    @Override
    public Map<String, Object> findMenus(Menu menu, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>(2);
        try {
            // String roleIds = userRoleMapper.getUserRoleByUserId(FebsUtil.getCurrentUserId());
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.orderByAsc(Menu::getOrderNum);
            queryWrapper.eq(Menu::getType, 1);
            // if (StringUtils.isBlank(roleIds)) {
            // return result;
            // }
            // String[] split = roleIds.split(",");
            // Set<String> collect = Arrays.stream(split).collect(Collectors.toSet());
            // //排除菜单
            // HashSet<Integer> excludeMenuId = new HashSet<>();
            // if (collect.contains(FebsConstant.ADMIN_USER)) {
            // //超级管理
            // } else if (collect.contains(FebsConstant.TEMP_USER)) {
            // return result;
            // //临时
            // } else if (collect.contains(FebsConstant.PROJECT_USER)) {
            // //项目负责人
            // } else {
            // //员工
            // }
            // if (excludeMenuId.size() > 0) {
            // queryWrapper.notIn(Menu::getMenuId, excludeMenuId);
            // }
            List<Menu> menus = baseMapper.selectList(queryWrapper);
            // 查询所有的按钮
            List<TButton> tButtonList =
                    tButtonService.list(new LambdaQueryWrapper<TButton>().eq(TButton::getButtonType, 1));

            for (Menu menu1 : menus) {
                menu1.setId("m_" + menu1.getMenuId());
                for (TButton tButton : tButtonList) {
                    tButton.setId("b_" + tButton.getButtonId());
                    if (menu1.getMenuId().equals(tButton.getButtonMenuid())) {
                        menu1.getTButtonList().add(tButton);
                    }
                }
            }

            List<MenuTree> trees = new ArrayList<>();
            buildTrees(trees, menus);
            if (menu.getType() != null) {
                if (StringUtils.equals(menu.getType().toString(), Menu.TYPE_BUTTON)) {
                    result.put(PageConstant.ROWS, trees);
                } else {
                    List<? extends Tree<?>> menuTree = TreeUtil.build(trees);
                    result.put(PageConstant.ROWS, menuTree);
                }
            } else {
                List<? extends Tree<?>> menuTree = TreeUtil.build(trees);
                result.put(PageConstant.ROWS, menuTree);
            }
            result.put("total", menus.size());
        } catch (NumberFormatException e) {
            log.error("查询菜单失败", e);
            result.put(PageConstant.ROWS, null);
            result.put(PageConstant.TOTAL, 0);
        }
        return result;
    }

    @Override
    public Map<String, Object> getUserRouters() throws FebsException {
        try {
            Map<String, Object> result = new HashMap<>(2);
            Long userId = FebsUtil.getCurrentUserId();
            String userPermissions = baseMapper.findUserPermissionNews(userId);
            List<VueRouter<Menu>> userRouters = getUserRouterNews(userId);
            String[] permissionArray = new String[0];
            if (StringUtils.isNoneBlank(userPermissions)) {
                permissionArray =
                        StringUtils.splitByWholeSeparatorPreserveAllTokens(userPermissions, StringConstant.COMMA);
            }
            result.put("routes", userRouters);
            result.put("buttonsPermission",
                    userRouters.size() > 0 ? userRouters.get(0).getAllButtonNewList() : new ArrayList<TButton>());
            // 清空数据
            if (userRouters.size() > 0) {
                userRouters.get(0).setAllButtonNewList(null);
            }
            result.put("permissions", permissionArray);
            return result;
        } catch (Exception e) {
            throw new FebsException("出错了");
        }
    }

    /**
     * 获取用户菜单 按钮
     *
     * @param userId
     * @return {@link List< VueRouter< Menu>>}
     */
    private List<VueRouter<Menu>> getUserRouterNews(Long userId) throws FebsException {
        // 拿到用户拥有的菜单
        List<Menu> menus = this.baseMapper.findNewUserMenus(userId);
        List<VueRouter<Menu>> routes = new ArrayList<>();
        menus.forEach(menu -> {
            VueRouter<Menu> route = new VueRouter<>();
            route.setId("m_" + menu.getMenuId().toString());
            route.setParentId(menu.getParentId().toString());
            route.setPath(menu.getPath());
            route.setMobilePath(menu.getMobilePath());
            route.setComponent(menu.getComponent());
            route.setName(menu.getRouteName());
            route.setMeta(new RouterMeta(menu.getMenuName(), menu.getIcon(), true, menu.getBelongMenuRouteName()));
            route.setButtonList(menu.getTButtonList());
            routes.add(route);
        });
        try {
            List<Map<String, Object>> maps = getButtonMaps(userId);
            // 暂时存放全部按钮controller层获取后清空
            List<VueRouter<Menu>> vueRouters = TreeUtil.buildVueRouter(routes);
            if (!vueRouters.isEmpty()) {
                vueRouters.get(0).setAllButtonNewList(maps);
            }
            return vueRouters;
        } catch (Exception e) {
            throw new FebsException("查询失败");
        }

    }

    private List<Map<String, Object>> getButtonMaps(Long userId) {
        // 拿到用户拥有的按钮
        List<TButton> buttonList = tButtonService.getButtonByUserId(userId);
        List<Map<String, Object>> map = new ArrayList<>(2);
        // 拿到这个角色对应的按钮之后 放入对应的菜单中
        if (buttonList != null && !buttonList.isEmpty()) {
            Map<Integer, Map<Integer, List<TButton>>> collect =
                    // type分组
                    buttonList.stream().collect(Collectors.groupingBy(TButton::getType,
                            // projectId 分组
                            Collectors.groupingBy(TButton::getProjectId)));
            Set<Map.Entry<Integer, Map<Integer, List<TButton>>>> entries = collect.entrySet();
            for (Map.Entry<Integer, Map<Integer, List<TButton>>> entrie : entries) {
                Map<Integer, List<TButton>> value = entrie.getValue();
                Set<Map.Entry<Integer, List<TButton>>> entries1 = value.entrySet();
                if (entrie.getKey() == 1) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("proejctId", -1);
                    List<TButton> buttons = new ArrayList<>(8);
                    entries1.forEach(entrie1 -> buttons.addAll(entrie1.getValue()));
                    hashMap.put("buttons", buttons);
                    map.add(hashMap);
                } else {
                    for (Map.Entry<Integer, List<TButton>> entrie1 : entries1) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("proejctId", entrie1.getKey());
                        hashMap.put("buttons", entrie1.getValue());
                        map.add(hashMap);
                    }
                }
            }
        }
        return map;
    }

    @Override
    public List<VueRouter<Menu>> getUserRouters(String username) {
        List<VueRouter<Menu>> routes = new ArrayList<>();
        // 不需要权限控制的菜单
        List<Menu> list1 = this.list(new LambdaQueryWrapper<Menu>().eq(Menu::getType, 1).ne(Menu::getBelongMenuRouteName, ""));
        // 拿到用户拥有的菜单
        List<Menu> menus = this.findUserMenus(username);
        try {
            HashSet<Long> menuIds = new HashSet<>();
            list1.forEach(list -> menuIds.add(list.getMenuId()));
            menus.forEach(menu -> {
                if (menuIds.contains(menu.getMenuId())) {
                    list1.removeIf(list -> list.getMenuId().equals(menu.getMenuId()));
                }
            });
            menus.addAll(list1);
            menus = menus.stream().sorted(Comparator.comparing(Menu::getOrderNum)).collect(Collectors.toList());
            menus.forEach(menu -> {
                VueRouter<Menu> route = new VueRouter<>();
                route.setId(menu.getMenuId().toString());
                route.setParentId(menu.getParentId().toString());
                route.setPath(menu.getPath());
                route.setMobilePath(menu.getMobilePath());
                route.setComponent(menu.getComponent());
                route.setName(menu.getRouteName());
                route.setMeta(new RouterMeta(menu.getMenuName(), menu.getIcon(), true, menu.getBelongMenuRouteName()));
                route.setButtonList(menu.getTButtonList());
                routes.add(route);
            });
            // 暂时存放全部按钮controller层获取后清空
            return TreeUtil.buildVueRouter(routes);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Menu> findMenuList(Menu menu) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(menu.getMenuName())) {
            queryWrapper.like(Menu::getMenuName, menu.getMenuName());
        }
        queryWrapper.orderByAsc(Menu::getMenuId);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMenu(Menu menu) {
        menu.setCreateTime(new Date());
        setMenu(menu);
        this.save(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(Menu menu) {
        menu.setModifyTime(new Date());
        setMenu(menu);
        baseMapper.updateById(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMeuns(String[] menuIds) {
        this.delete(Arrays.asList(menuIds));
    }

    private void buildTrees(List<MenuTree> trees, List<Menu> menus) {
        menus.forEach(menu -> {
            // if (menu.getBelongMenuRouteName() == null) {
            MenuTree tree = new MenuTree();
            tree.setMobilePath(menu.getMobilePath());
            tree.setId("m_" + menu.getMenuId());
            tree.setMenuId(menu.getMenuId().toString());
            tree.setParentId(menu.getParentId().toString());
            tree.setLabel(menu.getMenuName());
            tree.setComponent(menu.getComponent());
            tree.setIcon(menu.getIcon());
            tree.setSelectIcon(menu.getSelectIcon());
            tree.setOrderNum(menu.getOrderNum());
            tree.setPath(menu.getPath());
            tree.setType(menu.getType() == null ? "0" : menu.getType().toString());
            tree.setPerms(menu.getPerms());
            tree.setButtonList(menu.getTButtonList());
            trees.add(tree);
            // }
        });
    }

    private void setMenu(Menu menu) {
        if (menu.getParentId() == null) {
            menu.setParentId(Menu.TOP_MENU_ID);
        }
        if (Menu.TYPE_BUTTON.equals(menu.getType())) {
            menu.setPath(null);
            menu.setIcon(null);
            menu.setComponent(null);
            menu.setOrderNum(null);
        }
    }

    private void delete(List<String> menuIds) {
        removeByIds(menuIds);

        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Menu::getParentId, menuIds);
        List<Menu> menus = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(menus)) {
            List<String> menuIdList = new ArrayList<>();
            menus.forEach(m -> menuIdList.add(String.valueOf(m.getMenuId())));
            this.delete(menuIdList);
        }
    }

}
