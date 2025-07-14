package cc.mrbird.febs.server.tjdkxm.service.impl;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

import cc.mrbird.febs.common.core.entity.constant.MenuConstant;
import cc.mrbird.febs.common.core.entity.constant.RedisKey;
import cc.mrbird.febs.common.core.entity.system.MenuUserAuthDto;
import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
import cc.mrbird.febs.common.core.entity.tjdkxm.*;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.common.redis.service.RedisService;
import cc.mrbird.febs.server.tjdkxm.mapper.*;
import cc.mrbird.febs.server.tjdkxm.service.CacheableService;
import cc.mrbird.febs.server.tjdkxm.util.CacheableServiceUtils;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/8 17:05
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class CacheableServiceImpl implements CacheableService {
    @Autowired
    private QualityplanMapper qualityplanMapper;
    @Autowired
    private SafeplanMapper safeplanMapper;
    @Autowired
    private QualityproblemMapper qualityproblemMapper;
    @Autowired
    private SafeproblemMapper safeproblemMapper;
    @Autowired
    private AskfleaveMapper askfleaveMapper;
    @Autowired
    private ReportingMapper reportingMapper;
    @Autowired
    private ParcelUnitMapper parcelUnitMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private DocumentClassMapper documentClassMapper;
    @Autowired
    private ConferenceMapper conferenceMapper;
    private static final CacheableServiceUtils CACHEABLE_SERVICE_UTILS = new CacheableServiceUtils();
    @Autowired
    private RedisService redisService;
    @Autowired
    private ParcelMapper parcelMapper;
    @Autowired
    private SubitemMapper subitemMapper;
    @Autowired
    private UnitEngineMapper unitEngineMapper;
    @Autowired
    private SgrzMapper sgrzMapper;
    @Autowired
    private ZzaqglryrzMapper zzaqglryrzMapper;
    @Autowired
    private MajorProjectLogMapper majorProjectLogMapper;

    /**
     * 查询（项目中所有的单位工程、分部、分项）
     *
     * @return List<Project>
     */
    @Override
    public List<Map<String, Object>> getAllProjectChooses(Integer menuId, Integer buttonId) {
        List<Map<String, Object>> result = new ArrayList<>();
        Long userId = FebsUtil.getCurrentUserId();
        AuthUserModel authUser = getAuthUser(userId, menuId, buttonId);
        List<Map<String, Object>> results = getProjectAllChooses();
        authUser.getProjectIds().forEach(id -> results.forEach(result1 -> {
            if (id.toString().equals(result1.get("projectId").toString())) {
                result.add(result1);
            }
        }));
        return result;
    }

    /**
     * 获取所有项目 所有的单位工程、分部、分项
     *
     * @return {@link List}
     */
    private List<Map<String, Object>> getProjectAllChooses() {
        String key = RedisKey.PROJECT_ALL;
        if (Boolean.TRUE.equals(redisService.hasKey(key))) {
            return (List<Map<String, Object>>) redisService.get(key);
        } else {
            List<Map<String, Object>> resultList = new ArrayList<>();
            List<Map<String, Object>> projectModels = projectMapper.selectAllChoosesAll();
            Set<Object> projectIds =
                    projectModels.stream().map(model -> model.get("PROJECT_ID")).collect(Collectors.toSet());
            projectIds.forEach(projectId -> {
                List<Map<String, Object>> collect = projectModels.stream()
                        .filter(projectMap -> projectMap.get("PROJECT_ID").equals(projectId)).collect(Collectors.toList());
                resultList.add(CACHEABLE_SERVICE_UTILS.projectChoose(collect));
            });
            redisService.set(key, resultList, 60 * RedisKey.ONE_TIME);
            return resultList;
        }

    }

    /**
     * 查询 所有拥有项目
     *
     * @return List<Project>
     */
    @Override
    public List<Map<String, Object>> getAllProject(Integer menuId, Integer buttonId) {
        try {
            AuthUserModel authUser = getAuthUser(FebsUtil.getCurrentUserId(), menuId, buttonId);
            LambdaQueryWrapper<Project> projectLambdaQueryWrapper = new LambdaQueryWrapper<>();
            projectLambdaQueryWrapper.select(Project::getProjectId, Project::getProjectName);
            projectLambdaQueryWrapper.in(Project::getProjectId, authUser.getProjectIds());
            projectLambdaQueryWrapper.eq(Project::getIsDelete, 0);
            return projectMapper.selectMaps(projectLambdaQueryWrapper);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * 更新（项目中所有的单位工程、分部、分项）
     *
     * @param projectId 项目id
     */
    @Override
    public void upgetAllProjectChooses(Long projectId) {
        redisService.del(RedisKey.PROJECT_ALL);
        Map<String, Object> map = new HashMap<>(2);
        map.put("-1", Collections.emptyMap());
        redisService.hmset(RedisKey.PROJECT_USER_ALL, map, 1L);
        getProjectAllChooses();
    }

    /**
     * 更新（项目中所有的单位工程、分部、分项）
     *
     * @param projectId 项目id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePorject(Long projectId) {
        unitEngineMapper.update(null, new LambdaUpdateWrapper<UnitEngine>().eq(UnitEngine::getUnitProjectid, projectId)
                .set(UnitEngine::getIsDelete, 1));
        parcelMapper.update(null,
                new LambdaUpdateWrapper<Parcel>().eq(Parcel::getParcelProjectid, projectId).set(Parcel::getIsDelete, 1));
        subitemMapper.update(null, new LambdaUpdateWrapper<Subitem>().eq(Subitem::getSubitemProjectid, projectId)
                .set(Subitem::getIsDelete, 1));
        qualityplanMapper.update(null, new LambdaUpdateWrapper<Qualityplan>()
                .eq(Qualityplan::getQualityplanProjectid, projectId).set(Qualityplan::getIsDelete, 1));
        qualityproblemMapper.update(null, new LambdaUpdateWrapper<Qualityproblem>()
                .eq(Qualityproblem::getQualityproblenProjectid, projectId).set(Qualityproblem::getIsDelete, 1));
        safeplanMapper.update(null, new LambdaUpdateWrapper<Safeplan>().eq(Safeplan::getSafeplanProjectid, projectId)
                .set(Safeplan::getIsDelete, 1));
        safeproblemMapper.update(null, new LambdaUpdateWrapper<Safeproblem>()
                .eq(Safeproblem::getSafeproblenProjectid, projectId).set(Safeproblem::getIsDelete, 1));
        sgrzMapper.update(null,
                new LambdaUpdateWrapper<Sgrz>().eq(Sgrz::getSgrzProjectid, projectId).set(Sgrz::getIsDelete, 1));
        majorProjectLogMapper.update(null, new LambdaUpdateWrapper<MajorProjectLog>()
                .eq(MajorProjectLog::getMajorProjectid, projectId).set(MajorProjectLog::getIsDelete, 1));
        zzaqglryrzMapper.update(null, new LambdaUpdateWrapper<Zzaqglryrz>()
                .eq(Zzaqglryrz::getZzaqglryrzProjectid, projectId).set(Zzaqglryrz::getIsDelete, 1));
    }

    /**
     * 查询所有
     *
     * @return {@link List}
     */
    @Override
    public List<Map<String, Object>> getAllUser() {
        String key = RedisKey.USER_ALL;
        if (Boolean.TRUE.equals(redisService.hasKey(key))) {
            return (List<Map<String, Object>>) redisService.get(key);
        }
        List<Map<String, Object>> user = userMapper.getAllUser();
        redisService.set(key, user, 24 * 60 * RedisKey.ONE_TIME);
        return user;
    }

    /**
     * 查询所有用户
     *
     * @param menuId
     * @param buttonId
     * @return {@link List}
     */
    @Override
    public List<Map<String, Object>> getAllUser(Integer menuId, Integer buttonId) {
        return userMapper.getUserListNewByMenuIdAndButotnId(menuId, buttonId);
    }

    /**
     * 查询所有
     *
     * @return {@link List}
     */
    @Override
    public List<Map<String, Object>> getAllUserNew() {
        return userMapper.getAllUserNew();
    }

    @Override
    public Set<Long> getUserAllProjectId(Long userId) {
        String key = RedisKey.DATA_PROJECT + userId;
        return (Set<Long>) redisService.get(key);
    }

    /**
     * 查询（项目中所有的用户 )
     *
     * @return List<Project>
     */
    @Override
    public List<Map<String, Object>> getAllUserByProject(Set<Long> projectIds) {
        HashSet<Long> projectIds1 = new HashSet<>(projectIds);
        List<Map<String, Object>> result = new ArrayList<>();
        if (projectIds1.isEmpty()) {
            return getAllUserByProjectMothod(projectIds1);
        } else {
            projectIds1.forEach(projectId -> {
                if (projectId != null) {
                    result.add(getAllUserByProject(projectId));
                }
            });
        }
        return result;
    }

    /**
     * 项目中拥有该角色的用户信息
     *
     * @param menuId   菜单id
     * @param buttonId 按钮id
     * @return {@link List}
     */
    @Override
    public List<Map<String, Object>> getUserByMenuIdAndBtnIdAndProjectIds(Integer menuId, Integer buttonId) {
        AuthUserModel userAuth = getUserAuth();
        String key = RedisKey.PROJECT_MENU_BTN_REF + userAuth.getUserId() + "_" + menuId + "_" + buttonId;
        if (Boolean.TRUE.equals(redisService.hasKey(key))) {
            return (List<Map<String, Object>>) redisService.get(key);
        }
        Set<Long> projectIds = userAuth.getProjectIds();
        List<Map<String, Object>> result = userMapper.getProjectUserNew(menuId, buttonId, projectIds);
        Boolean set = redisService.set(key, result, RedisKey.ONE_TIME);
        return result;
    }

    /**
     * 所有项目用户
     *
     * @param projectIds 用户id
     * @return {@link List}
     */
    private List<Map<String, Object>> getAllUserByProjectMothod(HashSet<Long> projectIds) {
        String key = RedisKey.PROJECT_USER_ALL;
        Map<Object, Object> hmget = redisService.hmget(key);
        List<Map<String, Object>> result = new ArrayList<>(2);
        Map<String, Object> result2 = new HashMap<>(2);
        if (hmget == null || hmget.isEmpty()) {
            List<Project> projects = projectMapper.selectList(
                    new LambdaQueryWrapper<Project>().select(Project::getProjectId).eq(Project::getIsDelete, 0));
            Set<Long> finalProjectIds = projects.stream().map(Project::getProjectId).collect(Collectors.toSet());
            List<Map<String, Object>> projectUser = userMapper.getProjectUser();
            finalProjectIds.forEach(projectId -> {
                if (projectId != null && projectId != -1) {
                    Map<String, Object> result1 = new HashMap<>(3);
                    result1.put("projectId", projectId);
                    List<Map<String, Object>> user = new ArrayList<>();
                    projectUser.forEach(projectUser1 -> {
                        Long projectId1 = (Long) projectUser1.get("PROJECT_ID");
                        if (projectId1 != null && projectUser1.get("PROJECT_ID").equals(projectId)) {
                            result1.put("projectName", projectUser1.get("PROJECT_NAME"));
                            user.add(projectUser1);
                        }
                    });
                    result1.put("users", user);
                    result.add(result1);
                    result2.put(projectId.toString(), result1);
                }
            });
            redisService.hmset(key, result2, 2 * 10 * 60L);
        } else {
            List<Project> projects = projectMapper.selectList(
                    new LambdaQueryWrapper<Project>().select(Project::getProjectId).eq(Project::getIsDelete, 0));
            Set<Long> finalProjectIds = new HashSet<>();
            projects.forEach(project -> finalProjectIds.add(project.getProjectId()));
            projectIds.addAll(finalProjectIds);
            Set<Object> objects = hmget.keySet();
            projectIds.forEach(project -> {
                if (!objects.contains(project.toString())) {
                    Map<String, Object> result1 = getAllUserByProject(project);
                    result.add(result1);
                    result2.put(project.toString(), result1);
                }
            });
            hmget.forEach((k, v) -> {
                Map<String, Object> result1 = (Map<String, Object>) v;
                result.add(result1);
                result2.put(k.toString(), result1);
            });
            redisService.hmset(key, result2, 60 * RedisKey.ONE_TIME);
        }
        return result;
    }

    private Map<String, Object> getAllUserByProject(Long projectId) {
        if (projectId == null) {
            return Collections.emptyMap();
        }
        String key = RedisKey.PROJECT_USER_ALL;
        String item = projectId.toString();
        if (Boolean.TRUE.equals(redisService.hHasKey(key, item))) {
            return (Map<String, Object>) redisService.hget(key, item);
        }
        Map<String, Object> result = new HashMap<>(2);
        List<Map<String, Object>> projectUser = userMapper.getProjectUserByProjectId(projectId);
        result.put("projectId", projectId);
        if (projectUser != null && !projectUser.isEmpty()) {
            result.put("projectName", projectUser.get(0).get("PROJECT_NAME"));
        } else {
            Project project = projectMapper.selectOne(new LambdaQueryWrapper<Project>().select(Project::getProjectId)
                    .eq(Project::getIsDelete, 0).eq(Project::getProjectId, projectId));
            result.put("projectName", project == null ? null : project.getProjectName());
        }
        result.put("users", projectUser);
        redisService.hset(key, item, result, 60 * RedisKey.ONE_TIME);
        return result;
    }

    /**
     * 更新项目用户
     *
     * @param projectId 项目id
     */
    @Override
    public void upProjectUser(Long projectId) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("-1", Collections.emptyMap());
        redisService.hmset(RedisKey.PROJECT_USER_ALL, map, 1L);
    }

    /**
     * 更新项目用户
     */
    @Override
    public void upProjectUser() {
        Map<String, Object> map = new HashMap<>(2);
        map.put("-1", Collections.emptyMap());
        redisService.hmset(RedisKey.PROJECT_USER_ALL, map, 1L);
    }

    @Override
    public List<Map<String, Object>> getAppMenuAgencyNum(Long userId) {
        List<Map<String, Object>> resultMaps = new ArrayList<>();
        AuthUserModel userAuth = getUserAuth(userId);
        boolean admin = false;
        if (AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) {
            admin = true;
        }
        List<MenuUserAuthDto.MenuButtonDto> auths = userAuth.getAuths();
        getInfoCheck(!admin, userId, auths, resultMaps);
        getInfoAuthCheck(!admin, userId, auths, resultMaps);
        return resultMaps;
    }

    private void getInfoAuthCheck(boolean isNotAdmin, Long userId, List<MenuUserAuthDto.MenuButtonDto> auths,
                                  List<Map<String, Object>> resultMaps) {
        // 权限问题
        AtomicBoolean workApproval = new AtomicBoolean(false);
        AtomicBoolean leaveApproval = new AtomicBoolean(false);
        AtomicBoolean managementMeetings = new AtomicBoolean(false);
        auths.parallelStream().forEach(perms -> {
            if (perms.getMenuId() != null) {
                switch (Integer.parseInt(perms.getMenuId())) {
                    case MenuConstant.WORK_APPROVAL_ID:
                        workApproval.set(true);
                        break;
                    case MenuConstant.LEAVE_APPROVAL_ID:
                        leaveApproval.set(true);
                        break;
                    case MenuConstant.MANAGEMENT_MEETINGS_ID:
                        managementMeetings.set(true);
                        break;
                    default:
                        break;
                }
            }
        });
        // 请假申请审批表
        Integer askfleaveCount = (isNotAdmin && leaveApproval.get())
                ? askfleaveMapper.selectCount(new LambdaQueryWrapper<Askfleave>().select(Askfleave::getAskfleaveId)
                .eq(Askfleave::getAskfleaveCheckstate, 0).eq(Askfleave::getIsDelete, 0))
                : 0;
        HashMap<String, Object> resultMap4 = new HashMap<>(2);
        resultMap4.put("name", "请假审批");
        resultMap4.put("count", askfleaveCount);
        resultMaps.add(resultMap4);
        Integer reportingCount =
                (isNotAdmin && workApproval.get()) ? reportingMapper.selectCount(new LambdaQueryWrapper<Reporting>()
                        .select(Reporting::getReportingId).eq(Reporting::getReportingCheckuserid, userId)
                        .eq(Reporting::getReportingState, 0).eq(Reporting::getIsDelete, 0)) : 0;
        HashMap<String, Object> resultMap6 = new HashMap<>(2);
        resultMap6.put("name", "工作审批");
        resultMap6.put("count", reportingCount);
        resultMaps.add(resultMap6);
        // 会议
        Integer conferenceCount = (isNotAdmin && managementMeetings.get()) ? conferenceMapper.notStart(userId) : 0;
        HashMap<String, Object> resultMap7 = new HashMap<>(2);
        resultMap7.put("name", "会议管理");
        resultMap7.put("count", conferenceCount);
        resultMaps.add(resultMap7);
    }

    private void getInfoCheck(boolean isNotAdmin, Long userId, List<MenuUserAuthDto.MenuButtonDto> auths,
                              List<Map<String, Object>> resultMaps) {
        // 权限问题
        AtomicBoolean qualityplan = new AtomicBoolean(false);
        Set<Long> qualityplanProject = new HashSet<>(2);
        AtomicBoolean safeplan = new AtomicBoolean(false);
        Set<Long> safeplanProject = new HashSet<>(2);
        AtomicBoolean qualityproblem = new AtomicBoolean(false);
        Set<Long> qualityproblemProject = new HashSet<>(2);
        AtomicBoolean safeproblem = new AtomicBoolean(false);
        Set<Long> safeproblemProject = new HashSet<>(2);
        auths.parallelStream().forEach(perms -> {
            if (perms.getMenuId() != null) {
                switch (Integer.parseInt(perms.getMenuId())) {
                    case MenuConstant.INSPECTION_PLAN_ID:
                        qualityplan.set(true);
                        qualityplanProject.addAll(Arrays.stream(perms.getProjectIds().split(",")).map(Long::parseLong)
                                .collect(Collectors.toSet()));
                        break;
                    case MenuConstant.SINSPECTION_PLAN_ID:
                        safeplan.set(true);
                        safeplanProject.addAll(Arrays.stream(perms.getProjectIds().split(",")).map(Long::parseLong)
                                .collect(Collectors.toSet()));
                        break;
                    case MenuConstant.PROBLEM_LIST_ID:
                        qualityproblem.set(true);
                        qualityproblemProject.addAll(Arrays.stream(perms.getProjectIds().split(","))
                                .map(Long::parseLong).collect(Collectors.toSet()));
                        break;
                    case MenuConstant.DANGERS_LIST_ID:
                        safeproblem.set(true);
                        safeproblemProject.addAll(Arrays.stream(perms.getProjectIds().split(",")).map(Long::parseLong)
                                .collect(Collectors.toSet()));
                        break;
                    default:
                        break;
                }
            }
        });
        // 质量检查
        Integer qualityplanCount =
                (isNotAdmin && qualityplan.get()) ? qualityplanMapper.notChecked(userId, qualityplanProject) : 0;
        HashMap<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("name", "质量检查计划");
        resultMap.put("count", qualityplanCount);
        resultMaps.add(resultMap);
        // 安全检查
        Integer safeplanCount =
                (isNotAdmin && safeplan.get()) ? safeplanMapper.notChecked(userId, safeplanProject) : 0;
        HashMap<String, Object> resultMap1 = new HashMap<>(2);
        resultMap1.put("name", "安全检查计划");
        resultMap1.put("count", safeplanCount);
        resultMaps.add(resultMap1);
        // 质量问题
        Integer qualityproblemCount = (isNotAdmin && qualityproblem.get())
                ? qualityproblemMapper.getCountByUserIdAndProjectId(userId, qualityproblemProject) : 0;
        HashMap<String, Object> resultMap2 = new HashMap<>(2);
        resultMap2.put("name", "质量问题清单");
        resultMap2.put("count", qualityproblemCount);
        resultMaps.add(resultMap2);
        // 安全问题
        Integer safeproblemCount = (isNotAdmin && safeproblem.get())
                ? safeproblemMapper.getCountByUserIdAndProjectId(userId, safeproblemProject) : 0;
        HashMap<String, Object> resultMap3 = new HashMap<>(2);
        resultMap3.put("name", "安全隐患清单");
        resultMap3.put("count", safeproblemCount);
        resultMaps.add(resultMap3);
    }

    @Override
    public List<Map<String, Object>> findParcelUnits() {
        String key = RedisKey.SUPPLIER_ALL;
        if (Boolean.TRUE.equals(redisService.hasKey(key))) {
            return (List<Map<String, Object>>) redisService.get(key);
        }
        LambdaQueryWrapper<ParcelUnit> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ParcelUnit::getIsDelete, 0).select(ParcelUnit::getParcelUnitId, ParcelUnit::getParcelUnitName,
                ParcelUnit::getParcelUnitPerson, ParcelUnit::getParcelUnitPersonnumber);
        List<Map<String, Object>> maps = parcelUnitMapper.selectMaps(queryWrapper);
        redisService.set(key, maps, 30 * 24 * 60 * RedisKey.ONE_TIME);
        return maps;
    }

    @Override
    public void upFindParcelUnits() {
        try {
            redisService.del(RedisKey.SUPPLIER_ALL);
            findParcelUnits();
        } finally {
            redisService.del(RedisKey.SUPPLIER_ALL);
        }
    }

    /**
     * 获取项目文件分类
     */
    @Override
    public List<DocumentClass> getDocumentClass(String documentclassMenu) {
        return getDocumentClassByMenu(documentclassMenu, 0L);
    }

    /**
     * 获取模板文件分类
     */
    @Override
    public List<DocumentClass> getDocumentTempClass(String documentclassMenu) {
        return getDocumentClassByMenu(documentclassMenu, -1L);
    }

    private List<DocumentClass> getDocumentClassByMenu(String documentclassMenu, Long type) {
        List<DocumentClass> documentClasses1 = getDocumentClasses(type);
        List<DocumentClass> result = new ArrayList<>();
        for (DocumentClass documentClass : documentClasses1) {
            if (documentclassMenu.equals(documentClass.getDocumentclassMenu())) {
                result = documentClass.getDocumentclassChildren();
            }
        }
        return result;
    }

    private List<DocumentClass> getDocumentClasses(Long type) {
        AuthUserModel userAuth = getUserAuth(FebsUtil.getCurrentUserId());
        LambdaQueryWrapper<DocumentClass> wrapper = new LambdaQueryWrapper<DocumentClass>()
                .eq(DocumentClass::getIsDelete, 0).orderByAsc(DocumentClass::getDocumentclassId)
                .groupBy(DocumentClass::getDocumentclassMenu, DocumentClass::getDocumentclassId);
        if (AuthUserModel.KEY_NONE.equals(userAuth.getKey())) {
            return Collections.emptyList();
        }
        wrapper.eq(DocumentClass::getDocumentclassProjectid, type);
        List<DocumentClass> documentClasses = documentClassMapper.selectList(wrapper);
        return get(documentClasses);
    }

    private List<DocumentClass> get(List<DocumentClass> documentClasses) {
        documentClasses.forEach(documentClass -> {
            List<DocumentClass> collect = documentClasses.stream()
                    .filter(
                            documentClass1 -> documentClass.getDocumentclassId().equals(documentClass1.getDocumentclassPid()))
                    .collect(Collectors.toList());
            collect.forEach(documentClass11 -> {
                List<DocumentClass> collect1 = documentClasses.stream().filter(documentClass111 -> documentClass11
                        .getDocumentclassId().equals(documentClass111.getDocumentclassPid())).collect(Collectors.toList());
                documentClass11.setDocumentclassChildren(collect1);
            });
            documentClass.setDocumentclassChildren(collect);
        });
        documentClasses.removeIf(documentClass -> documentClass.getDocumentclassPid() != 0);
        return documentClasses;
    }

    /************* 权限开始 */

    /**
     * 获取用户的数据权限 角色类型
     */
    @Override
    public AuthUserModel getUserAuth(Long userId) {
        return (AuthUserModel) redisService.get(RedisKey.DATA_AUTH_PREFIX + userId);
    }

    /**
     * 获取具体的项目id
     *
     * @param menuId 菜单id
     * @return {@link AuthUserModel}
     */
    @Override
    public AuthUserModel getAuthUser(Integer menuId) {
        return getAuthUser(FebsUtil.getCurrentUserId(), menuId);
    }

    /**
     * 获取具体的项目id
     *
     * @param userId 用户id
     * @param menuId 菜单id
     * @return {@link AuthUserModel}
     */
    @Override
    public AuthUserModel getAuthUser(Long userId, Integer menuId) {
        return getAuthUser(userId, menuId, null);
    }

    /**
     * 获取具体的项目id
     *
     * @param menuId   菜单id
     * @param buttonId 按钮id
     * @return {@link AuthUserModel}
     */
    @Override
    public AuthUserModel getAuthUser(Integer menuId, Integer buttonId) {
        return getAuthUser(FebsUtil.getCurrentUserId(), menuId, buttonId);
    }

    /**
     * 获取具体的项目id
     *
     * @param userId   用户id 320
     * @param menuId   菜单id 36
     * @param buttonId 按钮id 108
     * @return {@link AuthUserModel}
     */
    @Override
    public AuthUserModel getAuthUser(Long userId, Integer menuId, Integer buttonId) {
        Set<Long> projectIds = new HashSet<>();
        projectIds.add(-1L);
        AuthUserModel userAuth = getUserAuth(userId);
        if (AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) {
            return userAuth;
        }
        MenuUserAuthDto.MenuButtonDto menuButtonDto =
                userAuth.getAuths().stream().filter(auth -> auth.getMenuId().equals(menuId.toString())).findFirst()
                        .orElse(new MenuUserAuthDto.MenuButtonDto());
        boolean hasMenuAuth = menuButtonDto.getMenuId() == null;
        if (hasMenuAuth) {
            throw new IllegalArgumentException("权限不足");
        }
        if (buttonId != null) {
            MenuUserAuthDto.ButtonDto buttonDto = menuButtonDto.getButtons().parallelStream()
                    .filter(buttonDto1 -> buttonDto1.getButtonId().equals(buttonId.toString())).findFirst()
                    .orElse(new MenuUserAuthDto.ButtonDto());
            boolean hasButtonAuth = buttonDto.getButtonId() == null;
            if (hasButtonAuth) {
                throw new IllegalArgumentException("权限不足");
            }
            projectIds.addAll(
                    Arrays.stream(buttonDto.getProjectIds().split(",")).map(Long::valueOf).collect(Collectors.toSet()));
        } else {
            projectIds.addAll(
                    Arrays.stream(menuButtonDto.getProjectIds().split(",")).map(Long::valueOf).collect(Collectors.toSet()));
        }
        ArrayList<MenuUserAuthDto.MenuButtonDto> menuButtonDtos = new ArrayList<>();
        menuButtonDtos.add(menuButtonDto);
        return new AuthUserModel().setUserId(userId).setKey(userAuth.getKey()).setProjectIds(projectIds)
                .setAuths(menuButtonDtos);
    }

    /**
     * 获取用户的数据权限 角色类型 projectId menuId buttonId
     */
    public AuthUserModel getUserAuth() {
        Long userId = FebsUtil.getCurrentUserId();
        return getUserAuth(userId);
    }

    /**
     * 获取用户的数据权限 角色类型
     */
    @Override
    public void upUserAuth(Long userId) {
        String key = RedisKey.DATA_AUTH_PREFIX + userId;
        redisService.del(key);
    }

    /**
     * 用户拥有的权限
     *
     * @param userId 用户id
     * @return {@link UserFun}
     */
    @Override
    public List<MenuUserAuthDto.MenuButtonDto> getAuthority(Long userId) {
        AuthUserModel userAuth = getUserAuth(userId);
        return userAuth.getAuths();
    }

    /**
     * 用户有该权限
     *
     * @param createUserId 用户id
     * @param menuId       {@link MenuConstant}
     * @param projectId    项目id 可不传
     * @return {@link boolean}
     */
    @Override
    public void hasPermission(Long createUserId, Integer menuId, Long projectId) {
        hasPermission(createUserId, menuId, null, projectId);
    }

    /**
     * 用户有该权限
     *
     * @param createUserId 用户id
     * @param menuId       菜单id
     * @param buttonId     按钮id
     * @param projectId    项目id null 非项目
     * @return {@link boolean}
     */
    @Override
    public void hasPermission(Long createUserId, Integer menuId, Integer buttonId, Long projectId) {
        HashSet<Integer> integers = new HashSet<>();
        integers.add(menuId);
        if (!hasMbpPermission(createUserId, integers, buttonId, projectId)) {
            throw new IllegalArgumentException("权限不足");
        }
    }

    /**
     * 用户有该权限
     *
     * @param createUserId 用户id
     * @param menuId       菜单id string[]
     * @return {@link boolean}
     */
    @Override
    public void hasPermission(Long createUserId, Integer... menuId) {
        if (!hasMbpPermission(createUserId,
                menuId == null ? Collections.emptySet() : Arrays.stream(menuId).collect(Collectors.toSet()), null, null)) {
            throw new IllegalArgumentException("权限不足");
        }
    }

    /**
     * 用户有该权限
     *
     * @param createUserId 用户id 99
     * @param menuId       菜单id 62
     * @param buttonId     按钮id 194
     * @param projectId    项目id null 非项目 138
     * @return {@link boolean}
     */
    private boolean hasMbpPermission(Long createUserId, Set<Integer> menuId, Integer buttonId, Long projectId) {
        Long userId = FebsUtil.getCurrentUserId();
        AuthUserModel userAuth = getUserAuth(userId);
        return (AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) || (isHasPermission(menuId, buttonId, projectId, userAuth.getAuths())
                || (createUserId == null || createUserId.equals(userId)));
    }

    private boolean isHasPermission(Set<Integer> menuId, Integer buttonId, Long projectId,
                                    List<MenuUserAuthDto.MenuButtonDto> auths) {
        MenuUserAuthDto.MenuButtonDto menuButtonDto =
                auths.stream().filter(auth -> menuId.contains(Integer.parseInt(auth.getMenuId()))).findFirst()
                        .orElse(new MenuUserAuthDto.MenuButtonDto());
        return menuId.isEmpty()
                || (menuButtonDto.getMenuId() != null && hasButtonPermission(buttonId, projectId, menuButtonDto));
    }

    /**
     * 是否拥有按钮权限
     *
     * @param buttonId      按钮id
     * @param projectId     项目id
     * @param menuButtonDto 实体
     * @return {@link boolean}
     */
    private boolean hasButtonPermission(Integer buttonId, Long projectId, MenuUserAuthDto.MenuButtonDto menuButtonDto) {
        boolean hasPermission = true;
        if (buttonId != null) {
            MenuUserAuthDto.ButtonDto buttonDto = menuButtonDto.getButtons().parallelStream()
                    .filter(buttonDto1 -> buttonDto1.getButtonId().equals(buttonId.toString())).findFirst()
                    .orElse(new MenuUserAuthDto.ButtonDto());
            hasPermission = buttonDto.getButtonId() != null && (projectId == null
                    || Arrays.stream(buttonDto.getProjectIds().split(",")).anyMatch(s -> s.equals(projectId.toString())));
        }
        return hasPermission;
    }

    /************* 权限结束 */

}
