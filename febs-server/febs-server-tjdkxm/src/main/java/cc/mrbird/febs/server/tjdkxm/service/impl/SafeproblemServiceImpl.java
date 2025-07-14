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
import cc.mrbird.febs.common.core.utils.OrderUtils;
import cc.mrbird.febs.server.tjdkxm.config.LogRecordContext;
import cc.mrbird.febs.server.tjdkxm.mapper.DangerMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.SafeplanMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.SafeproblemMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.SafeproblemlogMapper;
import cc.mrbird.febs.server.tjdkxm.service.CacheableService;
import cc.mrbird.febs.server.tjdkxm.service.SafeproblemService;
import cc.mrbird.febs.server.tjdkxm.service.SafeproblemUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * SafeproblemService实现
 *
 * @author zlkj_cg
 * @date 2022-01-12 15:51:04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SafeproblemServiceImpl extends ServiceImpl<SafeproblemMapper, Safeproblem> implements SafeproblemService {
    @Autowired
    private SafeproblemMapper safeproblemMapper;
    @Autowired
    private SafeproblemlogMapper safeproblemlogMapper;
    @Autowired
    private CacheableService cacheableService;
    @Autowired
    private SafeplanMapper safeplanMapper;
    @Autowired
    private DangerMapper dangerMapper;
    @Autowired
    private LogRecordContext logRecordContext;
    @Autowired
    private SafeproblemUserService safeproblemUserService;

    @Override
    public MyPage<Safeproblem> findSafeproblems(QueryRequest request, Safeproblem.Params safeproblem, int type)
        throws FebsException {
        QueryWrapper<Safeproblem> queryWrapper = new QueryWrapper<>();
        Long userId = FebsUtil.getCurrentUserId();
        AuthUserModel userAuth = cacheableService.getAuthUser(MenuConstant.DANGERS_LIST_ID);
        getParamNoOrder(safeproblem, type, queryWrapper, userAuth);
        if (AuthUserModel.KEY_NONE.equals(userAuth.getKey())) {
            return new MyPage<>();
        }
        queryWrapper.orderByAsc("SAFEPROBLEN_STATE");
        queryWrapper.orderByDesc("SAFEPROBLEN_ID");
        // 查询条件
        if (StringUtils.isNotBlank(safeproblem.getType())) {
            queryWrapper.and(wrapper -> {
                wrapper.eq("SAFEPROBLEN_STATE", "2");
                if ("1".equals(safeproblem.getType())) {
                    wrapper.eq("SAFEPROBLEN_RECTIFYUSERID", userAuth.getUserId());
                }
            });
        }

        return getMyPage(request, queryWrapper);
    }

    private void getParamNoOrder(Safeproblem.Params safeproblem, int type, QueryWrapper<Safeproblem> queryWrapper,
        AuthUserModel userAuth) {
        Set<Long> projectIds = userAuth.getProjectIds();
        if (!AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) {
            queryWrapper.in("SAFEPROBLEN_PROJECTID", projectIds);
        }
        queryWrapper.eq("IS_DELETE", 0);
        if (StringUtils.isNotBlank(safeproblem.getSafeproblenType())) {
            queryWrapper.eq("SAFEPROBLEN_TYPE", safeproblem.getSafeproblenType());
        }
        if (StringUtils.isNotBlank(safeproblem.getSafeproblenState())) {
            queryWrapper.eq("SAFEPROBLEN_STATE", safeproblem.getSafeproblenState());
        }
        if (StringUtils.isNotBlank(safeproblem.getSafeproblenLevel())) {
            queryWrapper.eq("SAFEPROBLEN_LEVEL", safeproblem.getSafeproblenLevel());
        }
        // 整改期限日期
        if (StringUtils.isNotEmpty(safeproblem.getStartRectifytime())) {
            queryWrapper.ge("SAFEPROBLEN_RECTIFYTIME", safeproblem.getStartRectifytime());
        }
        if (StringUtils.isNotEmpty(safeproblem.getEndRectifytime())) {
            queryWrapper.le("SAFEPROBLEN_RECTIFYTIME", safeproblem.getEndRectifytime());
        }
        // 实际整改日期
        if (StringUtils.isNotEmpty(safeproblem.getStartRectifyactime())) {
            queryWrapper.ge("SAFEPROBLEN_RECTIFYACTIME", safeproblem.getStartRectifyactime());
        }
        if (StringUtils.isNotEmpty(safeproblem.getEndRectifyactime())) {
            queryWrapper.le("SAFEPROBLEN_RECTIFYACTIME", safeproblem.getEndRectifyactime());
        }
        if (StringUtils.isNotBlank(safeproblem.getPscName())) {
            String pscName = safeproblem.getPscName();
            queryWrapper.and(wrapper -> {
                // wrapper.like("SAFEPROBLEN_CODE", pscName);
                wrapper.inSql("SAFEPROBLEN_PROJECTID",
                    "SELECT PROJECT_ID from p_project where IS_DELETE=0 and PROJECT_NAME like CONCAT('%','" + pscName
                        + "','%')");
                // //单位
                // wrapper.or().inSql("SAFEPROBLEN_UNITENGINEID",
                // "select UNIT_ID from p_unitengine where IS_DELETE=0 and UNIT_NAME like CONCAT('%','" + pscName +
                // "','%')");
                // //分部
                // wrapper.or().inSql("SAFEPROBLEN_PARCELID",
                // "SELECT PARCEL_ID from p_parcel where IS_DELETE=0 and PARCEL_NAME LIKE CONCAT('%','" + pscName +
                // "','%')");
                // //分项
                // wrapper.or().inSql("SAFEPROBLEN_SUBITEMID",
                // "SELECT SUBITEM_ID from p_subitem where IS_DELETE=0 and SUBITEM_NAME LIKE CONCAT('%','" + pscName +
                // "','%')");
            });
        }
    }

    /**
     * 查询整改记录分页
     *
     * @param request QueryRequest
     * @param safeproblem 安全隐患清单实体类
     * @param type
     * @return IPage<Safeproblem>
     */
    @Override
    public MyPage<Safeproblem> safeproblemRectList(QueryRequest request, Safeproblem.Params safeproblem, int type) {
        QueryWrapper<Safeproblem> queryWrapper = new QueryWrapper<>();
        AuthUserModel userAuth = cacheableService.getAuthUser(MenuConstant.SRECTIFICATION_RECORD_ID);
        if (AuthUserModel.KEY_NONE.equals(userAuth.getKey())) {
            return new MyPage<>();
        }
        getParam(request, safeproblem, type, queryWrapper, userAuth);
        return getMyPageSafeproblemRectList(request, queryWrapper, userAuth);
    }

    private MyPage<Safeproblem> getMyPageSafeproblemRectList(QueryRequest request,
        QueryWrapper<Safeproblem> queryWrapper, AuthUserModel userAuth) {
        return getMyPage(request, queryWrapper);
    }

    private MyPage<Safeproblem> getMyPage(QueryRequest request, QueryWrapper<Safeproblem> queryWrapper) {
        Integer integer = this.safeproblemMapper.selectCount(queryWrapper.select("1"));
        if (integer == null || integer == 0) {
            return new MyPage<>();
        }
        queryWrapper.groupBy("SAFEPROBLEN_ID");
        IPage<Safeproblem> page = this.safeproblemMapper
            .selectPageInfo(new Page<>(request.getPageNum(), request.getPageSize(), false), queryWrapper);
        page.setTotal(integer);
        List<Safeproblem> records = page.getRecords();
        if (records.size() > 0) {
            Set<Long> ids = records.stream().map(Safeproblem::getSafeproblenId).collect(Collectors.toSet());
            List<Safeproblemlog> safeproblemlogs = safeproblemlogMapper
                .selectList(new LambdaQueryWrapper<Safeproblemlog>().in(Safeproblemlog::getSafeproblenId, ids));
            records.forEach(record -> {
                List<Safeproblemlog> objects = new ArrayList<>();
                safeproblemlogs.forEach(s -> {
                    if (s.getSafeproblenId() != null && s.getSafeproblenId().equals(record.getSafeproblenId())) {
                        objects.add(s);
                    }
                });
                if (objects.size() > 0) {
                    record.setSafeproblemlogList(objects);
                }
            });
            page.setRecords(records);
        }
        return new MyPage<>(page);
    }

    private void getParam(QueryRequest request, Safeproblem.Params safeproblem, int type,
        QueryWrapper<Safeproblem> queryWrapper, AuthUserModel userAuth) {
        queryWrapper.orderByAsc("SAFEPROBLEN_STATE");
        queryWrapper.orderByDesc("SAFEPROBLEN_ID");
        // 排序
        OrderUtils.setQuseryOrder(queryWrapper, request);
        getParamNoOrder(safeproblem, type, queryWrapper, userAuth);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createSafeproblem(Safeproblem safeproblem) throws FebsException {
        FebsUtil.isProjectNotNull(safeproblem.getSafeproblenProjectid());
        String safeproblenCheckuserid = safeproblem.getSafeproblenCheckuserid();
        String[] split = null;
        if (!StringUtils.isEmpty(safeproblenCheckuserid)) {
            split = safeproblenCheckuserid.split(",");
            for (String s : split) {
                if (!StringUtils.isNumeric(s)) {
                    log.error(s + "不是数字");
                    throw new FebsException("新增失败");
                }
            }
        }
        safeproblem.setSafeproblenState("1");
        safeproblem.setSafeproblenCode(
            safeproblem.getSafeproblenProjectid() + DateUtil.getDateFormat(new Date(), DateUtil.FULL_TIME));
        safeproblem.setCreateUserid(FebsUtil.getCurrentUserId());
        safeproblem.setCreateTime(new Date());
        this.save(safeproblem);

        List<SafeproblemUser> safeproblemUsers = new ArrayList<>();
        if (split != null) {
            for (String s : split) {
                SafeproblemUser safeproblemUser = new SafeproblemUser();
                safeproblemUser.setTableId(safeproblem.getSafeproblenId());
                safeproblemUser.setUserId(Long.valueOf(s));
                safeproblemUsers.add(safeproblemUser);
            }
            safeproblemUserService.saveBatch(safeproblemUsers, safeproblemUsers.size());
        }
        Safeproblemlog safeproblemlog = new Safeproblemlog();
        safeproblemlog.setSafeproblenlogDo("1");
        safeproblemlog.setSafeproblenlogName(FebsUtil.getCurrentRealname());
        safeproblemlog.setSafeproblenId(safeproblem.getSafeproblenId());
        safeproblemlog.setSafeproblenlogAccepttime(new Date());
        safeproblemlogMapper.insert(safeproblemlog);
        logRecordContext.putVariable("id", safeproblem.getSafeproblenId());
        logRecordContext.putVariable("projectId", safeproblem.getSafeproblenProjectid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSafeproblem(Safeproblem safeproblem) throws FebsException {
        safeproblem.setSafeproblenProjectid(null);
        Safeproblem safeproblem1 = safeproblemMapper.selectById(safeproblem.getSafeproblenId());
        Assert.notNull(safeproblem1, "请选择数据");
        String state = safeproblem.getSafeproblenState();
        if (StringUtils.isNotBlank(state)) {
            if (!state.equals(safeproblem1.getSafeproblenState())) {
                Safeproblemlog safeproblemlog = new Safeproblemlog();
                safeproblemlog.setSafeproblenlogDo(safeproblem.getSafeproblenState());
                safeproblemlog.setSafeproblenlogName(FebsUtil.getCurrentRealname());
                safeproblemlog.setSafeproblenId(safeproblem.getSafeproblenId());
                safeproblemlog.setSafeproblenlogAccepttime(new Date());
                safeproblemlogMapper.insert(safeproblemlog);
            }
            if ("4".equals(state)) {
                safeproblem.setSafeproblenAcceptuserid(FebsUtil.getCurrentUserId());
                safeproblem.setSafeproblenAccepttime(new Date());
            }
            if ("5".equals(state)) {
                // 也可以参考下面这种写法
                safeproblemMapper.update(null,
                    Wrappers.<Safeproblem>lambdaUpdate().set(Safeproblem::getSafeproblenRectifyactime, null)
                        .set(Safeproblem::getSafeproblenRectifyuserid, null)
                        .set(Safeproblem::getSafeproblenRectifyimg, null)
                        .set(Safeproblem::getSafeproblenAcceptuserid, null)
                        .set(Safeproblem::getSafeproblenAccepttime, null).set(Safeproblem::getSafeproblenState, "2")
                        .eq(Safeproblem::getSafeproblenId, safeproblem.getSafeproblenId()));
                return;
            }
        }
        String safeproblenCheckuserid = safeproblem.getSafeproblenCheckuserid();
        if (!StringUtils.isEmpty(safeproblenCheckuserid)) {
            String[] split = safeproblenCheckuserid.split(",");
            for (String s : split) {
                if (!StringUtils.isNumeric(s)) {
                    log.error(s + "不是数字");
                    throw new FebsException("修改失败");
                }
            }
            safeproblemUserService.remove(new LambdaQueryWrapper<SafeproblemUser>().eq(SafeproblemUser::getTableId,
                safeproblem.getSafeproblenId()));
            List<SafeproblemUser> qualityproblemUsers = new ArrayList<>();
            for (String s : split) {
                SafeproblemUser qualityproblemUser = new SafeproblemUser();
                qualityproblemUser.setTableId(safeproblem.getSafeproblenId());
                qualityproblemUser.setUserId(Long.valueOf(s));
                qualityproblemUsers.add(qualityproblemUser);
            }
            safeproblemUserService.saveBatch(qualityproblemUsers, qualityproblemUsers.size());
        }
        this.updateById(safeproblem);
        logRecordContext.putVariable("projectId", safeproblem1.getSafeproblenProjectid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSafeproblem(Safeproblem safeproblem) throws FebsException {
        Safeproblem safeproblem1 = safeproblemMapper.selectById(safeproblem.getSafeproblenId());
        if (safeproblem1 == null) {
            throw new FebsException("数据错误");
        }
        cacheableService.hasPermission(safeproblem1.getCreateUserid(), MenuConstant.DANGERS_LIST_ID);
        this.update(new LambdaUpdateWrapper<Safeproblem>()
            .eq(Safeproblem::getSafeproblenId, safeproblem.getSafeproblenId()).set(Safeproblem::getIsDelete, 1));
        logRecordContext.putVariable("projectId", safeproblem1.getSafeproblenProjectid());

    }

    @Override
    public Map<String, Object> bigScreen(String type, String date) throws FebsException {
        // TODO: 2022/4/12 存在问题 负责人各个项目统计是否相同
        if (StringUtils.isBlank(type) || StringUtils.isBlank(date)) {
            throw new FebsException("参数不全");
        }
        ArrayList<String> data = getDate(date);
        Long userId = FebsUtil.getCurrentUserId();
        AuthUserModel userAuth = cacheableService.getAuthUser(MenuConstant.DANGER_CHART_ID);
        Map<String, Object> resultMap = new HashMap<>();
        if (AuthUserModel.KEY_NONE.equals(userAuth.getKey())) {
            return resultMap;
        }
        setScreenState(resultMap, data, userAuth, type);
        setScreenType(resultMap, data, userAuth, type);
        setScreenLevel(resultMap, data, userAuth, type);
        setScreenPlan(resultMap, data, userAuth, type);
        // 危险源数量
        setScreenDanger(resultMap, data, userAuth, type);
        return resultMap;
    }

    /**
     * 危险源数量
     */
    private void setScreenDanger(Map<String, Object> resultMap, ArrayList<String> date, AuthUserModel userAuth,
        String type) {
        LambdaQueryWrapper<Danger> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Danger::getDangerId).eq(Danger::getIsDelete, 0).ge(Danger::getDangerRecordtime, date.get(0))
            .le(Danger::getDangerRecordtime, date.get(1));
        if (!AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) {
            wrapper.in(Danger::getDangerProjectid, userAuth.getProjectIds());
        }
        if ("2".equals(type)) {
            wrapper.eq(Danger::getDangerRecorduserid, userAuth.getUserId());
        }
        Integer dangerNum = dangerMapper.selectCount(wrapper);
        resultMap.put("danger", dangerNum);
    }

    /**
     * 检查计划数量
     */
    private void setScreenPlan(Map<String, Object> resultMap, ArrayList<String> date, AuthUserModel userAuth,
        String type) {
        QueryWrapper<Safeplan> queryWrapper = new QueryWrapper<>();
        Set<Long> projectIds = userAuth.getProjectIds();
        Integer key = userAuth.getKey();
        Long userId = userAuth.getUserId();
        queryWrapper.select("SAFEPLAN_CHECKSTATE,count(1) as stateNum").eq("IS_DELETE", 0)
            .ge("SAFEPLAN_MAKERTIME", date.get(0)).le("SAFEPLAN_MAKERTIME", date.get(1)).groupBy("SAFEPLAN_CHECKSTATE")
            .having("SAFEPLAN_CHECKSTATE is not null and SAFEPLAN_CHECKSTATE!=''");
        if (!key.equals(AuthUserModel.KEY_ADMIN)) {
            queryWrapper.in("SAFEPLAN_PROJECTID", projectIds);
            if ("2".equals(type)) {
                queryWrapper.eq("SAFEPLAN_MAKERUSERID", userId);
            } else {
                userId = null;
            }
        } else {
            userId = null;
        }
        List<Map<String, Object>> maps = safeplanMapper.selectMaps(queryWrapper);
        // 0 代实施 1待验收【谁创建 谁验收】 2 验收通过 3 验收不通过
        // 计划检查次数
        String plan = "plan";
        Long planNum = 0L;
        // 待完成检查数
        String acceptance = "acceptance";
        Integer acceptanceNum = safeplanMapper.getAcceptanceCount(projectIds, userId, new QueryWrapper<Safeplan>()
            .eq("IS_DELETE", 0).ge("SAFEPLAN_MAKERTIME", date.get(0)).le("SAFEPLAN_MAKERTIME", date.get(1)));
        for (Map<String, Object> map : maps) {
            Object state = map.get("SAFEPLAN_CHECKSTATE");
            Long stateNum = (Long)map.get("stateNum");
            planNum += stateNum;
        }
        resultMap.put(acceptance, acceptanceNum);
        resultMap.put(plan, planNum);
    }

//    private ArrayList<String> getDate(String date) throws FebsException {
//        ArrayList<String> data = new ArrayList<>(2);
//        if (date.matches("^\\d{4}")) {
//            data.add(date);
//            int end = Integer.parseInt(date) + 1;
//            data.add("" + end);
//        } else if (date.matches("^\\d{4}-\\d{1,2}")) {
//            String[] split = date.split("\\-");
//            String endMoon = split[1];
//            int i1 = Integer.parseInt(endMoon);
//            if (i1 < 10) {
//                data.add(split[0] + "-0" + i1);
//            } else {
//                data.add(split[0] + "-" + i1);
//            }
//            int i = i1 + 1;
//            if (i < 10) {
//                data.add(split[0] + "-0" + i);
//            } else {
//                data.add(split[0] + "-" + i);
//            }
//        } else if (date.matches("^\\d{4}-\\d{1,2}-\\d{1,2}")) {
//            // 获取当前日期 的一周开始与结束
//            try {
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//                Date parse = format.parse(date);
//                Calendar calendar = Calendar.getInstance();
//                calendar.setFirstDayOfWeek(Calendar.MONDAY);
//                calendar.setTime(parse);
//                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//                setMinTimeOfDay(calendar);
//                data.add(DateUtil.getDateFormat(calendar.getTime()));
//
//                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
//                setMaxTimeOfDay(calendar);
//                data.add(DateUtil.getDateFormat(calendar.getTime()));
//            } catch (Exception e) {
//                throw new FebsException(e.getMessage());
//            }
//        } else {
//            throw new FebsException("日期格式错误");
//        }
//        return data;
//    }

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

    /**
     * 级别数量，级别统计
     */
    private void setScreenLevel(Map<String, Object> resultMap, ArrayList<String> date, AuthUserModel userAuth,
        String type) {

        QueryWrapper<Safeproblem> queryWrapper = new QueryWrapper<>();
        Set<Long> projectIds = userAuth.getProjectIds();
        Integer key = userAuth.getKey();
        Long userId = userAuth.getUserId();
        queryWrapper.select("SAFEPROBLEN_LEVEL, count(1) as levelNum").eq("IS_DELETE", 0).ge("CREATE_TIME", date.get(0))
            .le("CREATE_TIME", date.get(1)).groupBy("SAFEPROBLEN_LEVEL")
            .having("SAFEPROBLEN_LEVEL is not null and SAFEPROBLEN_LEVEL!=''");
        if (!key.equals(AuthUserModel.KEY_ADMIN)) {
            queryWrapper.in("SAFEPROBLEN_PROJECTID", projectIds);
            if ("2".equals(type)) {
                queryWrapper.eq("CREATE_USERID", userId);
            }
        }
        List<Map<String, Object>> mapLevels = safeproblemMapper.selectMaps(queryWrapper);
        resultMap.put("level", mapLevels);
    }

    /**
     * 类型数量，类型统计
     */
    private void setScreenType(Map<String, Object> resultMap, ArrayList<String> date, AuthUserModel userAuth,
        String type) {
        QueryWrapper<Safeproblem> queryWrapper = new QueryWrapper<>();
        Set<Long> projectIds = userAuth.getProjectIds();
        Integer key = userAuth.getKey();
        Long userId = userAuth.getUserId();
        queryWrapper.select("SAFEPROBLEN_TYPE,count(1) as typeNum").eq("IS_DELETE", 0).ge("CREATE_TIME", date.get(0))
            .le("CREATE_TIME", date.get(1)).groupBy("SAFEPROBLEN_TYPE")
            .having("SAFEPROBLEN_TYPE is not null and SAFEPROBLEN_TYPE!=''");
        if (!key.equals(AuthUserModel.KEY_ADMIN)) {
            queryWrapper.in("SAFEPROBLEN_PROJECTID", projectIds);
            if ("2".equals(type)) {
                queryWrapper.eq("CREATE_USERID", userId);
            }
        }
        List<Map<String, Object>> mapTypes = safeproblemMapper.selectMaps(queryWrapper);
        resultMap.put("typeNum", mapTypes.size());
        resultMap.put("type", mapTypes);
    }

    /**
     * 待整改，发现隐患，已巡检
     */
    private void setScreenState(Map<String, Object> resultMap, ArrayList<String> date, AuthUserModel userAuth,
        String type) {
        QueryWrapper<Safeproblem> queryWrapper = new QueryWrapper<>();
        // 待分配
        String distribute = "distribute";
        // 待整改
        String rectified = "rectified";
        // 安全隐患数量
        String discover = "discover";
        Long discoverNum = 0L;
        Set<Long> projectIds = userAuth.getProjectIds();
        Integer key = userAuth.getKey();
        Long userId = userAuth.getUserId();
        queryWrapper.select("SAFEPROBLEN_STATE,count(1) as stateNum").eq("IS_DELETE", 0).ge("CREATE_TIME", date.get(0))
            .le("CREATE_TIME", date.get(1)).groupBy("SAFEPROBLEN_STATE")
            .having("SAFEPROBLEN_STATE is not null and SAFEPROBLEN_STATE!=''");
        if (!key.equals(AuthUserModel.KEY_ADMIN)) {
            queryWrapper.in("SAFEPROBLEN_PROJECTID", projectIds);
            if ("2".equals(type)) {
                queryWrapper.eq("CREATE_USERID", userId);
            }
        }
        List<Map<String, Object>> mapStates = safeproblemMapper.selectMaps(queryWrapper);
        // 组装数据
        // 1待分配 2待整改 3待验收 4验收合格 5验收不通过
        for (Map<String, Object> mapState : mapStates) {
            if (mapState.containsKey("SAFEPROBLEN_STATE")) {
                Object state = mapState.get("SAFEPROBLEN_STATE");
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
}
