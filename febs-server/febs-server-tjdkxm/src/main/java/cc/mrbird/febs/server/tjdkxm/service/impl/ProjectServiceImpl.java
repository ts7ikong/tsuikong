package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.constant.ButtonConstant;
import cc.mrbird.febs.common.core.entity.constant.FebsConstant;
import cc.mrbird.febs.common.core.entity.constant.MenuConstant;
import cc.mrbird.febs.common.core.entity.system.Role;
import cc.mrbird.febs.common.core.entity.system.UserRole;
import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
import cc.mrbird.febs.common.core.entity.tjdkxm.Project;
import cc.mrbird.febs.common.core.entity.tjdkxm.ProjectRelate;
import cc.mrbird.febs.common.core.entity.tjdkxm.UserProject;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.common.core.utils.SortUtil;
import cc.mrbird.febs.server.tjdkxm.config.LogRecordContext;
import cc.mrbird.febs.server.tjdkxm.mapper.ProjectMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.UserProjectMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.UserRoleMapper;
import cc.mrbird.febs.server.tjdkxm.service.CacheableService;
import cc.mrbird.febs.server.tjdkxm.service.ProjectRelateService;
import cc.mrbird.febs.server.tjdkxm.service.ProjectService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * ProjectService实现
 *
 * @author zlkj_cg
 * @date 2022-01-12 15:51:03
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private UserProjectMapper userProjectMapper;
    @Autowired
    private CacheableService cacheableService;
    @Autowired
    private ProjectRelateService projectRelateService;
    @Autowired
    private LogRecordContext logRecordContext;

    @Override
    public IPage<Project> findProjects(QueryRequest request, Project.Params project) {
        AuthUserModel userAuth = cacheableService.getAuthUser(MenuConstant.PROJECTINFO_ID);
        LambdaQueryWrapper<Project> queryWrapper = new LambdaQueryWrapper<>();
        if (AuthUserModel.KEY_NONE.equals(userAuth.getKey())) {
            return new Page<>();
        }
        queryWrapper.eq(Project::getIsDelete, 0);
        queryWrapper.orderByDesc(Project::getCreateTime);
        Page<Project> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, null, FebsConstant.ORDER_DESC, true);
        if (StringUtils.isNotBlank(project.getProjectCode())) {
            queryWrapper.and(wapper -> {
                wapper.or().like(Project::getProjectCode, project.getProjectCode());
                wapper.or().like(Project::getProjectName, project.getProjectCode());
            });
        }
        if (!AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) {
            queryWrapper.in(Project::getProjectId, userAuth.getProjectIds());
        }
        this.projectMapper.selectPageInfo(page, queryWrapper);
        return page;
    }

    @Override
    public List<Map<String, Object>> findProjects(Project project) {
        QueryWrapper<Project> queryWrapper = Wrappers.query();
        queryWrapper.eq("IS_DELETE", 0);
        queryWrapper.select("PROJECT_ID as projectId", "PROJECT_NAME as projectName");
        return this.listMaps(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createProject(Project project1) throws FebsException {
        project1.setCreateTime(new Date());
        Long userId = FebsUtil.getCurrentUserId();
        project1.setCreateUserid(userId);
        // 创建项目 --》 --》--》UserProject新增
        // 创建项目
        this.projectMapper.insert(project1);
        final Project project = project1;
        if (project.getProjectId() == null) {
            throw new FebsException("创建项目失败");
        }
        Long projectId = project.getProjectId();
        List<ProjectRelate> list = new ArrayList<>();
        if (StringUtils.isNotBlank(project.getJsdw())) {
            List<ProjectRelate> projectRelates = JSON.parseArray(project.getJsdw(), ProjectRelate.class);
            list.addAll(projectRelates);
            projectRelates.forEach(projectRelate -> {
                projectRelate.setType("1");
                list.add(projectRelate);
            });
        }
        if (StringUtils.isNotBlank(project.getSgdw())) {
            List<ProjectRelate> projectRelates = JSON.parseArray(project.getSgdw(), ProjectRelate.class);
            list.addAll(projectRelates);
            projectRelates.forEach(projectRelate -> {
                projectRelate.setType("2");
                list.add(projectRelate);
            });
        }
        if (StringUtils.isNotBlank(project.getJldw())) {
            List<ProjectRelate> projectRelates = JSON.parseArray(project.getJldw(), ProjectRelate.class);
            projectRelates.forEach(projectRelate -> {
                projectRelate.setType("3");
                list.add(projectRelate);
            });
        }
        if (StringUtils.isNotBlank(project.getSjdw())) {
            List<ProjectRelate> projectRelates = JSON.parseArray(project.getSjdw(), ProjectRelate.class);
            projectRelates.forEach(projectRelate -> {
                projectRelate.setType("4");
                list.add(projectRelate);
            });
        }
        if (StringUtils.isNotBlank(project.getKcdw())) {
            List<ProjectRelate> projectRelates = JSON.parseArray(project.getKcdw(), ProjectRelate.class);
            projectRelates.forEach(projectRelate -> {
                projectRelate.setType("5");
                list.add(projectRelate);
            });
        }
        if (StringUtils.isNotBlank(project.getAuditdw())) {
            List<ProjectRelate> projectRelates = JSON.parseArray(project.getAuditdw(), ProjectRelate.class);
            projectRelates.forEach(projectRelate -> {
                projectRelate.setType("6");
                list.add(projectRelate);
            });
        }
        if (list.size() > 0) {
            list.forEach(projectRelate -> projectRelate.setTableId(project.getProjectId().toString()));
            projectRelateService.saveBatch(list, list.size());
        }


        // 项目负责人角色绑定
        addUserRole(project1.getProjectUserid(), projectId);
        // 用户和项目绑定
//        addUserProjects(projectId, project1.getProjectUserid());
        // 通用角色 绑定
        // List<UserRole> userRoles = userRoleMapper.getHasSpecialPermissionsUserAndRole();
        // userRoles.parallelStream().forEach(s -> {
        // s.setProjectId(projectId);
        // });
        // SqlHelper.executeBatch(UserRole.class, this.log, userRoles, userRoles.size(),
        // (sqlSession, entity) -> sqlSession
        // .insert(SqlHelper.getSqlStatement(UserRoleMapper.class, SqlMethod.INSERT_ONE), entity));
        // 用户和项目绑定
        cacheableService.upgetAllProjectChooses(projectId);
        logRecordContext.putVariable("id", projectId);
    }

    /**
     * 项目管理员角色绑定
     *
     * @param projectId 项目id
     * @param userId    用户id
     */
    public void addUserRole(Long userId, Long projectId) {
        Integer integer = userRoleMapper.selectCount(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId)
                .eq(UserRole::getRoleId, Role.PROJECT_ADMIN_ROLE).eq(UserRole::getProjectId, projectId));
        if (integer <= 0) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(Role.PROJECT_ADMIN_ROLE);
            userRole.setProjectId(projectId);
            userRoleMapper.insert(userRole);
        }
    }

    /**
     * 用户和项目绑定
     *
     * @param projectId 项目id
     * @param userId    用户id
     */

    private void addUserProjects(Long projectId, Long userId) {
        userProjectMapper.delete(new LambdaQueryWrapper<UserProject>().eq(UserProject::getUserId, userId)
                .eq(UserProject::getProjectId, projectId));
        UserProject userProject = new UserProject();
        userProject.setUserId(userId);
        userProject.setProjectId(projectId);
        userProject.setIsDefaultproject(1);
        userProjectMapper.insert(userProject);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProject(Project project) throws FebsException {
        if (project != null && project.getProjectId() != null) {
            Long userId = project.getProjectUserid() != null ? project.getProjectUserid() : null;
            Project project2 = projectMapper.selectById(project.getProjectId());
            if (project2 == null) {
                throw new FebsException("选择数据");
            }
            cacheableService.hasPermission(null, MenuConstant.PROJECTINFO_ID, ButtonConstant.BUTTON_94_ID, null);
            boolean b = this.updateById(project);
            if (b) {
                // 修改了负责人
                if (userId != null) {
                    setRelation(project2.getProjectUserid(), project2.getProjectId(), userId);
                }
                List<ProjectRelate> list = new ArrayList<>();
                try {
                    if (StringUtils.isNotBlank(project.getJsdw())) {
                        List<ProjectRelate> projectRelates = JSON.parseArray(project.getJsdw(), ProjectRelate.class);
                        projectRelates.forEach(projectRelate -> {
                            projectRelate.setType("1");
                            list.add(projectRelate);
                        });
                    }
                    if (StringUtils.isNotBlank(project.getSgdw())) {
                        List<ProjectRelate> projectRelates = JSON.parseArray(project.getSgdw(), ProjectRelate.class);
                        projectRelates.forEach(projectRelate -> {
                            projectRelate.setType("2");
                            list.add(projectRelate);
                        });
                    }
                    if (StringUtils.isNotBlank(project.getJldw())) {
                        List<ProjectRelate> projectRelates = JSON.parseArray(project.getJldw(), ProjectRelate.class);
                        projectRelates.forEach(projectRelate -> {
                            projectRelate.setType("3");
                            list.add(projectRelate);
                        });
                    }
                    if (StringUtils.isNotBlank(project.getSjdw())) {
                        List<ProjectRelate> projectRelates = JSON.parseArray(project.getSjdw(), ProjectRelate.class);
                        projectRelates.forEach(projectRelate -> {
                            projectRelate.setType("4");
                            list.add(projectRelate);
                        });
                    }
                    if (StringUtils.isNotBlank(project.getKcdw())) {
                        List<ProjectRelate> projectRelates = JSON.parseArray(project.getKcdw(), ProjectRelate.class);
                        projectRelates.forEach(projectRelate -> {
                            projectRelate.setType("5");
                            list.add(projectRelate);
                        });
                    }
                    if (StringUtils.isNotBlank(project.getAuditdw())) {
                        List<ProjectRelate> projectRelates = JSON.parseArray(project.getAuditdw(), ProjectRelate.class);
                        projectRelates.forEach(projectRelate -> {
                            projectRelate.setType("6");
                            list.add(projectRelate);
                        });
                    }
                    if (list.size() > 0) {
                        list.forEach(projectRelate -> projectRelate.setTableId(project.getProjectId().toString()));
                        projectRelateService.remove(new LambdaQueryWrapper<ProjectRelate>()
                                .eq(ProjectRelate::getTableId, project.getProjectId().toString()));
                        projectRelateService.saveBatch(list, list.size());
                    }
                } catch (Exception e) {
                    throw new FebsException("修改项目失败");
                }
                if (project.getProjectName() != null) {
                    cacheableService.upgetAllProjectChooses(project.getProjectId());
                }
            }
        } else {
            throw new FebsException("请选择项目");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProject(Project.Params project) throws FebsException {
        AuthUserModel userAuth = cacheableService.getAuthUser(MenuConstant.PROJECTINFO_ID);
        if (!AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) {
            Integer integer =
                    userProjectMapper.selectCount(new LambdaQueryWrapper<UserProject>().select(UserProject::getId)
                            .eq(UserProject::getProjectId, project.getProjectId()).eq(UserProject::getIsDelete, 0));
            if (integer > 0) {
                throw new FebsException("该项目还有人使用,需要强制删除请联系管理员");
            }
        }
        Project project1 = projectMapper.selectById(project.getProjectId());
        if (project1 == null) {
            throw new FebsException("请选择项目");
        }
        Long projectId = project1.getProjectId();
        Long projectUserid = project1.getProjectUserid();
        // 项目删除
        projectMapper.update(null,
                new LambdaUpdateWrapper<Project>().eq(Project::getProjectId, projectId).set(Project::getIsDelete, 1));
        userProjectMapper.update(null, new LambdaUpdateWrapper<UserProject>()
                .eq(UserProject::getProjectId, projectId).set(UserProject::getIsDelete, 1));

        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, projectUserid)
                .eq(UserRole::getProjectId, projectId).eq(UserRole::getRoleId, Role.PROJECT_ADMIN_ROLE));
        cacheableService.deletePorject(projectId);
        cacheableService.upgetAllProjectChooses(projectId);
    }

    /**
     * 根据id查询
     *
     * @param projectIds 项目ids
     * @return {@link List< Project>}
     */
    @Override
    public List<Project> getByIds(Set<Long> projectIds) {
        return list(new LambdaQueryWrapper<Project>().select(Project::getProjectId, Project::getProjectName)
                .in(Project::getProjectId, projectIds));
    }

    /**
     * 修改 项目负责人
     *
     * @param project 项目信息实体类
     */
    @Override
    public void updateProject(Project.UserModel project) throws FebsException {
        Long projectId = project.getProjectId();
        Project project2 = projectMapper.selectById(projectId);
        if (project2 == null) {
            throw new FebsException("请选择项目修改");
        }
        cacheableService.hasPermission(null, MenuConstant.PROJECTINFO_ID, ButtonConstant.BUTTON_297_ID, null);
        Project project1 = new Project();
        project1.setProjectId(projectId);
        project1.setProjectUserid(project.getProjectUserid());
        project1.setProjectPerson(project.getProjectPerson());
        project1.setProjectLink(project.getProjectLink());
        projectMapper.updateById(project1);
        setRelation(project2.getProjectUserid(), projectId, project.getProjectUserid());
    }

    private void setRelation(Long oldUserId, Long projectId, Long newUserId) {
        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, oldUserId)
                .eq(UserRole::getRoleId, Role.PROJECT_ADMIN_ROLE).eq(UserRole::getProjectId, projectId));
        userProjectMapper.delete(new LambdaQueryWrapper<UserProject>().eq(UserProject::getUserId, oldUserId)
                .eq(UserProject::getProjectId, projectId));
        // 关联
//        UserProject userProject = new UserProject();
//        userProject.setProjectId(projectId);
//        userProject.setUserId(newUserId);
//        userProjectMapper.insert(userProject);

        UserRole userRole = new UserRole();
        userRole.setUserId(newUserId);
        userRole.setRoleId(Role.PROJECT_ADMIN_ROLE);
        userRole.setProjectId(projectId);
        userRoleMapper.insert(userRole);
    }
}
