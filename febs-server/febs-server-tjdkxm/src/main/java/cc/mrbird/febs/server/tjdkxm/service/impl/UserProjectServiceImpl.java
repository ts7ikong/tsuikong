package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.constant.ButtonConstant;
import cc.mrbird.febs.common.core.entity.constant.FebsConstant;
import cc.mrbird.febs.common.core.entity.constant.MenuConstant;
import cc.mrbird.febs.common.core.entity.constant.ParamsConstant;
import cc.mrbird.febs.common.core.entity.system.*;
import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
import cc.mrbird.febs.common.core.entity.tjdkxm.Project;
import cc.mrbird.febs.common.core.entity.tjdkxm.UserProject;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.common.core.utils.Md5Util;
import cc.mrbird.febs.common.core.utils.OrderUtils;
import cc.mrbird.febs.common.redis.service.RedisService;
import cc.mrbird.febs.server.tjdkxm.mapper.ProjectMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.RoleMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.UserProjectMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.UserRoleMapper;
import cc.mrbird.febs.server.tjdkxm.service.CacheableService;
import cc.mrbird.febs.server.tjdkxm.service.IUserRoleService;
import cc.mrbird.febs.server.tjdkxm.service.UserProjectService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

/**
 * @author ZNKJ-R
 * @version V1.0
 * @date 2022/1/18 19:37
 */
@Service
// @RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserProjectServiceImpl extends ServiceImpl<UserProjectMapper, UserProject> implements UserProjectService {
    @Autowired
    private UserProjectMapper userProjectMapper;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMapper roleMapper;
    private final String tokenInfoUri;
    private final String captchaUrl;
    private final String removeTokenUrl;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CacheableService cacheableService;

    public UserProjectServiceImpl(ResourceServerProperties resourceProperties) {
        this.tokenInfoUri = resourceProperties.getUserInfoUri().replace("user", "oauth/token");
        this.captchaUrl = resourceProperties.getUserInfoUri().replace("user", "captcha?key=");
        this.removeTokenUrl = resourceProperties.getUserInfoUri().replace("user", "remove-token");
    }

    @Override
    public IPage<UserProject> findUserProject(QueryRequest request, UserProject userProject) {
        QueryWrapper<UserProject> queryWrapper = new QueryWrapper<>();
        OrderUtils.setQuseryOrder(queryWrapper, request);
        Page<UserProject> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.userProjectMapper.selectPage(page, queryWrapper);
    }

    @Override
    public IPage<Map<String, Object>> findUserByProjectId(QueryRequest request, UserProject.UserModel userProject)
        throws FebsException {
        QueryWrapper<UserProject.UserModel> queryWrapper = getParam(userProject);
        return this.userProjectMapper.selectPageInfo(new Page<>(request.getPageNum(), request.getPageSize()),
            userProject.getProjectId(), userProject.getProjectId(), queryWrapper);
    }

    private QueryWrapper<UserProject.UserModel> getParam(UserProject.UserModel userProject) throws FebsException {
        if (userProject.getProjectId() == null) {
            throw new FebsException("请先选择项目");
        }
        QueryWrapper<UserProject.UserModel> queryWrapper = new QueryWrapper<>();
        if (Strings.isNotEmpty(userProject.getUsername())) {
            queryWrapper.and(q -> q.like("t.USERNAME", userProject.getUsername()).or()
                .like("t.REALNAME", userProject.getUsername()).or().like("t.MOBILE", userProject.getUsername()));
        }
        if (userProject.getDeptId() != null) {
            queryWrapper.eq("t.DEPT_ID", userProject.getDeptId());
        }
        return queryWrapper;
    }

    @Override
    public IPage<Map<String, Object>> findNotUserByProjectId(QueryRequest request, UserProject.UserModel userProject)
        throws FebsException {
        QueryWrapper<UserProject.UserModel> queryWrapper = getParam(userProject);
        return this.userProjectMapper.selectPageInfo(new Page<>(request.getPageNum(), request.getPageSize()),
            userProject.getProjectId(), -1L, queryWrapper);
    }

    /**
     * 查询--质量检查计划、质量问题 检查人
     *
     * @return {@link List< Map< String, Object>>}
     */
    @Override
    public List<Map<String, Object>> projectUserInfoListQualityPlan() {
        return cacheableService.getUserByMenuIdAndBtnIdAndProjectIds(MenuConstant.INSPECTION_PLAN_ID,
            ButtonConstant.BUTTON_159_ID);
    }

    /**
     * 查询--质量问题整改人
     *
     * @return {@link Object}
     */
    @Override
    public List<Map<String, Object>> projectUserInfoListQualityRectify() {
        return cacheableService.getUserByMenuIdAndBtnIdAndProjectIds(MenuConstant.PROBLEM_LIST_ID,
            ButtonConstant.BUTTON_168_ID);
    }

    /**
     * 查询--质量问题验收人
     *
     * @return {@link Object}
     */
    @Override
    public List<Map<String, Object>> projectUserInfoListQualityAcceptance() {
        return cacheableService.getUserByMenuIdAndBtnIdAndProjectIds(MenuConstant.PROBLEM_LIST_ID,
            ButtonConstant.BUTTON_169_ID);
    }

    /**
     * 查询--安全检查计划、安全隐患 检查人
     *
     * @return {@link List< Map< String, Object>>}
     */
    @Override
    public List<Map<String, Object>> projectUserInfoListSafePlan() {
        return cacheableService.getUserByMenuIdAndBtnIdAndProjectIds(MenuConstant.SINSPECTION_PLAN_ID,
            ButtonConstant.BUTTON_176_ID);
    }

    /**
     * 查询--安全隐患整改人
     *
     * @return {@link Object}
     */
    @Override
    public List<Map<String, Object>> projectUserInfoListSafeRectify() {
        return cacheableService.getUserByMenuIdAndBtnIdAndProjectIds(MenuConstant.DANGERS_LIST_ID,
            ButtonConstant.BUTTON_185_ID);
    }

    /**
     * 查询--安全隐患验收人
     *
     * @return {@link Object}
     */
    @Override
    public List<Map<String, Object>> projectUserInfoListSafeAcceptance() {
        return cacheableService.getUserByMenuIdAndBtnIdAndProjectIds(MenuConstant.DANGERS_LIST_ID,
            ButtonConstant.BUTTON_186_ID);
    }

    /**
     * 查询--拥有工作审批的用户
     *
     * @return {@link List< Map< String, Object>>}
     */
    @Override
    public List<Map<String, Object>> userInfoWorkApproval() {
        return cacheableService.getAllUser(MenuConstant.WORK_APPROVAL_ID, null);
    }

    /**
     * 查询--拥有会议管理的用户
     *
     * @return {@link List< Map< String, Object>>}
     */
    @Override
    public List<Map<String, Object>> userInfoManagement() {
        return cacheableService.getAllUser(MenuConstant.MANAGEMENT_MEETINGS_ID, null);
    }

    @Override
    public List<UserProject> findUserProject() {
        LambdaQueryWrapper<UserProject> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserProject::getIsDelete, 0);
        queryWrapper.eq(UserProject::getUserId, FebsUtil.getCurrentUserId());
        return this.userProjectMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUserProjectList(UserProject.Add add) throws FebsException {
        checkParam(add);
        Long projectId = add.getProjectId();
        String userIds2 = add.getUserIds();
        Set<String> userIds = Arrays.stream(userIds2.split(",")).collect(Collectors.toSet());
        // 更新的
        Set<Long> upUserProjectId = new HashSet<Long>();
        // 新增的
        Set<String> userIdAdds = new HashSet<>();
        Set<UserRole> userRoles = new HashSet<>(2);
        Set<UserProject> userProjects = new HashSet<>(2);
        if (!userIds.isEmpty()) {
            for (String s : userIds) {
                if (!StringUtils.isNumeric(s)) {
                    log.error(s + "不是数字");
                    throw new FebsException("新增失败");
                }
            }
            // 查询到旧值
            List<UserProject> userProjectRemove = userProjectMapper.selectList(new LambdaQueryWrapper<UserProject>()
                .select(UserProject::getUserId, UserProject::getId).eq(UserProject::getProjectId, projectId)
                .in(UserProject::getUserId, userIds).eq(UserProject::getIsDelete, 1));
            upUserProjectId = userProjectRemove.stream().map(UserProject::getId).collect(Collectors.toSet());
            Set<Long> rmUserId = userProjectRemove.stream().map(UserProject::getUserId).collect(Collectors.toSet());
            userIdAdds =
                userIds.stream().filter(id -> !rmUserId.contains(Long.valueOf(id))).collect(Collectors.toSet());

            if (!userIdAdds.isEmpty() && userIdAdds.size() != 0) {
                // Set<Long> collect = userRoleMapper
                // .selectList(new LambdaQueryWrapper<UserRole>().select(UserRole::getUserId)
                // .in(UserRole::getUserId, userIdAdds).eq(UserRole::getRoleId, Role.PROJECT_DEFAULT_ROLE))
                // .stream().map(UserRole::getUserId).collect(toSet());
                for (String userId : userIdAdds) {
                    Long aLong = Long.valueOf(userId);
                    // if (!collect.contains(aLong)) {
                    // UserRole userRole = new UserRole();
                    // userRole.setUserId(aLong);
                    // userRole.setRoleId(Role.PROJECT_DEFAULT_ROLE);
                    // userRole.setProjectId(projectId);
                    // userRoles.add(userRole);
                    // }
                    UserProject userProject = new UserProject();
                    userProject.setUserId(aLong);
                    userProject.setProjectId(projectId);
                    userProjects.add(userProject);
                }
            }
        }
        if (!upUserProjectId.isEmpty()) {
            userProjectMapper.update(null, new LambdaUpdateWrapper<UserProject>()
                .in(UserProject::getId, upUserProjectId).set(UserProject::getIsDelete, 0));
        }
        if (!userIdAdds.isEmpty() && userIdAdds.size() != 0) {
            // if (!userRoles.isEmpty()) {
            // userRoleService.saveBatch(userRoles, userRoles.size());
            // }
            if (!userProjects.isEmpty()) {
                this.saveBatch(userProjects, userProjects.size());
            }
            cacheableService.upProjectUser(projectId);
        }
        // 处理role
        for (String userId : userIds) {
            Long aLong = Long.valueOf(userId);
            UserRole userRole = new UserRole();
            userRole.setUserId(aLong);
            userRole.setRoleId(Role.PROJECT_DEFAULT_ROLE);
            userRole.setProjectId(projectId);
            userRoles.add(userRole);
        }
        userRoleService.saveBatch(userRoles, userRoles.size());
    }

    /**
     * 参数校验
     *
     * @param add 实体
     */
    private void checkParam(UserProject.Add add) throws FebsException {
        if (add == null || StringUtils.isEmpty(add.getUserIds())) {
            throw new FebsException("请选择员工加入");
        }
        FebsUtil.isProjectNotNull(add.getProjectId());
        Project project = projectMapper.selectById(add.getProjectId());
        if (project == null || project.getProjectId() == null) {
            throw new FebsException("新增失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUserProject(UserProject userProject) throws FebsException {
        Set<String> roleIds = new HashSet<>();
        try {
            roleIds = Arrays.stream(userProject.getRoleIds().split(",")).collect(toSet());
        } catch (Exception e) {
        }
        FebsUtil.isProjectNotNull(userProject.getProjectId());
        Long projectId = userProject.getProjectId();
        LambdaQueryWrapper<UserRole> eq =
            new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userProject.getUserId());
        if (roleIds.size() > 0) {
            eq.in(UserRole::getRoleId, roleIds);
        }
        List<UserRole> userRoles2 = userRoleMapper.selectList(eq);
        if (roleIds.size() > 0 && userRoles2.size() == roleIds.size()) {
            throw new FebsException("该用户拥有的角色已经存在与此项目中");
        }
        List<UserRole> userRoles1 = userRoleMapper
            .selectList(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userProject.getUserId()));
        Set<Long> roleIdsLong = new HashSet<>();
        if (roleIds.size() == 0) {
            HashSet<Long> projects = new HashSet<>();
            projects.add(projectId);
            List<Map<String, Object>> maps = roleMapper.selectByIds(projects);
            if (maps != null && maps.size() > 0) {
                boolean flag = true;
                for (UserRole userRole : userRoles2) {
                    if (userRole.getRoleId().equals(maps.get(0).get("ROLE_ID"))) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    roleIdsLong.add((Long)maps.get(0).get("ROLE_ID"));
                }
            }
        } else {
            if (userRoles1 == null || userRoles1.size() == 0) {
                for (String roleId : roleIds) {
                    roleIdsLong.add(Long.valueOf(roleId));
                }
            } else {
                for (String roleId : roleIds) {
                    userRoles1.forEach(userRole -> {
                        if (!roleId.equals(userRole.getRoleId().toString())) {
                            roleIdsLong.add(userRole.getRoleId());
                        }
                    });
                }
            }
        }
        ArrayList<UserRole> userRoles = new ArrayList<UserRole>();
        roleIdsLong.forEach(roleId1 -> {
            // userrole 添加
            UserRole userRole = new UserRole();
            userRole.setRoleId(roleId1);
            userRole.setUserId(userProject.getUserId());
            userRoles.add(userRole);
        });
        if (userRoles.size() > 0) {
            userRoleService.saveBatch(userRoles, userRoles.size());
        }
        Integer integer1 =
            userProjectMapper.selectCount(new LambdaQueryWrapper<UserProject>().select(UserProject::getId)
                .eq(UserProject::getProjectId, projectId).eq(UserProject::getUserId, userProject.getUserId()));
        if (integer1 == 0) {
            userProject.setProjectId(projectId);
            this.save(userProject);
            cacheableService.upProjectUser(projectId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserProject(UserProject userProject) {
        Long userId = FebsUtil.getCurrentUserId();
        LambdaQueryWrapper<UserProject> eq = new LambdaQueryWrapper<UserProject>().eq(UserProject::getUserId, userId)
            .eq(UserProject::getIsDefaultproject, 1).ne(UserProject::getId, userProject.getId());
        List<UserProject> userProjects = this.userProjectMapper.selectList(eq);
        if (userProjects != null && userProjects.size() > 0) {
            userProjects.forEach(u -> u.setIsDefaultproject(0));
            userProjects.add(userProject);
            this.updateBatchById(userProjects);
        } else {
            this.updateById(userProject);
        }
    }

    /**
     * %%%%
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserProject(UserProject.Add userProject) throws FebsException {
        FebsUtil.isProjectNotNull(userProject.getProjectId());
        String userIds = userProject.getUserIds();
        String[] split = null;
        if (!StringUtils.isEmpty(userIds)) {
            split = userIds.split(",");
            for (String s : split) {
                if (!StringUtils.isNumeric(s)) {
                    log.error(s + "不是数字");
                    throw new FebsException("移除失败");
                }
            }
        } else {
            throw new FebsException("请选择用户");
        }
        Set<String> collect = Arrays.stream(split).collect(toSet());
        if (collect.contains(FebsUtil.getCurrentUserId().toString())) {
            throw new FebsException("错误！本人无法移除本人");
        }
        // Project project = projectMapper.selectOne(new LambdaQueryWrapper<Project>().select(Project::getProjectUserid)
        // .eq(Project::getProjectId, userProject.getProjectId()));
        // Long projectUserid = project.getProjectUserid();
        // if (projectUserid != null && collect.contains(projectUserid.toString())) {
        // // if (!AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) {
        // throw new FebsException("无法将负责人移除项目");
        // // }
        // // projectMapper.update(null, new LambdaUpdateWrapper<Project>()
        // // .eq(Project::getProjectId, userProject.getProjectId())
        // // .set(Project::getProjectUserid, null)
        // // );
        // }

        // cacheEvictService.setProjectByUser(userProject.getUserId(), projectId);
        this.update(null,
            new LambdaUpdateWrapper<UserProject>().eq(UserProject::getProjectId, userProject.getProjectId())
                .in(UserProject::getUserId, collect).set(UserProject::getIsDelete, 1));

        // 去删除角色关联信息
        userRoleService
            .remove(new LambdaUpdateWrapper<UserRole>().eq(UserRole::getProjectId, userProject.getProjectId())
                .in(UserRole::getUserId, collect).eq(UserRole::getRoleId, Role.PROJECT_DEFAULT_ROLE));

        cacheableService.upProjectUser(userProject.getProjectId());
    }

    @Override
    public Map<String, Object> toggleProject(Long projectId, String type, String password) throws FebsException {
        Long userId = FebsUtil.getCurrentUserId();
        Integer integer = userProjectMapper.selectCount(new LambdaQueryWrapper<UserProject>().select(UserProject::getId)
            .eq(UserProject::getUserId, userId).eq(UserProject::getProjectId, projectId));
        if (integer < 1) {
            throw new FebsException("项目不存在");
        }
        // 清除token
        // longOut();
        // 项目id设置
        // redisService.set(FebsUtil.getUserProjectRedisKey(), projectId);
        // 验证码
        // String captchaKey = "captchaKey123456" + FebsUtil.getCurrentUserId() + projectId;
        // Object codeInRedis = getcode(captchaKey);
        // //登录信息
        // Map tokenMapInfo = getTokenMapInfo(type, password, captchaKey, codeInRedis);
        HashMap<String, Object> resultMap = new HashMap<>(2);
        // resultMap.put("tokenInfo", tokenMapInfo);
        resultMap.put("projectInfo", this.projectMapper.selectById(projectId));
        // 权限处理
        return resultMap;
    }

    @Override
    public List<UserProject> getAllProject(Long userId) {
        List<Project> projects = projectMapper.selectList(new LambdaQueryWrapper<Project>()
            .select(Project::getProjectId, Project::getProjectName).eq(Project::getIsDelete, 0));
        List<UserProject> userProjects = userProjectMapper.selectList(
            new LambdaQueryWrapper<UserProject>().eq(UserProject::getUserId, userId).eq(UserProject::getIsDelete, 0));
        Set<Long> projectIdSet = userProjects.stream().map(UserProject::getProjectId).collect(toSet());

        projects.forEach(project -> {
            if (projectIdSet.size() == 0 || !projectIdSet.contains(project.getProjectId())) {
                UserProject userProject = new UserProject();
                userProject.setUserId(userId);
                userProject.setProjectId(project.getProjectId());
                userProject.setProjectName(project.getProjectName());
                userProject.setIsDefaultproject(0);
                userProject.setIsUserProject(0);
                userProjects.add(userProject);
            } else {
                for (UserProject userProject : userProjects) {
                    if (userProject.getProjectId().equals(project.getProjectId())) {
                        userProject.setIsUserProject(1);
                        userProject.setProjectName(project.getProjectName());
                    }
                }
            }
        });
        return userProjects;
    }

    @Override
    public List<UserProject> getAllUserProject() {
        return Collections.emptyList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateUserAllProject(List<UserProject> userProjects) {
        ArrayList<UserProject> deleteUserProject = new ArrayList<>();
        ArrayList<UserProject> insertUserProject = new ArrayList<>();
        HashSet<Long> projects = new HashSet<>();
        userProjects.forEach(userProject -> projects.add(userProject.getProjectId()));
        if (projects.size() == 0) {
            return "失败";
        }
        List<Map<String, Object>> maps = roleMapper.selectByIds(projects);
        for (UserProject userProject : userProjects) {
            if (userProject.getId() == null) {
                if (userProject.getIsUserProject() == 1) {
                    insertUserProject.add(userProject);
                }
            } else {
                if (userProject.getIsUserProject() == 0) {
                    deleteUserProject.add(userProject);
                }
            }
        }
        delete(userProjects, deleteUserProject);
        insert(insertUserProject, maps);
        cacheableService.upProjectUser();
        return "成功";
    }

    @Override
    public void updateAllProjectNew(String projectIds, Long userId) throws FebsException {
        if (StringUtils.isEmpty(projectIds) || userId == null) {
            throw new FebsException("更新失败");
        }
        String[] split = projectIds.split(",");
        for (String s : split) {
            if (!org.apache.commons.lang3.StringUtils.isNumeric(s)) {
                log.error(s + "不是数字");
                throw new FebsException("更新失败");
            }
        }
        Set<String> newProjectIds = Arrays.stream(split).collect(toSet());
        List<UserProject> saveOrUpdate = new ArrayList<>();
        List<UserProject> userProjects = userProjectMapper.selectList(new LambdaQueryWrapper<UserProject>()
            .select(UserProject::getId, UserProject::getProjectId).eq(UserProject::getUserId, userId));
        Set<Long> oldProjectIds = userProjects.stream().map(UserProject::getProjectId).collect(toSet());
        // 找出要新增的
        newProjectIds.forEach(newProjectId -> {
            Long aLong = Long.valueOf(newProjectId);
            if (!oldProjectIds.contains(aLong)) {
                UserProject userProject = new UserProject();
                userProject.setUserId(userId);
                userProject.setProjectId(aLong);
                saveOrUpdate.add(userProject);
            }
        });
        // 找出要删除的
        userProjects.forEach(userProject -> {
            Long projectId = userProject.getProjectId();
            if (projectId != null) {
                String s = String.valueOf(projectId);
                if (!newProjectIds.contains(s)) {
                    userProject.setIsDelete(1);
                    saveOrUpdate.add(userProject);
                }
            }
        });
        this.saveOrUpdateBatch(saveOrUpdate);
        cacheableService.upProjectUser();

    }

    @Override
    public List<Map<String, Object>> getAllProjectChooses(String menuId, Integer buttonId) {
        Integer[] menuInfo = MenuConstant.FRONT_ENCRY_MENU.get(menuId);
        if (menuInfo == null) {
            return Collections.emptyList();
        }
        return cacheableService.getAllProjectChooses(menuInfo[0], buttonId);
    }

    @Override
    public List<Map<String, Object>> getAllUserByProject() {
        Set<Long> projectIds = cacheableService.getUserAuth(FebsUtil.getCurrentUserId()).getProjectIds();
        return cacheableService.getAllUserByProject(projectIds);
    }

    @Override
    public List<Map<String, Object>> getAllUser() {
        return cacheableService.getAllUser();
    }

    @Override
    public List<Map<String, Object>> getAllUserNew() {
        return cacheableService.getAllUserNew();
    }

    @Override
    public List<Map<String, Object>> getUserAllProject(String menuId) {
        Integer[] menuInfo = MenuConstant.FRONT_ENCRY_MENU.get(menuId);
        if (menuInfo == null) {
            return Collections.emptyList();
        }
        return cacheableService.getAllProject(menuInfo[0], menuInfo[1]);
    }

    /**
     * 级联删除用户项目，用户项目权限
     *
     * @param userProjects
     * @param deleteUserProject
     */
    private void delete(List<UserProject> userProjects, ArrayList<UserProject> deleteUserProject) {
        if (deleteUserProject.size() > 0) {
            // 项目id
            HashSet<Long> deleteProjectIds = new HashSet<>();
            // userproject id
            HashSet<Long> deleteUserProjectIds = new HashSet<>();
            deleteUserProject.forEach(deleteUserProject1 -> {
                deleteProjectIds.add(deleteUserProject1.getProjectId());
                deleteUserProjectIds.add(deleteUserProject1.getId());
            });
            // userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId,
            // userProjects.get(0).getUserId()).in(UserRole::getProjectId, deleteProjectIds));
            if (deleteUserProjectIds.size() > 0) {
                // userProjectMapper.deleteBatchIds(deleteUserProjectIds);
                userProjectMapper.update(null, new LambdaUpdateWrapper<UserProject>()
                    .in(UserProject::getId, deleteUserProjectIds).set(UserProject::getIsDelete, 1));
            }
        }
    }

    /**
     * 级联添加用户项目，用户项目权限
     *
     * @param insertUserProject
     * @param projcetRoleMaps
     */
    private void insert(ArrayList<UserProject> insertUserProject, List<Map<String, Object>> projcetRoleMaps) {
        if (insertUserProject.size() > 0) {
            Long userId = insertUserProject.get(0).getUserId();
            HashSet<Long> updateProjectIds = new HashSet<>();
            HashSet<Long> installUserIds = new HashSet<>();
            HashSet<Long> installUserId1s = new HashSet<>();
            insertUserProject.forEach(userProject -> installUserIds.add(userProject.getProjectId()));
            if (installUserIds.size() > 0) {
                // 还有update
                List<UserProject> userProjectUpdates =
                    userProjectMapper.selectList(new LambdaQueryWrapper<UserProject>().select(UserProject::getProjectId)
                        .eq(UserProject::getUserId, userId).eq(UserProject::getIsDelete, 1));
                if (userProjectUpdates.size() > 0) {
                    userProjectUpdates.forEach(userProject -> {
                        Long projectId = userProject.getProjectId();
                        if (installUserIds.contains(projectId)) {
                            insertUserProject.removeIf(userProject1 -> projectId.equals(userProject1.getProjectId()));
                            updateProjectIds.add(projectId);
                        }
                    });
                }

                ArrayList<UserRole> userRoles = new ArrayList<>();
                insertUserProject.forEach(insertUser -> {
                    UserRole userRole = new UserRole();
                    projcetRoleMaps.forEach(map -> {
                        if (insertUser.getProjectId().equals(map.get("PROJECT_ID"))) {
                            userRole.setRoleId((Long)map.get("ROLE_ID"));
                        }
                    });
                    userRole.setUserId(insertUser.getUserId());
                    userRoles.add(userRole);
                });
                // cacheEvictService.setUserByProject();
                // cacheEvictService.setProjectByUser();
                if (updateProjectIds.size() > 0) {
                    userProjectMapper.update(null,
                        new LambdaUpdateWrapper<UserProject>().eq(UserProject::getUserId, userId)
                            .in(UserProject::getProjectId, updateProjectIds).set(UserProject::getIsDelete, 0));
                }

                if (userRoles.size() > 0) {
                    userRoleService.saveBatch(userRoles, userRoles.size());
                }
                if (insertUserProject.size() > 0) {
                    this.saveBatch(insertUserProject, insertUserProject.size());
                }
            }
        }

    }

    /**
     * token信息
     *
     * @param type 登录类型
     * @param password 密码
     * @param captchaKey 验证码key
     * @param codeInRedis 验证码
     * @return {@link Map}
     */
    private Map getTokenMapInfo(String type, String password, String captchaKey, Object codeInRedis) {
        MultiValueMap<String, Object> requestMap = new LinkedMultiValueMap<String, Object>(6);
        requestMap.add("grant_type", "password");
        requestMap.add("username", FebsUtil.getCurrentUsername());
        requestMap.add("password", password);
        requestMap.add("key", captchaKey);
        requestMap.add("code", codeInRedis);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic ZmViczoxMjM0NTY=");
        headers.add(ParamsConstant.LOGIN_TYPE, type);
        HttpEntity<MultiValueMap<String, Object>> httpEntity =
            new HttpEntity<MultiValueMap<String, Object>>(requestMap, headers);
        Map tokenMapInfo = restTemplate.postForEntity(tokenInfoUri, httpEntity, Map.class).getBody();
        return tokenMapInfo;
    }

    /**
     * 验证码
     *
     * @param captchaKey key
     * @return {@link Object}
     */
    private Object getcode(String captchaKey) {
        restTemplate.getForEntity(captchaUrl + captchaKey, String.class);
        Object codeInRedis = redisService.get(FebsConstant.CODE_PREFIX + captchaKey);
        return codeInRedis;
    }

    /**
     * 退出登录
     */
    private void longOut() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "bearer " + FebsUtil.getCurrentTokenValue());
        restTemplate.postForEntity(removeTokenUrl,
            new HttpEntity<MultiValueMap<String, Object>>(new LinkedMultiValueMap<String, Object>(), headers),
            String.class);
    }
}
