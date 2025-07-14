package cc.mrbird.febs.server.system.service.impl;

import cc.mrbird.febs.common.core.entity.CurrentUser;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.constant.FebsConstant;
import cc.mrbird.febs.common.core.entity.constant.StringConstant;
import cc.mrbird.febs.common.core.entity.system.*;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.DateUtil;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.common.core.utils.OrderUtils;
import cc.mrbird.febs.common.core.utils.SortUtil;
import cc.mrbird.febs.server.system.configure.LogRecordContext;
import cc.mrbird.febs.server.system.mapper.DeptMapper;
import cc.mrbird.febs.server.system.mapper.RoleMapper;
import cc.mrbird.febs.server.system.mapper.UserMapper;
import cc.mrbird.febs.server.system.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author MrBird
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserServiceImpl extends ServiceImpl<UserMapper, SystemUser> implements IUserService {
    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private CacheEvictService cacheEvictService;
    @Autowired
    private LogRecordContext logRecordContext;
    @Autowired
    private ThreadPoolTaskExecutor febsAsyncThreadPool;
    @Autowired
    private TRoleButtonService roleButtonService;

    /**
     * 项目临时用户 修改为正式用户
     *
     * @param user
     */
    @Override
    public void updateTempUserToUser(boolean isTemp, SystemUser user) throws FebsException {
        if (isTemp) {
            user.setLevel(3);
            updateUserToTemp(user);
        } else {
            user.setLevel(2);
            updateTempToUser(user);
        }

    }

    private void updateTempToUser(SystemUser user) throws FebsException {
        // 更新用户
        user.setPassword(null);
        user.setUsername(null);
        user.setCreateTime(null);
        user.setModifyTime(DateUtil.getNowdateTimeToString());
        Long userId = user.getUserId();
        if (userId == null) {
            throw new FebsException("请选择用户");
        }
        SystemUser systemUser = userMapper.selectById(userId);
        if (systemUser == null) {
            throw new FebsException("修改失败");
        }
        updateById(user);
        List<UserRole> userRoles = new ArrayList<>(2);
        Set<Long> collect = null;
        try {
            collect =
                Arrays.stream(StringUtils.split(user.getRoleId(), ",")).map(Long::valueOf).collect(Collectors.toSet());
        } catch (Exception e) {
            throw new FebsException("数据错误");
        }
        Set<Long> finalCollect = collect;
        if (StringUtils.isNotBlank(user.getRoleId())) {
            try {
                // // 通用权限
                // List<Long> specialPermissions = roleMapper.getSpecialPermissions(finalCollect);
                // if (!specialPermissions.isEmpty()) {
                // List<Long> projectIds = roleMapper.getProjectIds();
                // specialPermissions.parallelStream().forEach(specialPermission -> {
                // finalCollect.removeIf(s -> s.equals(specialPermission));
                // projectIds.parallelStream().forEach(projectId -> {
                // UserRole userRole = new UserRole();
                // userRole.setUserId(user.getUserId());
                // userRole.setRoleId(specialPermission);
                // userRole.setProjectId(projectId);
                // userRoles.add(userRole);
                // });
                // });
                // }
                // 特殊权限
                finalCollect.parallelStream().forEach(s -> {
                    if (s.equals(Role.PROJECT_ADMIN_ROLE) || s.equals(Role.TEMP_ROLE)
                        || s.equals(Role.PROJECT_DEFAULT_ROLE) || s.equals(Role.PARTY_MEMBER)) {
                    } else {
                        UserRole userRole = new UserRole();
                        userRole.setUserId(user.getUserId());
                        userRole.setRoleId(s);
                        userRoles.add(userRole);
                    }
                });
            } catch (Exception e) {
                throw new IllegalArgumentException("修改失败");
            }
        }
        partyMember(user, userRoles);
        userRoleService.remove(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, user.getUserId())
            .gt(UserRole::getRoleId, Role.PROJECT_DEFAULT_ROLE));
        try {
            if (!userRoles.isEmpty()) {
                userRoleService.saveBatch(userRoles, userRoles.size());
            }
            cacheEvictService.upUser(user.getUserId());
            cacheEvictService.upAuthority(user.getUserId());
        } catch (Exception e) {
            throw new FebsException("数据错误");
        }
    }

    /**
     * 正式用户改成临聘用户
     *
     */
    private void updateUserToTemp(SystemUser user) throws FebsException {
        // 更新用户
        user.setPassword(null);
        user.setUsername(null);
        user.setCreateTime(null);
        user.setModifyTime(DateUtil.getNowdateTimeToString());
        Long userId = user.getUserId();
        if (userId == null) {
            throw new FebsException("请选择用户");
        }
        SystemUser systemUser = userMapper.selectById(userId);
        if (systemUser == null) {
            throw new FebsException("修改失败");
        }
        updateById(user);
        List<UserRole> userRoles = new ArrayList<>(2);
        Set<Long> collect = null;
        try {
            collect =
                Arrays.stream(StringUtils.split(user.getRoleId(), ",")).map(Long::valueOf).collect(Collectors.toSet());
        } catch (Exception e) {
            throw new FebsException("数据错误");
        }
        Set<Long> finalCollect = collect;
        if (StringUtils.isNotBlank(user.getRoleId())) {
            try {
                // // 通用权限
                // List<Long> specialPermissions = roleMapper.getSpecialPermissions(finalCollect);
                // if (!specialPermissions.isEmpty()) {
                // List<Long> projectIds = roleMapper.getProjectIds();
                // specialPermissions.parallelStream().forEach(specialPermission -> {
                // finalCollect.removeIf(s -> s.equals(specialPermission));
                // projectIds.parallelStream().forEach(projectId -> {
                // UserRole userRole = new UserRole();
                // userRole.setUserId(user.getUserId());
                // userRole.setRoleId(specialPermission);
                // userRole.setProjectId(projectId);
                // userRoles.add(userRole);
                // });
                // });
                // }
                // 特殊权限
                finalCollect.parallelStream().forEach(s -> {
                    if (s.equals(Role.PROJECT_ADMIN_ROLE) || s.equals(Role.TEMP_ROLE)
                        || s.equals(Role.PROJECT_DEFAULT_ROLE) || s.equals(Role.PARTY_MEMBER)) {
                    } else {
                        UserRole userRole = new UserRole();
                        userRole.setUserId(user.getUserId());
                        userRole.setRoleId(s);
                        userRoles.add(userRole);
                    }
                });
            } catch (Exception e) {
                throw new IllegalArgumentException("修改失败");
            }
        }
        partyMember(user, userRoles);
        // 添加临聘用户的信息
        userRoles.add(new UserRole(user.getUserId(), Role.TEMP_ROLE));
        userRoleService.remove(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, user.getUserId())
            .gt(UserRole::getRoleId, Role.PROJECT_DEFAULT_ROLE).gt(UserRole::getRoleId, Role.PROJECT_ADMIN_ROLE));
        try {
            if (!userRoles.isEmpty()) {
                userRoleService.saveBatch(userRoles, userRoles.size());
            }
            cacheEvictService.upUser(user.getUserId());
            cacheEvictService.upAuthority(user.getUserId());
        } catch (Exception e) {
            throw new FebsException("数据错误");
        }
    }

    @Override
    public List<SystemUser> projectUserInfoListSafePlanYsr(Long projectId, Long buttonId) {
        // 先根据项目id查询有这个权限的角色id
        LambdaQueryWrapper<TRoleButton> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TRoleButton::getButtonId, buttonId);
        queryWrapper.in(TRoleButton::getProjectId, -2, projectId);
        List<TRoleButton> roleButtons = roleButtonService.list(queryWrapper);
        List<Long> roleIds = roleButtons.stream().map(TRoleButton::getRoleId).collect(Collectors.toList());
        if (roleIds == null || roleIds.size() == 0) {
            return new ArrayList<>();
        }
        // 根据角色id查询用户id
        LambdaQueryWrapper<UserRole> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(UserRole::getRoleId, roleIds);
        queryWrapper1.groupBy(UserRole::getUserId);
        List<UserRole> userRoles = userRoleService.list(queryWrapper1);
        List<Long> userIds = userRoles.stream().map(UserRole::getUserId).collect(Collectors.toList());
        if (userIds == null || userIds.size() == 0) {
            return new ArrayList<>();
        }
        // 根据用户id查询用户信息
        LambdaQueryWrapper<SystemUser> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.in(SystemUser::getUserId, userIds);
        queryWrapper2.eq(SystemUser::getStatus, 1);
        queryWrapper2.ne(SystemUser::getUserId, 1);
        queryWrapper2.select(SystemUser::getUserId, SystemUser::getUsername, SystemUser::getRealname);
        List<SystemUser> systemUsers = this.list(queryWrapper2);
        return systemUsers;
    }

    @Override
    public SystemUser findByName(String username) {
        LambdaQueryWrapper<SystemUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemUser::getUsername, username);
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public IPage<SystemUser> findYgUserDetailList(SystemUser user, QueryRequest request) {
        QueryWrapper<SystemUser> queryUser = Wrappers.query();
        Page<SystemUser> page = getParams(user, request, queryUser);
        queryUser.eq("u.LEVEL", 2);
        return this.userMapper.getPageUser(page, queryUser);
    }

    private Page<SystemUser> getParams(SystemUser user, QueryRequest request, QueryWrapper<SystemUser> queryUser) {
        Page<SystemUser> page = new Page<>(request.getPageNum(), request.getPageSize());
        queryUser.groupBy("u.USER_ID");
        if (Strings.isNotEmpty(user.getUsername())) {
            queryUser.and(q -> q.like("u.USERNAME", user.getUsername()).or().like("u.REALNAME", user.getUsername()).or()
                .like("u.MOBILE", user.getUsername()));
        }
        if (user.getDeptId() != null) {
            queryUser.eq("u.DEPT_ID", user.getDeptId());
        }
        queryUser.ne("u.STATUS", '2');
        OrderUtils.setQuseryOrder(queryUser, request);
        return page;
    }

    /**
     * 项目临时用户 查询
     *
     * @param user 用户实体条件
     * @param request 分页参数
     * @return {@link null}
     */
    @Override
    public IPage<?> findTempUserList(SystemUser user, QueryRequest request) throws FebsException {
        QueryWrapper<SystemUser> queryUser = Wrappers.query();
        Page<SystemUser> page = getParams(user, request, queryUser);
        queryUser.eq("u.LEVEL", 3);
        // 添加部门筛选条件
        if (user.getDeptId() != null) {
            queryUser.eq("DEPT_ID", user.getDeptId());
        }
        // List<SystemUser> records = setSystemUsers(userPage, false);
        // userPage.setRecords(records);
        return userMapper.getPageUser(page, queryUser);
    }

    /**
     * 项目临时用户 新增
     *
     * @param user 用户实体
     * @return {@link null}
     */
    @Override
    public void addTempUser(SystemUser user) throws FebsException {
        user.setLevel(3);
        addNewUser(user, true);
    }

    /**
     * 项目临时用户 修改
     *
     * @param user 用户实体
     * @return {@link null}
     */
    @Override
    public void updateTempUser(SystemUser user) throws FebsException {
        updateYgUser(user);
    }

    /**
     * 获取所有用户
     *
     * @return {@link List< Map< String, Object>>}
     */
    @Override
    public List<Map<String, Object>> getAllUsers() {
        return userMapper.getAllUsers();
    }

    @Override
    public String getAvatar(Long userId) {
        return this.userMapper.selectAvatar(userId);
    }

    /**
     * 项目中的用户
     *
     * @param user
     * @param request
     * @return {@link IPage<?>}
     */
    @Override
    public IPage<?> userByProjectList(SystemUser user, Long projectId, QueryRequest request) throws FebsException {
        if (projectId == null) {
            throw new FebsException("查询错误，未知的项目");
        }
        LambdaQueryWrapper<SystemUser> queryUser = Wrappers.lambdaQuery();
        Page<SystemUser> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, null, FebsConstant.ORDER_DESC, true);
        if (Strings.isNotEmpty(user.getUsername())) {
            queryUser.and(q -> q.like(SystemUser::getUsername, user.getUsername()).or()
                .like(SystemUser::getRealname, user.getUsername()).or()
                .like(SystemUser::getMobile, user.getUsername()));
        }
        if (user.getDeptId() != null) {
            queryUser.eq(SystemUser::getDeptId, user.getDeptId());
        }
        IPage<SystemUser> userPage = this.userMapper.userByProjectList(page, projectId, queryUser);
        List<SystemUser> records = setSystemUsers(userPage, false);
        userPage.setRecords(records);
        return userPage;
    }

    /**
     * 未在项目中的用户
     *
     * @param user
     * @param request
     * @return {@link IPage<?>}
     */
    @Override
    public IPage<?> userNotInProject(SystemUser user, Long projectId, QueryRequest request) throws FebsException {
        if (projectId == null) {
            throw new FebsException("查询错误，未知的项目");
        }
        QueryWrapper<SystemUser> queryUser = Wrappers.query();
        Page<SystemUser> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, null, FebsConstant.ORDER_DESC, true);
        if (Strings.isNotEmpty(user.getUsername())) {
            queryUser.and(q -> q.like("t.USERNAME", user.getUsername()).or().like("t.REALNAME", user.getUsername()).or()
                .like("t.MOBILE", user.getUsername()));
        }
        if (user.getDeptId() != null) {
            queryUser.eq("t.DEPT_ID", user.getDeptId());
        }
        queryUser.groupBy("u.USER_ID");
        // List<SystemUser> records = setSystemUsers(userPage, true);
        // userPage.setRecords(records);
        return this.userMapper.userNotInProject(page, projectId, queryUser);
    }

    /**
     * 对结果封装
     *
     * @param userPage
     * @return {@link List< SystemUser>}
     */
    private List<SystemUser> setSystemUsers(IPage<SystemUser> userPage, boolean notIn) {
        Set<Long> deptIds = new HashSet<>();
        Set<Long> userIds = new HashSet<>();
        List<SystemUser> records = userPage.getRecords();
        records.forEach(n -> {
            deptIds.add(n.getDeptId());
            userIds.add(n.getUserId());
        });

        if (deptIds.size() > 0) {
            List<Map<String, Object>> deptIds1 = deptMapper.getDeptIds(deptIds);
            records.forEach(n -> {
                n.setPassword("");
                deptIds1.forEach(map -> {
                    if (map.get("DEPT_ID").equals(n.getDeptId())) {
                        n.setDeptName((String)map.get("DEPT_NAME"));
                    }
                });
            });
        }
        if (!notIn) {
            if (userIds.size() > 0) {
                List<Map<String, Object>> userIds1 = roleMapper.selectByUserIds(userIds);
                records.forEach(n -> {
                    n.setPassword("");
                    userIds1.forEach(map -> {
                        if (map.get("USER_ID").equals(n.getUserId())) {
                            Long roleId = (Long)map.get("ROLE_ID");
                            n.setRoleId(roleId.toString());
                            String roleName = (String)map.get("ROLE_NAME");
                            if (Strings.isEmpty(n.getRoleName())) {
                                n.setRoleId(roleId + "");
                                n.setRoleName(roleName);
                            } else {
                                n.setRoleId(n.getRoleId() + "," + roleId);
                                n.setRoleName(n.getRoleName() + "," + roleName);
                            }
                        }
                    });
                });
            }
        }
        return records;
    }

    @Override
    public IPage<SystemUser> findUserDetailList(SystemUser user, QueryRequest request) {
        LambdaQueryWrapper<SystemUser> queryUser = Wrappers.lambdaQuery();
        Page<SystemUser> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, null, FebsConstant.ORDER_DESC, true);
        if (Strings.isNotEmpty(user.getUsername())) {
            queryUser.and(q -> q.like(SystemUser::getUsername, user.getUsername()).or().like(SystemUser::getRealname,
                user.getUsername()));
        }
        if (user.getType() != null) {
            queryUser.eq(SystemUser::getType, user.getType());
        }
        if (user.getSupId() != null) {
            queryUser.eq(SystemUser::getSupId, user.getSupId());
        }
        queryUser.ne(SystemUser::getStatus, '2');
        queryUser.gt(SystemUser::getUserId, 1);
        Page<SystemUser> userPage = this.page(page, queryUser);

        // 本次查询所有的用户
        List<SystemUser> userList = userPage.getRecords();
        // 记录所有的用户id
        ArrayList<Object> userIds = new ArrayList<>();

        if (userList.size() > 0) {
            userList.forEach(item -> {
                userIds.add(item.getUserId());
            });
            // 获取所有的角色id
            QueryWrapper<UserRole> queryRole = Wrappers.query();
            queryRole.in("USER_ID", userIds);
            List<UserRole> roleList = userRoleService.list(queryRole);
            List<Integer> roleIds = new ArrayList<>();
            roleList.forEach(n -> roleIds.add(n.getRoleId().intValue()));

            QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
            roleQueryWrapper.in("ROLE_ID", roleIds);
            // 获取role menu

            if (roleIds.size() > 0) {
                List<Role> roleMenuList = roleService.list(roleQueryWrapper);
                roleList.forEach(role -> {
                    roleMenuList.forEach(roleMenu -> {
                        if (role.getRoleId().equals(roleMenu.getRoleId())) {
                            role.setRole(roleMenu);
                        }
                    });
                });
            }
            roleList.forEach(r -> {
                userList.forEach(u -> {
                    if (u.getUserId().equals(r.getUserId())) {
                        if (r.getRole() != null) {
                            if (Strings.isEmpty(u.getRoleName())) {
                                u.setRoleId(r.getRole().getRoleId() + "");
                                u.setRoleName(r.getRole().getRoleName());
                            } else {
                                u.setRoleId(u.getRoleId() + "," + r.getRole().getRoleId());
                                u.setRoleName(u.getRoleName() + "," + r.getRole().getRoleName());
                            }
                        }
                    }
                });
            });
        }
        userPage.setRecords(userList);
        return userPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLoginTime(String username) {
        SystemUser user = new SystemUser();
        user.setLastLoginTime(DateUtil.getNowdateTimeToString());

        this.baseMapper.update(user, new LambdaQueryWrapper<SystemUser>().eq(SystemUser::getUsername, username));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addYgUser(SystemUser user) throws FebsException {
        user.setLevel(2);
        // addUser(user, Role.PROJECT_DEFAULT_ROLE);
        addNewUser(user, false);
    }

    private void addNewUser(SystemUser user, boolean isTempUser) throws FebsException {
        // 创建用户
        LambdaQueryWrapper<SystemUser> addqueryUser = Wrappers.lambdaQuery();
        addqueryUser.eq(SystemUser::getUsername, user.getUsername());
        addqueryUser.select(SystemUser::getLevel);
        try {
            List<SystemUser> systemUsers = this.userMapper.selectList(addqueryUser);
            if (systemUsers != null && !systemUsers.isEmpty()) {
                Integer level = systemUsers.get(0).getLevel();
                if (level == 2 || level == 1) {
                    throw new FebsException("已存在相同用户名");
                }
                if (level == 3) {
                    throw new FebsException("已存在相同用户名的临聘人员");
                }
            }
        } catch (Exception e) {
            throw new FebsException("用户名重复");
        }
        user.setCreateTime(DateUtil.getNowdateTimeToString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        boolean save = this.save(user);
        if (save) {
            ArrayList<UserRole> userRoles = new ArrayList<>(2);
            Long userId1 = user.getUserId();
            if (!isTempUser) {
                String roleId1 = user.getRoleId();
                if (StringUtils.isNotBlank(roleId1)) {
                    try {
                        Set<Long> collect =
                            Arrays.stream(roleId1.split(",")).map(Long::valueOf).collect(Collectors.toSet());
                        // // 通用权限
                        // List<Long> specialPermissions = roleMapper.getSpecialPermissions(collect);
                        // if (!specialPermissions.isEmpty()) {
                        // List<Long> projectIds = roleMapper.getProjectIds();
                        // specialPermissions.parallelStream().forEach(specialPermission -> {
                        // collect.removeIf(s -> s.equals(specialPermission));
                        // projectIds.parallelStream().forEach(projectId -> {
                        // UserRole userRole = new UserRole();
                        // userRole.setUserId(user.getUserId());
                        // userRole.setRoleId(specialPermission);
                        // userRole.setProjectId(projectId);
                        // userRoles.add(userRole);
                        // });
                        // });
                        // }
                        // 特殊权限
                        collect.parallelStream().forEach(s -> {
                            if (s.equals(Role.ADMIN_ROLE) || s.equals(Role.PROJECT_ADMIN_ROLE)
                                || s.equals(Role.TEMP_ROLE) || s.equals(Role.PROJECT_DEFAULT_ROLE)
                                || s.equals(Role.PARTY_MEMBER)) {
                            } else {
                                UserRole userRole = new UserRole();
                                userRole.setUserId(user.getUserId());
                                userRole.setRoleId(s);
                                userRoles.add(userRole);
                            }
                        });
                    } catch (NumberFormatException e) {
                        log.error("转换失败");
                    }
                }
            } else {
                // 添加用户角色信息
                UserRole userRole = new UserRole();
                userRole.setUserId(userId1);
                // 2L 默认公司临时员工角色id:2L
                userRole.setRoleId(Role.TEMP_ROLE);
                userRoles.add(userRole);
            }
            partyMember(user, userRoles);
            if (!userRoles.isEmpty()) {
                userRoleService.saveBatch(userRoles, userRoles.size());
            }
            cacheEvictService.upUser(user.getUserId());
        }

    }

    private void partyMember(SystemUser user, List<UserRole> userRoles) {
        if (SystemUser.IS_PARTY_MEMBER.equals(user.getPartyMember())) {
            // 党员
            UserRole userRole = new UserRole();
            userRole.setRoleId(Role.PARTY_MEMBER);
            userRole.setUserId(user.getUserId());
            userRoles.add(userRole);
        }
    }

    private void addUser(SystemUser user, Long roleId) throws FebsException {
        // 创建用户
        LambdaQueryWrapper<SystemUser> addqueryUser = Wrappers.lambdaQuery();
        addqueryUser.eq(SystemUser::getUsername, user.getUsername());
        addqueryUser.select(SystemUser::getLevel);
        try {
            List<SystemUser> systemUsers = this.userMapper.selectList(addqueryUser);
            if (systemUsers.size() > 0) {
                Integer level = systemUsers.get(0).getLevel();
                if (level == 2 || level == 1) {
                    throw new FebsException("已存在相同用户名");
                }
                if (level == 3) {
                    throw new FebsException("已存在相同用户名的临聘人员");
                }
            }
        } catch (Exception e) {
            throw new FebsException("用户名重复");
        }
        user.setCreateTime(DateUtil.getNowdateTimeToString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        boolean save = this.save(user);
        if (save) {
            ArrayList<UserRole> userRoles = new ArrayList<>(2);
            if (!roleId.equals(Role.TEMP_ROLE)) {
                String roleId1 = user.getRoleId();
                if (StringUtils.isNotBlank(roleId1)) {
                    String[] split = roleId1.split(",");
                    for (String s : split) {
                        try {
                            UserRole userRole = new UserRole();
                            userRole.setUserId(user.getUserId());
                            userRole.setRoleId(Long.valueOf(s));
                            userRoles.add(userRole);
                        } catch (NumberFormatException e) {
                            log.error(s + "转换失败");
                        }
                    }
                } else {
                    // 添加用户角色信息
                    UserRole userRole = new UserRole();
                    userRole.setUserId(user.getUserId());
                    // 2L 默认公司员工角色id:3L
                    userRole.setRoleId(roleId);
                    userRoles.add(userRole);
                }
            } else {
                // 添加用户角色信息
                UserRole userRole = new UserRole();
                userRole.setUserId(user.getUserId());
                // 2L 默认公司员工角色id:2L
                userRole.setRoleId(roleId);
                userRoles.add(userRole);
            }
            if (SystemUser.IS_PARTY_MEMBER.equals(user.getPartyMember())) {
                // 党员
                UserRole userRole = new UserRole();
                userRole.setRoleId(Role.PARTY_MEMBER);
                userRole.setUserId(user.getUserId());
                userRoles.add(userRole);
            }
            if (!userRoles.isEmpty()) {
                userRoleService.saveBatch(userRoles, userRoles.size());
            }
            cacheEvictService.upUser(user.getUserId());
        }
        logRecordContext.putVariable("id", user.getUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUser(SystemUser user) throws FebsException {
        addYgUser(user);
    }

    private void updateNewYgUser(SystemUser user) throws FebsException {
        // 更新用户
        user.setPassword(null);
        user.setUsername(null);
        user.setCreateTime(null);
        user.setModifyTime(DateUtil.getNowdateTimeToString());
        Long userId = user.getUserId();
        if (userId == null) {
            throw new FebsException("请选择用户");
        }
        SystemUser systemUser = userMapper.selectById(userId);
        if (systemUser == null) {
            throw new FebsException("修改失败");
        }
        updateById(user);
        List<UserRole> userRoles = new ArrayList<>(2);
        Set<Long> collect = null;
        try {
            collect =
                Arrays.stream(StringUtils.split(user.getRoleId(), ",")).map(Long::valueOf).collect(Collectors.toSet());
        } catch (Exception e) {
            throw new FebsException("数据错误");
        }
        Set<Long> finalCollect = collect;
        if (StringUtils.isNotBlank(user.getRoleId())) {
            try {
                // 通用权限
                List<Long> specialPermissions = roleMapper.getSpecialPermissions(finalCollect);
                if (!specialPermissions.isEmpty()) {
                    List<Long> projectIds = roleMapper.getProjectIds();
                    specialPermissions.parallelStream().forEach(specialPermission -> {
                        finalCollect.removeIf(s -> s.equals(specialPermission));
                        projectIds.parallelStream().forEach(projectId -> {
                            UserRole userRole = new UserRole();
                            userRole.setUserId(user.getUserId());
                            userRole.setRoleId(specialPermission);
                            userRole.setProjectId(projectId);
                            userRoles.add(userRole);
                        });
                    });
                }
                // 特殊权限
                finalCollect.parallelStream().forEach(s -> {
                    if (s.equals(Role.PROJECT_ADMIN_ROLE) || s.equals(Role.TEMP_ROLE)
                        || s.equals(Role.PROJECT_DEFAULT_ROLE) || s.equals(Role.PARTY_MEMBER)) {
                    } else {
                        UserRole userRole = new UserRole();
                        userRole.setUserId(user.getUserId());
                        userRole.setRoleId(s);
                        userRoles.add(userRole);
                    }
                });
            } catch (Exception e) {
                throw new IllegalArgumentException("修改失败");
            }
        }
        partyMember(user, userRoles);
        userRoleService.remove(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, user.getUserId())
            .gt(UserRole::getRoleId, Role.TEMP_ROLE));
        try {
            if (!userRoles.isEmpty()) {
                userRoleService.saveBatch(userRoles, userRoles.size());
            }
            cacheEvictService.upUser(user.getUserId());
            cacheEvictService.upAuthority(user.getUserId());
        } catch (Exception e) {
            throw new FebsException("数据错误");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateYgUser(SystemUser user) throws FebsException {
        updateNewYgUser(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateYgUserToProject(SystemUser user) throws FebsException {
        // //先删除该用户除了公司角色的所有角色
        //// userRoleService.deleteUserRoleByUserIdOr0(user.getUserId());
        // String[] projectIds = user.getProjectIds().split(StringConstant.COMMA);
        // Set<Long> projectIdSet = new HashSet<>();
        // Arrays.stream(projectIds).forEach(e -> projectIdSet.add(Long.valueOf(e)));
        // if (projectIdSet.size() > 0) {
        // //查找项目角色
        // List<Map<String, Object>> maps = roleMapper.selectByIds(projectIdSet);
        // List<UserRole> userRoles = new ArrayList<>();
        // maps.forEach(map -> {
        // UserRole userRole = new UserRole();
        // userRole.setRoleId((Long) map.get("ROLE_ID"));
        // userRole.setUserId(user.getUserId());
        // userRoles.add(userRole);
        // });
        // userRoleService.saveBatch(userRoles);
        // }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(SystemUser user) throws FebsException {
        updateYgUser(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUsers(String[] userIds) throws FebsException {
        List<String> list = Arrays.asList(userIds);
        if (userIds == null || userIds.length == 0) {
            throw new FebsException("请选择用户删除");
        }
        for (String id : list) {
            if ("1".equals(id)) {
                throw new FebsException("删除失败");
            }
        }
        LambdaQueryWrapper<UserRole> userRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userRoleLambdaQueryWrapper.eq(UserRole::getRoleId, Role.PROJECT_ADMIN_ROLE).in(UserRole::getUserId, list);
        int size = userRoleService.count(userRoleLambdaQueryWrapper);
        if (size > 0) {
            throw new FebsException("删除用户中有项目的负责人，请先处理.. 后再删除");
        }
        UpdateWrapper<SystemUser> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.in("USER_ID", list).set("STATUS", "2").set("USERNAME", "remove" + UUID.randomUUID());
        this.update(userUpdateWrapper);
        // 删除用户角色
        this.userRoleService.deleteUserRolesByUserId(userIds);
        cacheEvictService.upUser();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteYgUser(String userId) throws FebsException {
        if (userId == null || "1".equals(userId)) {
            throw new FebsException("请选择用户删除");
        }
        HashSet<String> userIds = new HashSet<String>() {
            {
                add(userId);
            }
        };
        String userId1 = FebsUtil.getCurrentUserId().toString();
        if (userIds.contains(userId1)) {
            throw new FebsException("权限不足,无法删除本人");
        }
        LambdaQueryWrapper<UserRole> userRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 负责人权限
        userRoleLambdaQueryWrapper.eq(UserRole::getRoleId, Role.PROJECT_ADMIN_ROLE).eq(UserRole::getUserId, userId);
        int size = userRoleService.count(userRoleLambdaQueryWrapper);
        if (size > 0) {
            List<String> projectName = userMapper.selectProjectNameByUserId(userIds);
            if (projectName != null && projectName.size() > 0) {
                StringBuilder str = new StringBuilder("该用户是");
                projectName.forEach(name -> {
                    str.append(name);
                    str.append("、");
                });
                str.append("的负责人,请先在这些项目中解绑该用户");
                throw new FebsException(str.toString());
            }
        }
        UpdateWrapper<SystemUser> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("USER_ID", userId).set("STATUS", "2").set("USERNAME", "remove" + UUID.randomUUID());
        this.update(userUpdateWrapper);
        this.userRoleService.deleteUserRoleByUserId(userId);
        cacheEvictService.upUser();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(String userId) throws FebsException {
        deleteYgUser(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProfile(SystemUser user) throws FebsException {
        user.setPassword(null);
        user.setUsername(null);
        user.setStatus(null);
        if (isCurrentUser(user.getUserId())) {
            updateById(user);
            cacheEvictService.upUser(user.getUserId());
        } else {
            throw new FebsException("您无权修改别人的账号信息！");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAvatar(String avatar) {
        SystemUser user = new SystemUser();
        user.setAvatar(avatar);
        Long userId = FebsUtil.getCurrentUserId();
        this.baseMapper.update(user, new LambdaQueryWrapper<SystemUser>().eq(SystemUser::getUserId, userId));
        cacheEvictService.upUser(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(String password) throws FebsException {
        SystemUser user = new SystemUser();
        try {
            cacheEvictService.upPwd();
        } catch (Exception e) {

        }
        user.setPassword(passwordEncoder.encode(password));
        String currentUsername = FebsUtil.getCurrentUsername();
        int i = this.baseMapper.update(user,
            new LambdaQueryWrapper<SystemUser>().eq(SystemUser::getUsername, currentUsername));
        if (i < 0) {
            throw new FebsException("修改失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(String[] usernames) {
        SystemUser params = new SystemUser();
        params.setPassword(passwordEncoder.encode(SystemUser.DEFAULT_PASSWORD));
        try {
            cacheEvictService.upPwd();
        } catch (Exception e) {

        }
        List<String> list = Arrays.asList(usernames);
        this.baseMapper.update(params, new LambdaQueryWrapper<SystemUser>().in(SystemUser::getUsername, list));

    }

    private void setUserRoles(SystemUser user, String[] roles) {
        List<UserRole> userRoles = new ArrayList<>();
        Arrays.stream(roles).forEach(roleId -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getUserId());
            userRole.setRoleId(Long.valueOf(roleId));
            userRoles.add(userRole);
        });
        userRoleService.saveBatch(userRoles, userRoles.size());
    }

    private boolean isCurrentUser(Long id) {
        CurrentUser currentUser = FebsUtil.getCurrentUser();
        return currentUser != null && id.equals(currentUser.getUserId());
    }

    @Override
    public void resetPassword(SystemUser user) {
        LambdaQueryWrapper<Object> queryUser = Wrappers.lambdaQuery();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            // cacheEvictService.upPwd();
        } catch (Exception e) {

        }
        user.setUsername(null);
        this.updateById(user);
    }

    @Override
    public void restMyPassword(SystemUser user) throws FebsException {

        SystemUser byId = this.getById(user.getUserId());
        try {
            cacheEvictService.upPwd();
        } catch (Exception e) {

        }
        boolean matches = passwordEncoder.matches(user.getOldPassword(), byId.getPassword());

        if (!matches) {
            throw new FebsException("旧密码输入错误");
        }

        LambdaQueryWrapper<Object> queryUser = Wrappers.lambdaQuery();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.updateById(user);
    }

    @Override
    public String getCurrentUser() {
        SystemUser user = this.getById(FebsUtil.getCurrentUser().getUserId());
        return user.getUsername();
    }
}
