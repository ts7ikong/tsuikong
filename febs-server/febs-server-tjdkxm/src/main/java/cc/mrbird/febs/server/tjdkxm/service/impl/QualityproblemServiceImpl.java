package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.constant.ButtonConstant;
import cc.mrbird.febs.common.core.entity.constant.MenuConstant;
import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
import cc.mrbird.febs.common.core.entity.tjdkxm.*;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.DateUtil;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.config.LogRecordContext;
import cc.mrbird.febs.server.tjdkxm.mapper.QualityplanMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.QualityproblemMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.QualityproblemlogMapper;
import cc.mrbird.febs.server.tjdkxm.service.CacheableService;
import cc.mrbird.febs.server.tjdkxm.service.QualityproblemService;
import cc.mrbird.febs.server.tjdkxm.service.QualityproblemUserService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * QualityproblemService实现
 *
 * @author zlkj_cg
 * @date 2022-01-12 15:51:04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class QualityproblemServiceImpl extends ServiceImpl<QualityproblemMapper, Qualityproblem>
    implements QualityproblemService {
    @Autowired
    private QualityproblemMapper qualityproblemMapper;
    @Autowired
    private QualityproblemlogMapper qualityproblemlogMapper;
    @Autowired
    private QualityplanMapper qualityplanMapper;
    @Autowired
    private CacheableService cacheableService;
    @Autowired
    private LogRecordContext logRecordContext;
    @Autowired
    private QualityproblemUserService qualityproblemUserService;

    @Override
    public MyPage<Qualityproblem> findQualityproblems(QueryRequest request, Qualityproblem.Params qualityproblem)
        throws FebsException {
        QueryWrapper<Qualityproblem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("IS_DELETE", 0);
        AuthUserModel userAuth = cacheableService.getAuthUser(MenuConstant.PROBLEM_LIST_ID);
        getParamNoOrder(qualityproblem, queryWrapper, userAuth);
        queryWrapper.orderByAsc("QUALITYPROBLEN_STATE");
        queryWrapper.orderByDesc("QUALITYPROBLEN_ID");
        // 查询条件
        if (StringUtils.isNotBlank(qualityproblem.getType())) {
            queryWrapper.and(wrapper -> {
                wrapper.eq("QUALITYPROBLEN_STATE", "2");
                if ("1".equals(qualityproblem.getType())) {
                    wrapper.eq("QUALITYPROBLEN_RECTIFYUSERID", userAuth.getUserId());
                }
            });
        }
        return getMyPage(request, queryWrapper);
    }

    private MyPage<Qualityproblem> getMyPage(QueryRequest request, QueryWrapper<Qualityproblem> queryWrapper) {
        Integer integer = this.qualityproblemMapper.selectCount(queryWrapper.select("1"));
        if (integer == null || integer == 0) {
            return new MyPage<>();
        }
        queryWrapper.groupBy("QUALITYPROBLEN_ID");
        IPage<Qualityproblem> page = this.qualityproblemMapper
            .selectPageInfo(new Page<>(request.getPageNum(), request.getPageSize(), false), queryWrapper);
        page.setTotal(integer);
        List<Qualityproblem> records = page.getRecords();
        if (records.size() > 0) {
            Set<Long> ids = records.stream().map(Qualityproblem::getQualityproblenId).collect(Collectors.toSet());
            List<Qualityproblemlog> safeproblemlogs = qualityproblemlogMapper.selectList(
                new LambdaQueryWrapper<Qualityproblemlog>().in(Qualityproblemlog::getQualityproblenId, ids));
            records.forEach(record -> {
                List<Qualityproblemlog> objects = new ArrayList<>();
                safeproblemlogs.forEach(s -> {
                    if (s.getQualityproblenId() != null
                        && s.getQualityproblenId().equals(record.getQualityproblenId())) {
                        objects.add(s);
                    }
                });
                if (objects.size() > 0) {
                    record.setQualityProblemLogList(objects);
                }
            });
            page.setRecords(records);
        }
        return new MyPage<>(page);
    }

    /**
     * 查询（分页）整改记录
     *
     * @param request QueryRequest
     * @param qualityproblem 质量问题清单实体类
     * @return IPage<Qualityproblem>
     */
    @Override
    public MyPage<Qualityproblem> findQualityproblemRectList(QueryRequest request, Qualityproblem.Params qualityproblem)
        throws FebsException {
        QueryWrapper<Qualityproblem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("IS_DELETE", 0);
        AuthUserModel userAuth = cacheableService.getAuthUser(MenuConstant.RECTIFICATION_RECORD_ID);
        // 查询条件
        getParam(qualityproblem, queryWrapper, userAuth);
        return getMyPage(request, queryWrapper);
    }

    /**
     * 查询（分页）代整改记录
     *
     * @param request QueryRequest
     * @param qualityproblem 质量问题清单实体类
     * @return IPage<Qualityproblem>
     */
    @Override
    public MyPage<Qualityproblem> findQualityproblemRectifiedList(QueryRequest request,
        Qualityproblem.Params qualityproblem) {
        QueryWrapper<Qualityproblem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("IS_DELETE", 0);
        AuthUserModel userAuth = cacheableService.getAuthUser(MenuConstant.PROBLEM_LIST_ID);
        // 查询条件
        getParam(qualityproblem, queryWrapper, userAuth);
        return getMyPage(request, queryWrapper);
    }

    private void getParam(Qualityproblem.Params qualityproblem, QueryWrapper<Qualityproblem> queryWrapper,
        AuthUserModel userAuth) {
        queryWrapper.orderByAsc("QUALITYPROBLEN_STATE");
        queryWrapper.orderByDesc("QUALITYPROBLEN_ID");
        getParamNoOrder(qualityproblem, queryWrapper, userAuth);
    }

    private void getParamNoOrder(Qualityproblem.Params qualityproblem, QueryWrapper<Qualityproblem> queryWrapper,
        AuthUserModel userAuth) {
        if (!AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) {
            queryWrapper.in("QUALITYPROBLEN_PROJECTID", userAuth.getProjectIds());
        }
        if (StringUtils.isNotBlank(qualityproblem.getQualityproblenLevel())) {
            queryWrapper.eq("QUALITYPROBLEN_LEVEL", qualityproblem.getQualityproblenLevel());
        }
        if (StringUtils.isNotBlank(qualityproblem.getQualityproblenState())) {
            queryWrapper.eq("QUALITYPROBLEN_STATE", qualityproblem.getQualityproblenState());
        }
        if (StringUtils.isNotEmpty(qualityproblem.getStartChecktime())) {
            queryWrapper.ge("QUALITYPROBLEN_CHECKTIME", qualityproblem.getStartChecktime());
        }
        if (StringUtils.isNotEmpty(qualityproblem.getEndChecktime())) {
            queryWrapper.le("QUALITYPROBLEN_CHECKTIME", qualityproblem.getEndChecktime());
        }
        if (StringUtils.isNotEmpty(qualityproblem.getStartRectifytime())) {
            queryWrapper.ge("QUALITYPROBLEN_RECTIFYTIME", qualityproblem.getStartRectifytime());
        }
        if (StringUtils.isNotEmpty(qualityproblem.getEndRectifytime())) {
            queryWrapper.le("QUALITYPROBLEN_RECTIFYTIME", qualityproblem.getEndRectifytime());
        }
        if (StringUtils.isNotBlank(qualityproblem.getQualityproblenCheckusername())) {
            queryWrapper.and(wrapper -> {
                String s = qualityproblem.getQualityproblenCheckusername().replaceAll("\\,", "");
                wrapper.in("QUALITYPROBLEN_ID",
                    "select q.TABLE_ID from t_user t LEFT JOIN p_qualityproblem_user q on p.USER_ID=t.USER_ID "
                        + "where REALNAME LIKE CONCAT('%','" + s + "','%')");
                wrapper.or().like("QUALITYPROBLEN_CHECKFOREIGNUSER", s);
            });
        }
        if (StringUtils.isNotBlank(qualityproblem.getPscName())) {
            String pscName = qualityproblem.getPscName();
            queryWrapper.and(wrapper -> {
                // 项目
                wrapper.inSql("QUALITYPROBLEN_PROJECTID",
                    "SELECT PROJECT_ID from p_project where IS_DELETE=0 and PROJECT_NAME like CONCAT('%','" + pscName
                        + "','%')");
                // 单位
                // wrapper.inSql("QUALITYPROBLEN_UNITENGINEID",
                // "select UNIT_ID from p_unitengine where IS_DELETE=0 and UNIT_NAME like CONCAT('%','" + pscName +
                // "','%')");
                // //分部
                // wrapper.or().inSql("QUALITYPROBLEN_PARCELID",
                // "SELECT PARCEL_ID from p_parcel where IS_DELETE=0 and PARCEL_NAME LIKE CONCAT('%','" + pscName +
                // "','%')");
                // //分项
                // wrapper.or().inSql("QUALITYPROBLEN_SUBITEMID",
                // "SELECT SUBITEM_ID from p_subitem where IS_DELETE=0 and SUBITEM_NAME LIKE CONCAT('%','" + pscName +
                // "','%')");
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createQualityproblem(Qualityproblem qualityproblem) throws FebsException {
        FebsUtil.isProjectNotNull(qualityproblem.getQualityproblenProjectid());
        String qualityproblenCheckuserid = qualityproblem.getQualityproblenCheckuserid();
        String[] split = null;
        if (!StringUtils.isEmpty(qualityproblenCheckuserid)) {
            split = qualityproblenCheckuserid.split(",");
            for (String s : split) {
                if (!StringUtils.isNumeric(s)) {
                    log.error(s + "不是数字");
                    throw new FebsException("新增失败");
                }
            }
        }
        qualityproblem.setQualityproblenState("1");
        // if (qualityproblem.getQualityproblenChecktime().compareTo(new Date()) <= 0) {
        // throw new FebsException("计划检查日期应在当前日期之后");
        // }
        qualityproblem.setCreateUserid(FebsUtil.getCurrentUserId());
        qualityproblem.setCreateTime(new Date());
        this.save(qualityproblem);

        List<QualityproblemUser> qualityproblemUsers = new ArrayList<>();
        if (split != null) {
            for (String s : split) {
                QualityproblemUser qualityproblemUser = new QualityproblemUser();
                qualityproblemUser.setTableId(qualityproblem.getQualityproblenId());
                qualityproblemUser.setUserId(Long.valueOf(s));
                qualityproblemUsers.add(qualityproblemUser);
            }
            qualityproblemUserService.saveBatch(qualityproblemUsers, qualityproblemUsers.size());
        }
        Qualityproblemlog qualityproblemlog = new Qualityproblemlog();
        qualityproblemlog.setQualityproblenId(qualityproblem.getQualityproblenId());
        qualityproblemlog.setQualityproblenlogDo("1");
        qualityproblemlog.setQualityproblenlogName(FebsUtil.getCurrentRealname());
        qualityproblemlog.setQualityproblenlogAccepttime(new Date());
        qualityproblemlogMapper.insert(qualityproblemlog);
        logRecordContext.putVariable("id", qualityproblem.getQualityproblenId());
        logRecordContext.putVariable("projectId", qualityproblem.getQualityproblenProjectid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateQualityproblem(Qualityproblem qualityproblem) throws FebsException {
        Qualityproblem qualityproblem1 = qualityproblemMapper.selectById(qualityproblem.getQualityproblenId());
        String state = qualityproblem.getQualityproblenState();
        if (StringUtils.isNotBlank(state)) {
            if (!state.equals(qualityproblem1.getQualityproblenState())) {
                Qualityproblemlog qualityproblemlog = new Qualityproblemlog();
                qualityproblemlog.setQualityproblenId(qualityproblem.getQualityproblenId());
                qualityproblemlog.setQualityproblenlogDo(qualityproblem.getQualityproblenState());
                qualityproblemlog.setQualityproblenlogName(FebsUtil.getCurrentRealname());
                qualityproblemlog.setQualityproblenlogAccepttime(new Date());
                qualityproblemlogMapper.insert(qualityproblemlog);
            }
            if ("4".equals(state)) {
                qualityproblem.setQualityproblenAcceptuserid(FebsUtil.getCurrentUserId());
                qualityproblem.setQualityproblenAccepttime(new Date());
            }
            // if ("3".equals(state)) {
            // qualityproblem.setQualityproblenRectifyimg(JSONArray.parseObject(qualityproblem.getQualityproblenRectifyimg().toString()));
            // qualityproblem.setQualityproblenAccepttime(new Date());
            // }
            if ("5".equals(state)) {
                // 也可以参考下面这种写法
                qualityproblemMapper.update(null,
                    Wrappers.<Qualityproblem>lambdaUpdate().set(Qualityproblem::getQualityproblenRectifyimg, null)
                        .set(Qualityproblem::getQualityproblenRectifyactime, null)
                        .set(Qualityproblem::getQualityproblenRectifyuserid, null)
                        .set(Qualityproblem::getQualityproblenAccepttime, null)
                        .set(Qualityproblem::getQualityproblenAcceptuserid, null)
                        .set(Qualityproblem::getQualityproblenState, "2")
                        .eq(Qualityproblem::getQualityproblenId, qualityproblem.getQualityproblenId()));
                return;
            }
        }
        String qualityproblenCheckuserid = qualityproblem.getQualityproblenCheckuserid();
        if (!StringUtils.isEmpty(qualityproblenCheckuserid)) {
            String[] split = qualityproblenCheckuserid.split(",");
            for (String s : split) {
                if (!StringUtils.isNumeric(s)) {
                    log.error(s + "不是数字");
                    throw new FebsException("修改失败");
                }
            }
            qualityproblemUserService.remove(new LambdaQueryWrapper<QualityproblemUser>()
                .eq(QualityproblemUser::getTableId, qualityproblem.getQualityproblenId()));
            List<QualityproblemUser> qualityproblemUsers = new ArrayList<>();
            for (String s : split) {
                QualityproblemUser qualityproblemUser = new QualityproblemUser();
                qualityproblemUser.setTableId(qualityproblem.getQualityproblenId());
                qualityproblemUser.setUserId(Long.valueOf(s));
                qualityproblemUsers.add(qualityproblemUser);
            }
            qualityproblemUserService.saveBatch(qualityproblemUsers, qualityproblemUsers.size());
        }
        this.updateById(qualityproblem);
        logRecordContext.putVariable("projectId", qualityproblem1.getQualityproblenProjectid());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteQualityproblem(Qualityproblem qualityproblem) throws FebsException {
        Qualityproblem qualityproblem1 = qualityproblemMapper.selectById(qualityproblem.getQualityproblenId());
        if (qualityproblem1 == null) {
            throw new FebsException("选择记录");
        }
        cacheableService.hasPermission(qualityproblem1.getCreateUserid(), MenuConstant.PROBLEM_LIST_ID,
            ButtonConstant.BUTTON_164_ID, qualityproblem1.getQualityproblenProjectid());
        this.update(new LambdaUpdateWrapper<Qualityproblem>()
            .eq(Qualityproblem::getQualityproblenId, qualityproblem.getQualityproblenId())
            .set(Qualityproblem::getIsDelete, 1));
        logRecordContext.putVariable("projectId", qualityproblem1.getQualityproblenProjectid());
    }

    @Override
    public Map<String, Object> bigScreen(String type, String date) throws FebsException {
        if (StringUtils.isBlank(type) || StringUtils.isBlank(date)) {
            throw new FebsException("参数不全");
        }
        ArrayList<String> data = getDate(date);
        AuthUserModel userAuth = cacheableService.getAuthUser(MenuConstant.PROBLEM_CHART_ID);
        Map<String, Object> resultMap = new HashMap<>();
        if (AuthUserModel.KEY_NONE.equals(userAuth.getKey())) {
            return resultMap;
        }
        setScreenState(resultMap, data, userAuth, type);
        setScreenType(resultMap, data, userAuth, type);
        setScreenLevel(resultMap, data, userAuth, type);
        setScreenPlan(resultMap, data, userAuth, type);
        return resultMap;
    }

    /**
     * 计划
     */
    private void setScreenPlan(Map<String, Object> resultMap, ArrayList<String> data, AuthUserModel userAuth,
        String type) {
        QueryWrapper<Qualityplan> queryWrapper = new QueryWrapper<>();
        Set<Long> projectIds = userAuth.getProjectIds();
        Long userId = userAuth.getUserId();
        queryWrapper.select("QUALITYPLAN_CHECKSTATE,count(1) as stateNum").eq("IS_DELETE", 0)
            .ge("QUALITYPLAN_MAKERTIME", data.get(0)).le("QUALITYPLAN_MAKERTIME", data.get(1))
            .groupBy("QUALITYPLAN_CHECKSTATE")
            .having("QUALITYPLAN_CHECKSTATE is not null and QUALITYPLAN_CHECKSTATE!=''");
        if (!AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) {
            queryWrapper.in("QUALITYPLAN_PROJECTID", projectIds);
            if ("2".equals(type)) {
                queryWrapper.eq("QUALITYPLAN_MAKERUSERID", userId);
            }
        }

        List<Map<String, Object>> maps = qualityplanMapper.selectMaps(queryWrapper);
        // 0 代实施 1待验收【谁创建 谁验收】 2 验收通过 3 验收不通过
        // 计划检查次数
        String plan = "plan";
        Long planNum = 0L;
        // 待完成检查数
        String acceptance = "acceptance";
        if (!"2".equals(type)) {
            userId = null;
        }
        Integer acceptanceNum = qualityplanMapper.getAcceptanceCount(projectIds, userId, new QueryWrapper<Qualityplan>()
            .eq("IS_DELETE", 0).ge("QUALITYPLAN_MAKERTIME", data.get(0)).le("QUALITYPLAN_MAKERTIME", data.get(1)));
        for (Map<String, Object> map : maps) {
            Object state = map.get("QUALITYPLAN_CHECKSTATE");
            Long stateNum = (Long)map.get("stateNum");
            planNum += stateNum;
        }
        resultMap.put(acceptance, acceptanceNum);
        resultMap.put(plan, planNum);
    }

    private void setScreenLevel(Map<String, Object> resultMap, ArrayList<String> data, AuthUserModel userAuth,
        String type) {
        QueryWrapper<Qualityproblem> queryWrapper = new QueryWrapper<>();
        Set<Long> projectIds = userAuth.getProjectIds();
        Long userId = userAuth.getUserId();
        // 级别统计
        queryWrapper.select("QUALITYPROBLEN_LEVEL, count(1) as levelNum").eq("IS_DELETE", 0)
            .ge("CREATE_TIME", data.get(0)).le("CREATE_TIME", data.get(1)).groupBy("QUALITYPROBLEN_LEVEL")
            .having("QUALITYPROBLEN_LEVEL is not null and QUALITYPROBLEN_LEVEL!=''");
        if (!AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) {
            queryWrapper.in("QUALITYPROBLEN_PROJECTID", projectIds);
            if ("2".equals(type)) {
                queryWrapper.eq("CREATE_USERID", userId);
            }
        }
        List<Map<String, Object>> mapLevels = qualityproblemMapper.selectMaps(queryWrapper);
        resultMap.put("level", mapLevels);
    }

    private void setScreenType(Map<String, Object> resultMap, ArrayList<String> data, AuthUserModel userAuth,
        String type) {
        QueryWrapper<Qualityproblem> queryWrapper = new QueryWrapper<>();
        Set<Long> projectIds = userAuth.getProjectIds();
        Long userId = userAuth.getUserId();
        // 类型数量，类型统计
        queryWrapper.select("QUALITYPROBLEN_TYPE,count(1) as typeNum").eq("IS_DELETE", 0).ge("CREATE_TIME", data.get(0))
            .le("CREATE_TIME", data.get(1)).groupBy("QUALITYPROBLEN_TYPE")
            .having("QUALITYPROBLEN_TYPE is not null and QUALITYPROBLEN_TYPE!=''");
        if (!AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) {
            queryWrapper.in("QUALITYPROBLEN_PROJECTID", projectIds);
            if ("2".equals(type)) {
                queryWrapper.eq("CREATE_USERID", userId);
            }
        }
        List<Map<String, Object>> mapTypes = qualityproblemMapper.selectMaps(queryWrapper);
        resultMap.put("typeNum", mapTypes.size());
        resultMap.put("type", mapTypes);
    }

    private void setScreenState(Map<String, Object> resultMap, ArrayList<String> data, AuthUserModel userAuth,
        String type) {
        QueryWrapper<Qualityproblem> queryWrapper = new QueryWrapper<>();
        Set<Long> projectIds = userAuth.getProjectIds();
        Long userId = userAuth.getUserId();
        // 待整改，发现隐患，已巡检
        // 待分配
        String distribute = "distribute";
        // 待整改
        String rectified = "rectified";
        // 质量问题数量
        String discover = "discover";
        Long discoverNum = 0L;
        queryWrapper.select("QUALITYPROBLEN_STATE,count(1) as stateNum").eq("IS_DELETE", 0)
            .ge("CREATE_TIME", data.get(0)).le("CREATE_TIME", data.get(1)).groupBy("QUALITYPROBLEN_STATE")
            .having("QUALITYPROBLEN_STATE is not null and QUALITYPROBLEN_STATE!=''");
        if (!AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) {
            queryWrapper.in("QUALITYPROBLEN_PROJECTID", projectIds);
            if ("2".equals(type)) {
                queryWrapper.eq("CREATE_USERID", userId);
            }
        }
        List<Map<String, Object>> mapStates = qualityproblemMapper.selectMaps(queryWrapper);
        // 组装数据
        // 1待分配 2待整改 3待验收 4验收合格 5验收不通过
        for (Map<String, Object> mapState : mapStates) {
            if (mapState.containsKey("QUALITYPROBLEN_STATE")) {
                Object state = mapState.get("QUALITYPROBLEN_STATE");
                Long stateNum = (Long)mapState.get("stateNum");
                discoverNum += stateNum;
                if ("1".equals(state)) {
                    resultMap.put(distribute, stateNum);
                }
                if ("2".equals(state)) {
                    resultMap.put(rectified, stateNum);
                }
            }
        }
        resultMap.put(discover, discoverNum);
    }

    private ArrayList<String> getDate(String date) throws FebsException {
        ArrayList<String> data = new ArrayList<>(2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (date.matches("^\\d{4}")) {
            data.add(date + "-01-01 00:00:00");
            int end = Integer.parseInt(date) + 1;
            data.add(end + "-01-01 00:00:00");

        } else if (date.matches("^\\d{4}-\\d{1,2}")) {
            String[] split = date.split("\\-");
            String year = split[0];
            String month = split[1];
            int monthValue = Integer.parseInt(month);

            LocalDate firstDay = YearMonth.of(Integer.parseInt(year), monthValue).atDay(1);
            LocalDate lastDay = YearMonth.of(Integer.parseInt(year), monthValue).atEndOfMonth();

            String start = firstDay.atStartOfDay().format(formatter);
            String end = lastDay.atTime(23, 59, 59).format(formatter);

            data.add(start);
            data.add(end);

        } else if (date.matches("^\\d{4}-\\d{1,2}-\\d{1,2}")) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date parse = format.parse(date);
                Calendar calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                calendar.setTime(parse);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                setMinTimeOfDay(calendar);
                data.add(DateUtil.getDateFormat(calendar.getTime()));

                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                setMaxTimeOfDay(calendar);
                data.add(DateUtil.getDateFormat(calendar.getTime()));
            } catch (Exception e) {
                throw new FebsException(e.getMessage());
            }
        } else {
            throw new FebsException("日期格式错误");
        }

        return data;
    }

    private void setMinTimeOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private void setMaxTimeOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }
}
