package cc.mrbird.febs.server.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import cc.mrbird.febs.common.core.entity.tjdkxm.Project;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.constant.FebsConstant;
import cc.mrbird.febs.common.core.entity.system.*;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.DateUtil;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.common.core.utils.SortUtil;
import cc.mrbird.febs.server.system.mapper.RoleMapper;
import cc.mrbird.febs.server.system.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author MrBird
 */
@Slf4j
@Service("roleService")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    private final IRoleMenuService roleMenuService;

    private final TRoleButtonService roleButtonService;
    private final IUserRoleService userRoleService;
    private final CacheEvictService cacheEvictService;
    private final IMenuService menuService;

    @Override
    public Role findNewRoles3(Long roleId) {
        //查询所有角色
        Role role = this.getById(roleId);
        //查询关联的菜单
        LambdaQueryWrapper<RoleMenu> roleMenuQueryWrapper = Wrappers.lambdaQuery();
        roleMenuQueryWrapper.in(RoleMenu::getRoleId, roleId);
        List<RoleMenu> roleMenus = this.roleMenuService.listById(roleId);
        //查询关联的按钮
        LambdaQueryWrapper<TRoleButton> roleButtonQueryWrapper = Wrappers.lambdaQuery();
        roleButtonQueryWrapper.eq(TRoleButton::getRoleId, roleId);
        List<TRoleButton> roleButtons = this.roleButtonService.list(roleButtonQueryWrapper);

        //根据项目ID和角色Id查询关联表的菜单id
        List<RoleDtoInfo2> projects = new ArrayList<>();
        //-1 特殊 -2 通用
        for (RoleMenu roleMenu : roleMenus) {
            RoleDtoInfo2 project1 = new RoleDtoInfo2();
            project1.setRoleId(roleMenu.getRoleId());
            if (roleMenu.getProjectId() < 0) {
                if (roleMenu.getClassType() == 1) {
                    roleMenu.setProjectId(-1L);
                }
                if (roleMenu.getClassType() == 1) {
                    roleMenu.setProjectId(-2L);
                }
            } else {
                roleMenu.setProjectId(roleMenu.getProjectId());
            }
            roleMenu.setProjectName(roleMenu.getProjectName());
        }
        //如果pid-1 公用，pid-2 项目


        List<RoleDtoInfo> menus2 = new ArrayList<>();
        RoleDtoInfo roleDtoInfo1 = new RoleDtoInfo();
        roleDtoInfo1.setType(1);
        RoleDtoInfo roleDtoInfo2 = new RoleDtoInfo();
        roleDtoInfo2.setType(2);
        menus2.add(roleDtoInfo1);
        menus2.add(roleDtoInfo2);
        role.setMenus(menus2);
        List<RoleDtoInfo> menus1 = role.getMenus();


        List<RoleDtoInfo2> roleDate1projects1 = new ArrayList<>();
        List<RoleDtoInfo2> roleDate1projects2 = new ArrayList<>();
        for (RoleDtoInfo2 project1 : projects) {
            if (project1.getRoleId().equals(role.getRoleId())) {
                if ((project1.getProjectId() == -1 || project1.getProjectId() == -2)) {
                    if (roleDate1projects1.size() == 0) {
                        roleDate1projects1.add(project1);
                    }
                } else {
                    roleDate1projects2.add(project1);
                }
            }
        }
        role.getMenus().get(0).setProjects(roleDate1projects1);
        role.getMenus().get(1).setProjects(roleDate1projects2);
        return role;
    }


    @Override
    public IPage<RoleDto> findNewRoles(Role role, QueryRequest request) {
        Integer total = this.baseMapper.findRoleNewPageTotal(role);
        IPage<RoleDto> roleNewPage = new Page<>();
        if (total != null && total > 0) {
            Integer pageNum = (request.getPageNum() - 1) * 10;
            List<RoleDto> roleDtos = this.baseMapper.findRoleNewPage(request.getPageSize(), pageNum, role);
            roleNewPage.setTotal(total);
            for (RoleDto roleDto : roleDtos) {
                for (RoleDtoInfo menu : roleDto.getMenus()) {
                    List<RoleDtoInfo2> projects = menu.getProjects();
                    projects.forEach(role1 -> {
                        StringBuilder ids = new StringBuilder();
                        if (Strings.isNotEmpty(role1.getMenuIds())) {
                            for (String ms : role1.getMenuIds().split(",")) {
                                ids.append("m_").append(ms).append(",");
                            }
                        }
                        if (Strings.isNotEmpty(role1.getButtonIds())) {
                            for (String bs : role1.getButtonIds().split(",")) {
                                ids.append("b_").append(bs).append(",");
                            }
                        }
                        if (Strings.isNotEmpty(ids)) {
                            role1.setIds(ids.substring(0, ids.length() - 1));
                        }
                        role1.setButtonIds(null);
                        role1.setMenuIds(null);
                    });
                }
            }
            roleNewPage.setRecords(roleDtos);
            return roleNewPage;
        }
        return roleNewPage;
    }

    @Override
    public IPage<Role> findRoles(Role role, QueryRequest request) {
        Page<Role> page = new Page<>(request.getPageNum(), request.getPageSize(), false);
        SortUtil.handlePageSort(request, page, "r.ROLE_ID", FebsConstant.ORDER_ASC, false);
        Integer rolePageCount = this.baseMapper.findRolePageCount(role);
        if (rolePageCount == null || rolePageCount == 0) {
            return new Page<>();
        }
        IPage<Role> rolePage = this.baseMapper.findRolePage(page, role);
        rolePage.setTotal(rolePageCount);
        rolePage.getRecords().forEach(role1 -> {
            StringBuilder ids = new StringBuilder();
            if (Strings.isNotEmpty(role1.getMenuIds())) {
                for (String ms : role1.getMenuIds().split(",")) {
                    ids.append("m_").append(ms).append(",");
                }
            }
            if (Strings.isNotEmpty(role1.getButtonIds())) {
                for (String bs : role1.getButtonIds().split(",")) {
                    ids.append("b_").append(bs).append(",");
                }
            }
            if (Strings.isNotEmpty(ids)) {
                role1.setIds(ids.substring(0, ids.length() - 1));
            }
        });
        return rolePage;
    }

    @Override
    public List<Role> findUserRole(String userName) {
        return baseMapper.findUserRole(userName);
    }

    @Override
    public List<Role> findAllRoles(Role role) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Role::getRoleId, Role::getRoleName);
        queryWrapper.gt(Role::getRoleId, Role.ADMIN_ROLE);
        queryWrapper.eq(Role::getIsDelete, 0);
        queryWrapper.orderByAsc(Role::getRoleId);
        return this.baseMapper.selectList(queryWrapper);
    }

    /**
     * 过滤默认权限
     *
     * @return {@link List< Role>}
     */
    @Override
    public List<Role> findAllNewRoles(Role role) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Role::getRoleId, Role::getRoleName);
        queryWrapper.gt(Role::getRoleId, Role.TEMP_ROLE);
        queryWrapper.not(wapper -> wapper.eq(Role::getRoleId, Role.PARTY_MEMBER));
        queryWrapper.eq(Role::getIsDelete, 0);
        queryWrapper.orderByAsc(Role::getRoleId);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public Role findByName(String roleName) {
        LambdaQueryWrapper<Role> eq = new LambdaQueryWrapper<Role>().eq(Role::getRoleName, roleName);
        return baseMapper.selectOne(eq);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRole(Role role) throws FebsException {
        if (StringUtils.isBlank(role.getIds())) {
            throw new FebsException("请至少选择一项权限");
        }

        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("ROLE_ID").eq("ROLE_NAME", role.getRoleName()).eq("IS_DELETE", 0);
        int count = this.count(queryWrapper);

        if (count > 0) {
            throw new FebsException("角色名已存在");
        }

        role.setCreateTime(DateUtil.getNowdateTimeToString());
        this.save(role);

        if (StringUtils.isNotBlank(role.getIds())) {
            String[] menuIds = role.getIds().split(",");
            List<String> collect = Arrays.stream(menuIds).collect(Collectors.toList());

            List<Integer> menuList = new ArrayList<>();
            // 把菜单和按钮list区别出来
            List<Integer> buttonList = new ArrayList<>();
            collect.forEach(s -> {
                // 1.菜单
                if (s.contains("m_")) {
                    menuList.add(Integer.parseInt(s.split("_")[1]));
                }
                // 2.按钮
                if (s.contains("b_")) {
                    buttonList.add(Integer.parseInt(s.split("_")[1]));
                }
            });
            if (menuList.size() <= 0) {
                throw new FebsException("请至少勾选一个菜单");
            }
            setRoleButtonList(role, buttonList);
            setRoleMenuList(role, menuList);
        }
    }

    /**
     * 创建角色
     *
     * @param role role
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createNewRole(RoleDto.Params role) throws FebsException {
        List<RoleDtoInfo.Params> menus = role.getMenus();
        if (menus == null || menus.isEmpty()) {
            throw new FebsException("请至少选择一项权限");
        }
        QueryWrapper<Role> queryWrapper = Wrappers.query();
        queryWrapper.select("ROLE_ID").eq("ROLE_NAME", role.getRoleName()).eq("IS_DELETE", 0);
        int count = this.count(queryWrapper);
        if (count > 0) {
            throw new FebsException("角色名已存在");
        }
        Role role1 = new Role();
        BeanUtils.copyProperties(role, role1);
        role1.setCreateTime(DateUtil.getNowdateTimeToString());
        this.save(role1);
        roleFollowUp(menus, role1.getRoleId(), role1.getType(), false);
    }

    /**
     * role 后续操作
     *
     * @param menus  菜单信息
     * @param roleId 角色id
     * @param isUp   是否时更新
     */
    private void roleFollowUp(List<RoleDtoInfo.Params> menus, Long roleId, Integer roleType, boolean isUp)
            throws FebsException {
        List<RoleMenu> menuList = new ArrayList<>();
        // 把菜单和按钮list区别出来
        List<TRoleButton> buttonList = new ArrayList<>();
        boolean special = roleType != null && roleType == 2;
        int count = 0;
        for (RoleDtoInfo.Params menu : menus) {
            List<RoleDtoInfo2.Params> projects = menu.getProjects();
            Integer type = menu.getType();
            boolean typeBoolean = type == null || (type <= 0 || type > 2);
            if (typeBoolean || (projects == null || projects.isEmpty())) {
                count++;
            } else {
                projects.removeIf(project -> StringUtils.isBlank(project.getIds()));
                if (projects.isEmpty()) {
                    count++;
                } else {
                    setButtonAndMenuList(roleId, menuList, buttonList, projects, type == 1, special);
                }
            }
        }
        if (count == menus.size() || menuList.isEmpty()) {
            throw new FebsException("请至少勾选一个菜单");
        }
        if (isUp) {
            removeRoleRelated(roleId);
        }
        try {
            // 级联添加 RoleMenu
            if (!menuList.isEmpty()) {
                roleMenuService.saveBatch(menuList, menuList.size());
            }
            // 级联添加 RoleButton
            if (!buttonList.isEmpty()) {
                roleButtonService.saveBatch(buttonList, buttonList.size());
            }

        } catch (Exception e) {
            throw new FebsException("错误");
        }
    }

    /**
     * 对role 的菜单按钮操作
     *
     * @param roleId     角色id
     * @param menuList   菜单s
     * @param buttonList 按钮s
     * @param projects   具体的权限信息
     * @param type       是否公共权限 true公共 false项目
     * @param roleType   true 通用角色 false 特殊角色
     */
    private void setButtonAndMenuList(Long roleId, List<RoleMenu> menuList, List<TRoleButton> buttonList,
                                      List<RoleDtoInfo2.Params> projects, boolean type, boolean roleType) throws FebsException {
        boolean flag = false;
        System.out.println(Role.PROJECT_ADMIN_ROLE.equals(roleId) || Role.TEMP_ROLE.equals(roleId)
                || Role.PROJECT_DEFAULT_ROLE.equals(roleId) || Role.PARTY_MEMBER.equals(roleId));
        if (Role.PROJECT_ADMIN_ROLE.equals(roleId) || Role.TEMP_ROLE.equals(roleId)
                || Role.PROJECT_DEFAULT_ROLE.equals(roleId) || Role.PARTY_MEMBER.equals(roleId)) {
            flag = true;
        }
        for (RoleDtoInfo2.Params project : projects) {
            Long projectId = null;
            if (flag) {
                projectId = -2L;
            } else {

                if (roleType) {
                    if (type) {
                        projectId = -1L;
                    } else {
                        projectId = -2L;
                    }
                } else {
                    if (type) {
                        projectId = -1L;
                    } else {
                        projectId = project.getProjectId();
                    }
                }
            }
            String ids = project.getIds();
            if (StringUtils.isBlank(ids)) {
                throw new FebsException("数据格式错误");
            }
            List<String> collect = Arrays.stream(ids.split(",")).collect(Collectors.toList());
            for (String s : collect) {
                try {
                    // 1.菜单
                    String s1 = s.split("_")[1];
                    if (!StringUtils.isNumeric(s1)) {
                        throw new FebsException("");
                    }
                    if (s.contains("m_")) {
                        RoleMenu roleMenu = new RoleMenu();
                        roleMenu.setRoleId(roleId);
                        roleMenu.setMenuId(Long.parseLong(s1));
                        roleMenu.setProjectId(projectId);
                        menuList.add(roleMenu);
                    }
                    // 2.按钮
                    if (s.contains("b_")) {
                        TRoleButton roleButton = new TRoleButton();
                        roleButton.setRoleId(roleId);
                        roleButton.setButtonId(Long.parseLong(s1));
                        roleButton.setProjectId(projectId);
                        buttonList.add(roleButton);
                    }
                } catch (FebsException e) {
                    throw new FebsException("数据格式错误");
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoles(String[] roleIds) throws FebsException {
        List<String> list = Arrays.asList(roleIds);
        for (String id : list) {
            if ((Role.PROJECT_ADMIN_ROLE.toString()).equals(id) || (Role.TEMP_ROLE.toString()).equals(id)
                    || (Role.PROJECT_DEFAULT_ROLE.toString()).equals(id) || (Role.PARTY_MEMBER.toString()).equals(id)) {
                throw new FebsException("删除角色列表中包含不能删除的角色,请排除!");
            }
        }
        baseMapper.update(null, new LambdaUpdateWrapper<Role>().in(Role::getRoleId, list).set(Role::getIsDelete, 1));
        this.roleMenuService.deleteRoleMenusByRoleId(roleIds);
        this.userRoleService.deleteUserRolesByRoleId(roleIds);
        cacheEvictService.upAuthority();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(String roleId) throws FebsException {
        if (roleId.equals(Role.PROJECT_ADMIN_ROLE.toString()) || roleId.equals(Role.TEMP_ROLE.toString())
                || roleId.equals(Role.PROJECT_DEFAULT_ROLE.toString()) || roleId.equals(Role.PARTY_MEMBER.toString())) {
            throw new FebsException("该角色不能删除!");
        }
        // 如果当前角色还有用户在使用则不能删除
        int count = userRoleService.count(new QueryWrapper<UserRole>().select("id").eq("ROLE_ID", roleId));
        if (count > 0) {
            throw new FebsException("当前角色还有用户在使用，暂不能删除");
        }
        baseMapper.update(null, new LambdaUpdateWrapper<Role>().eq(Role::getRoleId, roleId).set(Role::getIsDelete, 1));
        String[] roleIds = new String[1];
        roleIds[0] = roleId;
        roleMenuService.deleteRoleMenusByRoleId(roleIds);
        userRoleService.deleteUserRolesByRoleId(roleIds);
        cacheEvictService.upAuthority();
    }

    /**
     * 更新角色
     *
     * @param role role
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateNewRole(RoleDto.Params role) throws FebsException {
        Long roleId = role.getRoleId();
        List<RoleDtoInfo.Params> menus = role.getMenus();
        checkRole(role, roleId, menus);
        Role role1 = new Role();
        BeanUtils.copyProperties(role, role1);
        role1.setModifyTime(DateUtil.getNowdateTimeToString());
        baseMapper.updateById(role1);
        roleFollowUp(menus, roleId, role1.getType(), true);
        cacheEvictService.upAuthority();
    }

    /**
     * 删除角色相关
     *
     * @param roleId 角色id
     */
    private void removeRoleRelated(Long roleId) throws FebsException {
        roleMenuService.remove(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, roleId));
        roleButtonService.remove(new LambdaQueryWrapper<TRoleButton>().eq(TRoleButton::getRoleId, roleId));
    }

    /**
     * 校验角色
     *
     * @param role   角色
     * @param roleId 角色id
     * @param menus
     */
    private void checkRole(RoleDto.Params role, Long roleId, List<RoleDtoInfo.Params> menus) throws FebsException {
        Long userId = FebsUtil.getCurrentUserId();
        if (roleId == null || roleId.equals(Role.ADMIN_ROLE)) {
            throw new FebsException("修改失败！");
        }
        if (menus == null || menus.isEmpty()) {
            throw new FebsException("请至少选择一项权限");
        }
        // 角色名重复
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("ROLE_ID").eq("ROLE_NAME", role.getRoleName()).eq("IS_DELETE", 0).ne("ROLE_ID",
                role.getRoleId());
        int count = this.count(queryWrapper);
        // 权限
        if (roleId.equals(Role.PROJECT_ADMIN_ROLE) || roleId.equals(Role.TEMP_ROLE)
                || roleId.equals(Role.PROJECT_DEFAULT_ROLE) || roleId.equals(Role.PARTY_MEMBER)) {
            UserRole one = userRoleService.getOne(
                    new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId).orderByAsc(UserRole::getId));
            if (one == null || one.getRoleId() == null || !one.getRoleId().equals(Role.ADMIN_ROLE)) {
                throw new FebsException("权限不足！");
            }

        }
        try {
            if (count > 0) {
                throw new FebsException("角色名重复");
            }
        } catch (FebsException e) {
            throw new FebsException(e.getMessage());
        } catch (Exception e) {
            throw new FebsException("错误");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(Role role) throws FebsException {
        Long roleId = role.getRoleId();
        if (roleId == null || roleId.equals(Role.ADMIN_ROLE)) {
            throw new FebsException("修改失败！");
        }
        if (roleId.equals(Role.PROJECT_ADMIN_ROLE) || roleId.equals(Role.TEMP_ROLE)
                || roleId.equals(Role.PROJECT_DEFAULT_ROLE) || roleId.equals(Role.PARTY_MEMBER)) {
            Long userId = FebsUtil.getCurrentUserId();
            UserRole one = userRoleService
                    .getOne(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId).orderByAsc(UserRole::getId));
            if (one == null || one.getRoleId() == null || !one.getRoleId().equals(Role.ADMIN_ROLE)) {
                throw new FebsException("权限不足！");
            }
        }
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("ROLE_ID").eq("ROLE_NAME", role.getRoleName()).eq("IS_DELETE", 0).ne("ROLE_ID",
                role.getRoleId());
        int count = this.count(queryWrapper);
        if (count > 0) {
            throw new FebsException("角色名重复");
        }
        role.setModifyTime(DateUtil.getNowdateTimeToString());
        baseMapper.updateById(role);
        roleMenuService.remove(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, role.getRoleId()));
        roleButtonService.remove(new LambdaQueryWrapper<TRoleButton>().eq(TRoleButton::getRoleId, role.getRoleId()));
        if (StringUtils.isNotBlank(role.getIds())) {
            String[] menuIds = role.getIds().split(",");
            List<String> collect = Arrays.stream(menuIds).collect(Collectors.toList());
            List<Integer> menuList = new ArrayList<>();
            // 把菜单和按钮list区别出来
            List<Integer> buttonList = new ArrayList<>();
            collect.forEach(s -> {
                // 1.菜单
                if (s.contains("m_")) {
                    menuList.add(Integer.parseInt(s.split("_")[1]));
                }
                // 2.按钮
                if (s.contains("b_")) {
                    buttonList.add(Integer.parseInt(s.split("_")[1]));
                }
            });
            if (menuList.size() <= 0) {
                throw new FebsException("请至少勾选一个菜单");
            }
            setRoleButtonList(role, buttonList);
            setRoleMenuList(role, menuList);
            cacheEvictService.upAuthority();
        }
    }

    private void setRoleMenuList(Role role, List<Integer> menuIds) {
        List<RoleMenu> roleMenus = new ArrayList<>();
        if (null != menuIds && menuIds.size() > 0) {
            for (Integer id : menuIds) {
                RoleMenu roleMenu = new RoleMenu();
                if (id != null) {
                    roleMenu.setMenuId(Long.valueOf(id));
                    roleMenu.setRoleId(role.getRoleId());
                    roleMenu.setProjectId(-2L);
                    roleMenus.add(roleMenu);
                }
            }
            this.roleMenuService.saveBatch(roleMenus, roleMenus.size());
        }
    }

    private void setRoleButtonList(Role role, List<Integer> buttonIds) {
        List<TRoleButton> roleButtons = new ArrayList<>();
        if (null != buttonIds && buttonIds.size() > 0) {
            for (Integer id : buttonIds) {
                TRoleButton roleButton = new TRoleButton();
                if (id != null) {
                    roleButton.setButtonId(Long.valueOf(id));
                    roleButton.setRoleId(role.getRoleId());
                    roleButton.setProjectId(-2L);
                    roleButtons.add(roleButton);
                }
            }
            roleButtonService.saveBatch(roleButtons, roleButtons.size());
        }
    }

}
