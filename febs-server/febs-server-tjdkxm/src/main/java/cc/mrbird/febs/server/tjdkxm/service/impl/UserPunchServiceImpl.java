package cc.mrbird.febs.server.tjdkxm.service.impl;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.constant.ButtonConstant;
import cc.mrbird.febs.common.core.entity.constant.MenuConstant;
import cc.mrbird.febs.common.core.entity.constant.RedisKey;
import cc.mrbird.febs.common.core.entity.system.MenuUserAuthDto;
import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
import cc.mrbird.febs.common.core.entity.tjdk.Dept;
import cc.mrbird.febs.common.core.entity.tjdkxm.PunchArea;
import cc.mrbird.febs.common.core.entity.tjdkxm.PunchAreaClocktime;
import cc.mrbird.febs.common.core.entity.tjdkxm.UserProject;
import cc.mrbird.febs.common.core.entity.tjdkxm.UserPunch;
import cc.mrbird.febs.server.tjdkxm.service.*;
import cc.mrbird.febs.server.tjdkxm.util.dto.Count;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.nacos.client.config.utils.IOUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.system.SystemUser;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.DateUtil;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.common.redis.service.RedisService;
import cc.mrbird.febs.server.tjdkxm.config.LogRecordContext;
import cc.mrbird.febs.server.tjdkxm.mapper.UserPunchMapper;
import cc.mrbird.febs.server.tjdkxm.util.Holidays;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletResponse;

/**
 * @author ZNKJ-R
 * @version V1.0
 * @date 2022/1/18 20:35
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserPunchServiceImpl extends ServiceImpl<UserPunchMapper, UserPunch> implements UserPunchService {
    @Autowired
    private UserPunchMapper userFunMapper;
    @Autowired
    private CacheableService cacheableService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private LogRecordContext logRecordContext;
    @Autowired
    private SystemService systemService;
    @Autowired
    private IUserService userService;
    @Autowired
    DeptServiceImpl deptService;
    @Autowired
    private UserProjectService userProjectService;

    private static final String TIME_FORMAT = "^\\d{4}-\\d{2}-\\d{2}$";

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateUtil.FULL_TIME_1);

    @Override
    public IPage<UserPunch> findUserPunch(QueryRequest request, UserPunch.Params userPunch, boolean app)
            throws FebsException {
        QueryWrapper<UserPunch> queryWrapper = new QueryWrapper<>();

        // 项目id

        if (userPunch.getPunchProjectid() != null) {
            // 查询当前项目下有哪些用户 并装入到userIds中
            LambdaQueryWrapper<UserProject> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(UserProject::getProjectId, userPunch.getPunchProjectid());
            queryWrapper1.eq(UserProject::getIsDelete, 0);
            List<UserProject> userProjects = userProjectService.list(queryWrapper1);
            List<Long> userIds = userProjects.stream().map(UserProject::getUserId).collect(Collectors.toList());
            if (userIds.size() <= 0) {
                IPage<UserPunch> page = new Page<>(request.getPageNum(), request.getPageSize());
                return page;
            } else {
                queryWrapper.in("PUNCH_USERID", userIds);
            }
        }

        // 分部id
        if (StringUtils.isNotBlank(userPunch.getPunchUserName())) {
            queryWrapper.inSql("PUNCH_USERID", "select USER_ID from t_user where `STATUS`!=2 "
                    + "and `LEVEL` <> 1 and REALNAME like CONCAT('%','" + userPunch.getPunchUserName() + "'," + "'%')");
        }
        if (app) {
            queryWrapper.eq("PUNCH_USERID", FebsUtil.getCurrentUserId());
        } else {
            AuthUserModel userAuth = cacheableService.getAuthUser(MenuConstant.USERPUNCH_ID);
            if (!AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) {
                List<MenuUserAuthDto.MenuButtonDto> auths = userAuth.getAuths();
                boolean noneMatch =
                        auths.parallelStream().anyMatch(auth -> auth.getButtons().parallelStream().noneMatch(
                                buttonDto -> buttonDto.getButtonId().equals(Integer.toString(ButtonConstant.BUTTON_217_ID))));
                if (noneMatch) {
                    queryWrapper.eq("PUNCH_USERID", userAuth.getUserId());
                }
            }
        }
        if (userPunch.getPunchPunchtype() != null) {
            queryWrapper.eq("PUNCH_PUNCHTYEPE", userPunch.getPunchPunchtype());
        }
        String punchTime = "PUNCH_TIME";
        if (StringUtils.isNotBlank(userPunch.getPunchStartTime())) {
            queryWrapper.ge(punchTime, userPunch.getPunchStartTime());
        }
        if (StringUtils.isNotBlank(userPunch.getPunchEndTime())) {
            queryWrapper.le(punchTime, userPunch.getPunchEndTime());
        }
        if (userPunch.getPunchTime() != null) {
            queryWrapper.ge(punchTime, DateUtil.getMonthStartTime(userPunch.getPunchTime())).le(punchTime,
                    DateUtil.getMonthEndTime(userPunch.getPunchTime()));
        }
        queryWrapper.orderByDesc(punchTime);
        return this.userFunMapper.selectPageInfo2(new Page<>(request.getPageNum(), request.getPageSize()),
                queryWrapper);
    }

    @Override
    public FebsResponse findUserPunch1(QueryRequest request, UserPunch.Params userPunch, boolean app)
            throws FebsException {
        QueryWrapper<UserPunch> queryWrapper = new QueryWrapper<>();
        // 分部id
        if (StringUtils.isNotBlank(userPunch.getPunchUserName())) {
            queryWrapper.inSql("PUNCH_USERID", "select USER_ID from t_user where `STATUS`!=2 "
                    + "and `LEVEL` <> 1 and REALNAME like CONCAT('%','" + userPunch.getPunchUserName() + "'," + "'%')");
        }
        if (app) {
            queryWrapper.eq("PUNCH_USERID", FebsUtil.getCurrentUserId());
        } else {
            AuthUserModel userAuth = cacheableService.getAuthUser(MenuConstant.USERPUNCH_ID);
            if (!AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) {
                List<MenuUserAuthDto.MenuButtonDto> auths = userAuth.getAuths();
                boolean noneMatch =
                        auths.parallelStream().anyMatch(auth -> auth.getButtons().parallelStream().noneMatch(
                                buttonDto -> buttonDto.getButtonId().equals(Integer.toString(ButtonConstant.BUTTON_217_ID))));
                if (noneMatch) {
                    queryWrapper.eq("PUNCH_USERID", userAuth.getUserId());
                }
            }
        }
        if (userPunch.getPunchPunchtype() != null) {
            queryWrapper.eq("PUNCH_PUNCHTYEPE", userPunch.getPunchPunchtype());
        }
        String punchTime = "PUNCH_TIME";
        if (StringUtils.isNotBlank(userPunch.getPunchStartTime())) {
            queryWrapper.ge(punchTime, userPunch.getPunchStartTime());
        }
        if (StringUtils.isNotBlank(userPunch.getPunchEndTime())) {
            queryWrapper.le(punchTime, userPunch.getPunchEndTime());
        }
        if (userPunch.getPunchTime() != null) {
            queryWrapper.ge(punchTime, DateUtil.getMonthStartTime(userPunch.getPunchTime())).le(punchTime,
                    DateUtil.getMonthEndTime(userPunch.getPunchTime()));
        }
        queryWrapper.orderByDesc(punchTime);

        List<Integer> userPunches = this.userFunMapper.selectPageInfo1(queryWrapper);
        return new FebsResponse().data(userPunches);
    }

    @Override
    public UserPunch.AreaByDay arePunchAreaByDay() {
        Long userId = FebsUtil.getCurrentUserId();
        String key = String.format(RedisKey.USER_PUNCH_DAY, DateUtil.getNowDate(), userId);
        if (Boolean.TRUE.equals(redisService.hasKey(key))) {
            return (UserPunch.AreaByDay) redisService.get(key);
        }
        UserPunch.AreaByDay areaByDay = new UserPunch.AreaByDay();
        // 节假日&(周六||周天) 不能打卡
        // TODO: 2022/6/1 特殊日子 打卡
        boolean flag = Holidays.isHolidays(LocalDate.now().toString());
        if (flag) {
            redisService.set(key, true, 24 * 60 * RedisKey.ONE_TIME);
            areaByDay.setHoliday(1);
        } else {
            UserPunch userPunch = userFunMapper.selectCountByDay(userId);
            if (userPunch != null && userPunch.getPunchId() != null && userPunch.getPunchTime() != null) {
                Instant instant = userPunch.getPunchTime().toInstant();
                LocalDate punchTime = instant.atZone(ZoneId.systemDefault()).toLocalDate();
                flag = LocalDate.now().isAfter(punchTime);
                areaByDay.setIsPunch(flag ? 0 : 1);
            }
        }
        // 一天
        redisService.set(key, areaByDay, 24 * 60 * RedisKey.ONE_TIME);
        return areaByDay;
    }

    /**
     * 分页查询--未打卡
     *
     * @param request  分页
     * @param username 用户名、姓名
     * @param date     时间
     * @param type     类型 日 周 月
     * @return {@link IPage<?>}
     */
    @Override
    public IPage<Map<String, Object>> noUserPunch(QueryRequest request, String username, String date, String type) {
        String startDate = DateUtil.getBeginDate();
        String endDate = DateUtil.getEndDate();
        return userFunMapper.noUserPunch(new Page<>(request.getPageNum(), request.getPageSize()), username, startDate,
                endDate);
    }

    @Override
    public Map<String, Object> getPunchRecord(String type, String startTime, String endTime, boolean isAdmin)
            throws FebsException {
        Long userId = null;
        if (!isAdmin) {
            userId = FebsUtil.getCurrentUserId();
        }
        if (!startTime.matches(TIME_FORMAT) || !endTime.matches(TIME_FORMAT)) {
            throw new FebsException("数据错误");
        }
        // TODO: 2022/5/13 后面可能会和组织关联
        Map<String, Object> map = new HashMap<>(5);
        try {
            Set<SystemUser> users = userFunMapper.selectUserCount(userId);
            int userSize = users.size();
            // 时间列表
            List<String> timeList = new ArrayList<>(7);
            // 时间天数
            int count = getDayList(startTime, endTime, timeList);
            // 已打卡记录
            List<UserPunch> userPunches = userFunMapper.selectUserPunchByTime(userId, startTime, endTime);
            getPunch(map, users, timeList, count, userPunches, isAdmin);
            getByType(map, userPunches, isAdmin);
            // 总人数 num 应打次数 num
            map.put("num", isAdmin ? userSize : count);
        } catch (Exception e) {
            throw new FebsException("数据错误");
        }
        return map;
    }


    @Override
    public void getPunchRecord1(HttpServletResponse response, String type, String startTime, String endTime,
                                boolean isAdmin) throws FebsException, IOException, ParseException {

        // 查询所有用户信息
        LambdaQueryWrapper<SystemUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(SystemUser::getUserId, SystemUser::getUsername, SystemUser::getRealname,
                SystemUser::getDeptId, SystemUser::getPost);
        queryWrapper.eq(SystemUser::getStatus, 1);
        queryWrapper.ne(SystemUser::getLevel, 1);
        List<SystemUser> userlist = userService.list(queryWrapper);
        // 查询所有部门信息
        List<Long> userIds = new ArrayList<>();
        List<Long> deptIds = new ArrayList<>();
        userlist.forEach(user -> {
            userIds.add(user.getUserId());
            deptIds.add(user.getDeptId());

        });
        LambdaQueryWrapper<Dept> deptQueryWrapper = new LambdaQueryWrapper<>();
        deptQueryWrapper.select(Dept::getDeptId, Dept::getDeptName);
        deptQueryWrapper.in(Dept::getDeptId, deptIds);
        List<Dept> deptlist = deptService.list(deptQueryWrapper);
        for (SystemUser user : userlist) {
            for (Dept dept : deptlist) {
                if (user.getDeptId().equals(dept.getDeptId())) {
                    user.setDeptName(dept.getDeptName());
                    continue;
                } else {
                    user.setDeptName("");
                    continue;
                }
            }
        }
        // 查询所有考勤信息
        LambdaQueryWrapper<UserPunch> punchQueryWrapper = new LambdaQueryWrapper<>();
        punchQueryWrapper.in(UserPunch::getPunchUserid, userIds);
        punchQueryWrapper.between(UserPunch::getPunchTime, startTime + " 00:00:00", endTime + " 23:59:59");
        List<UserPunch> punchlist = this.list(punchQueryWrapper);
        // 处理考勤信息
        List<Count> countlist = new ArrayList<>();
        for (SystemUser user : userlist) {
            Count count = new Count();
            count.setUserName(user.getRealname());
            count.setDeptName(user.getDeptName());
            count.setPost(user.getPost());
            count.setPunchCount(0);
            for (UserPunch punch : punchlist) {
                if (user.getUserId().equals(punch.getPunchUserid())) {
                    int day = DateUtil.getDay(punch.getPunchTime());
                    switch (day) {
                        case 1:
                            if (count.getPunchResult1().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult1(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 2:
                            if (count.getPunchResult2().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult2(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 3:
                            if (count.getPunchResult3().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult3(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 4:
                            if (count.getPunchResult4().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult4(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 5:
                            if (count.getPunchResult4().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult5(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 6:
                            if (count.getPunchResult6().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult6(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 7:
                            if (count.getPunchResult7().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult7(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 8:
                            if (count.getPunchResult8().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult8(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 9:
                            if (count.getPunchResult9().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult9(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 10:
                            if (count.getPunchResult10().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult10(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 11:
                            if (count.getPunchResult11().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult11(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 12:
                            if (count.getPunchResult12().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult12(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 13:
                            if (count.getPunchResult13().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult13(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 14:
                            if (count.getPunchResult14().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult14(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 15:
                            if (count.getPunchResult15().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult15(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 16:
                            if (count.getPunchResult16().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult16(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 17:
                            if (count.getPunchResult17().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult17(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 18:
                            if (count.getPunchResult18().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult18(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 19:
                            if (count.getPunchResult19().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult19(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 20:
                            if (count.getPunchResult20().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult20(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 21:
                            if (count.getPunchResult21().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult21(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 22:
                            if (count.getPunchResult22().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult22(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 23:
                            if (count.getPunchResult23().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult23(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 24:
                            if (count.getPunchResult24().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult24(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 25:
                            if (count.getPunchResult25().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult25(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 26:
                            if (count.getPunchResult26().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult26(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 27:
                            if (count.getPunchResult27().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult27(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 28:
                            if (count.getPunchResult28().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult28(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 29:
                            if (count.getPunchResult29().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult29(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 30:
                            if (count.getPunchResult30().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult30(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 31:
                            if (count.getPunchResult31().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult31(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                    }
                }
            }
            int lc = DateUtil.getDaysOfMonth(startTime) - count.getPunchCount() - Holidays.dqyisHolidays(startTime);
            if (lc < 0) {
                count.setLatePunchCount(0);
            } else {
                count.setLatePunchCount(lc);
            }
            countlist.add(count);
        }
        if (countlist.size() > 0) {
            get(response, countlist, startTime, endTime);
        }
    }

    @Override
    public void getPunchRecord2(HttpServletResponse response, String type, String startTime, String endTime,
                                boolean isAdmin) throws IOException, ParseException {

        // 查询所有用户信息
        LambdaQueryWrapper<SystemUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(SystemUser::getUserId, SystemUser::getUsername, SystemUser::getRealname,
                SystemUser::getDeptId, SystemUser::getPost);
        queryWrapper.eq(SystemUser::getUserId, FebsUtil.getCurrentUser().getUserId());
        List<SystemUser> userlist = userService.list(queryWrapper);
        // 查询所有部门信息
        List<Long> userIds = new ArrayList<>();
        List<Long> deptIds = new ArrayList<>();
        userlist.forEach(user -> {
            userIds.add(user.getUserId());
            deptIds.add(user.getDeptId());

        });
        LambdaQueryWrapper<Dept> deptQueryWrapper = new LambdaQueryWrapper<>();
        deptQueryWrapper.select(Dept::getDeptId, Dept::getDeptName);
        deptQueryWrapper.in(Dept::getDeptId, deptIds);
        List<Dept> deptlist = deptService.list(deptQueryWrapper);
        for (SystemUser user : userlist) {
            for (Dept dept : deptlist) {
                if (user.getDeptId().equals(dept.getDeptId())) {
                    user.setDeptName(dept.getDeptName());
                    continue;
                } else {
                    user.setDeptName("");
                    continue;
                }
            }
        }
        // 查询所有考勤信息
        LambdaQueryWrapper<UserPunch> punchQueryWrapper = new LambdaQueryWrapper<>();
        punchQueryWrapper.in(UserPunch::getPunchUserid, userIds);
        punchQueryWrapper.between(UserPunch::getPunchTime, startTime + " 00:00:00", endTime + " 23:59:59");
        List<UserPunch> punchlist = this.list(punchQueryWrapper);
        // 处理考勤信息
        List<Count> countlist = new ArrayList<>();
        for (SystemUser user : userlist) {
            Count count = new Count();
            count.setUserName(user.getRealname());
            count.setDeptName(user.getDeptName());
            count.setPost(user.getPost());
            count.setPunchCount(0);
            for (UserPunch punch : punchlist) {
                if (user.getUserId().equals(punch.getPunchUserid())) {
                    int day = DateUtil.getDay(punch.getPunchTime());
                    switch (day) {
                        case 1:

                            if (count.getPunchResult1().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult1(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 2:
                            if (count.getPunchResult2().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult2(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 3:
                            if (count.getPunchResult3().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult3(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 4:
                            if (count.getPunchResult4().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult4(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 5:
                            if (count.getPunchResult4().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult5(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 6:
                            if (count.getPunchResult6().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult6(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 7:
                            if (count.getPunchResult7().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult7(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 8:
                            if (count.getPunchResult8().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult8(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 9:
                            if (count.getPunchResult9().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult9(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 10:
                            if (count.getPunchResult10().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult10(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 11:
                            if (count.getPunchResult11().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult11(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 12:
                            if (count.getPunchResult12().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult12(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 13:
                            if (count.getPunchResult13().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult13(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 14:
                            if (count.getPunchResult14().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult14(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 15:
                            if (count.getPunchResult15().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult15(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 16:
                            if (count.getPunchResult16().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult16(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 17:
                            if (count.getPunchResult17().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult17(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 18:
                            if (count.getPunchResult18().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult18(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 19:
                            if (count.getPunchResult19().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult19(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 20:
                            if (count.getPunchResult20().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult20(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 21:
                            if (count.getPunchResult21().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult21(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 22:
                            if (count.getPunchResult22().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult22(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 23:
                            if (count.getPunchResult23().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult23(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 24:
                            if (count.getPunchResult24().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult24(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 25:
                            if (count.getPunchResult25().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult25(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 26:
                            if (count.getPunchResult26().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult26(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 27:
                            if (count.getPunchResult27().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult27(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 28:
                            if (count.getPunchResult28().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult28(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 29:
                            if (count.getPunchResult29().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult29(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 30:
                            if (count.getPunchResult30().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult30(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                        case 31:
                            if (count.getPunchResult31().equals("")) {
                                count.setPunchCount(count.getPunchCount() + 1);
                            }
                            count.setPunchResult31(punch.getPunchPunchtyepe() == 0 ? "内勤" : "外勤");
                            break;
                    }
                }
            }
            int lc = DateUtil.getDaysOfMonth(startTime) - count.getPunchCount() - Holidays.dqyisHolidays(startTime);
            if (lc < 0) {
                count.setLatePunchCount(0);
            } else {
                count.setLatePunchCount(lc);
            }

            countlist.add(count);
        }
        if (countlist.size() > 0) {
            get(response, countlist, startTime, endTime);
        }
    }

    public void get(HttpServletResponse response, List<Count> data, String startTime, String endTime)
            throws IOException {
        OutputStream out = null;
        BufferedOutputStream bos = null;

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = startTime + "-" + endTime + ".xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        //Windows
        String templateFileName = "C:\\code\\tianjiudikuang\\muban\\monthPunch.xlsx";
        //Linux
//        String templateFileName = "/mydata/template/monthPunch.xlsx";

        out = response.getOutputStream();
        bos = new BufferedOutputStream(out);
        ExcelWriter excelWriter = EasyExcel.write(bos).withTemplate(templateFileName).build();
        WriteSheet writeSheet = EasyExcel.writerSheet(0, "Punch").build();
        // 写入list之前的数据
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("createTime", DateUtil.getNowdateTimeToString());
        excelWriter.fill(map, writeSheet);
        // 直接写入数据
        excelWriter.fill(data, writeSheet);
        excelWriter.finish();
    }

    @Override
    public void download(HttpServletResponse response, String userPunchIds) throws FebsException, IOException {
        if (Strings.isEmpty(userPunchIds)) {
            throw new FebsException("请勾选需要导出的数据");
        }
        String[] split = userPunchIds.split(",");
        if (split.length <= 0) {
            throw new FebsException("请勾选需要导出的数据");
        }
        List<UserPunch> userPunches = userFunMapper.selectInfo(userPunchIds);
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = "userPunchs";
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".template");
        // 只有订单记录的代码
        EasyExcel.write(response.getOutputStream(), UserPunch.class).sheet("考勤记录").doWrite(userPunches);
    }

    private void getPunch(Map<String, Object> map, Set<SystemUser> users, List<String> timeList, int count,
                          List<UserPunch> userPunches, boolean isAdmin) {
        // 分组 去重 保证 每个用户每天 只有一次
        // TODO: 2022/5/16 后面可能会和打卡时间段关联
        Map<Long,
                ArrayList<UserPunch>> collect = userPunches.parallelStream()
                .collect(Collectors.groupingBy(UserPunch::getPunchUserid,
                        Collectors.collectingAndThen(
                                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(UserPunch::getDateStr))),
                                ArrayList<UserPunch>::new)));
        // 全勤
        List<Map<String, Object>> punchInfo = new ArrayList<>(2);
        AtomicInteger punchNum = new AtomicInteger(0);
        // 缺勤
        List<Map<String, Object>> notPunchInfo = new ArrayList<>(2);
        AtomicInteger notPunchNum = new AtomicInteger(0);
        collect.forEach((userId, userPunches1) -> {
            Map<String, Object> map1 = new HashMap<>(2);
            map1.put("userId", userId);
            map1.put("userName", users.parallelStream().filter(u -> u.getUserId().equals(userId)).findFirst()
                    .orElse(new SystemUser()).getRealname());
            users.removeIf(u -> u.getUserId().equals(userId));
            if (userPunches1 != null && userPunches1.size() >= count) {
                // 全勤 人数+1
                punchNum.getAndIncrement();
                map1.put("punchInfo", userPunches.parallelStream().filter(u -> u.getPunchUserid().equals(userId))
                        .collect(Collectors.toSet()));
                punchInfo.add(map1);
            } else {
                // 缺勤 人数+1
                notPunchNum.getAndIncrement();
                Set<String> userTimes = new HashSet<>(4);
                timeList.forEach(time1 -> {
                    boolean present = userPunches1 != null
                            && userPunches1.parallelStream().noneMatch(u -> u.getDateStr().equals(time1));
                    if (present) {
                        userTimes.add(time1);
                    }
                });
                map1.put("notPunchInfo", userTimes);
                notPunchInfo.add(map1);
            }
        });
        users.forEach(user -> {
            notPunchNum.getAndIncrement();
            Map<String, Object> map1 = new HashMap<>(2);
            map1.put("userId", user.getUserId());
            map1.put("userName", user.getRealname());
            map1.put("notPunchInfo", timeList);
            notPunchInfo.add(map1);
        });
        // 全勤人数 punchNum
        map.put("punchNum", punchNum.get());
        // 全勤详情 punchInfo
        map.put("punchInfo", punchInfo);
        // 缺勤人数 notPunchNum
        if (isAdmin) {
            map.put("notPunchNum", notPunchNum.get());
        } else {
            int size = 0;
            if (!notPunchInfo.isEmpty()) {
                Map<String, Object> map1 = notPunchInfo.get(0);
                if (map1 != null && !map1.isEmpty()) {
                    Collection<String> notPunchInfo1 = (Collection<String>) map1.get("notPunchInfo");
                    if (notPunchInfo1 != null && !notPunchInfo1.isEmpty()) {
                        size = notPunchInfo1.size();
                    }
                }
            }
            map.put("notPunchNum", size);
        }
        // 缺勤详情 notPunchInfo
        map.put("notPunchInfo", notPunchInfo);
    }

    private void getByType(Map<String, Object> map, List<UserPunch> userPunches, boolean isAdmin) {
        // 内勤
        List<Map<String, Object>> officeWorkInfo = new ArrayList<>(2);
        AtomicInteger officeWorkNum = new AtomicInteger(0);
        // 外勤
        List<Map<String, Object>> fieldWorkInfo = new ArrayList<>(2);
        AtomicInteger fieldWorkNum = new AtomicInteger(0);
        Map<String, List<UserPunch>> collectByType = userPunches.parallelStream().collect(
                Collectors.groupingBy(u -> u.getPunchPunchtyepe() + "_" + u.getPunchUserid() + "_" + u.getPunchUserName()));
        collectByType.forEach((typeAndUserId, types) -> {
            try {
                String[] s = typeAndUserId.split("_");
                Map<String, Object> map1 = new HashMap<>(2);
                map1.put("userId", s[1]);
                map1.put("userName", s[2]);
                if ("1".equals(s[0])) {
                    if (isAdmin) {
                        fieldWorkNum.getAndIncrement();
                    } else {
                        fieldWorkNum.set(types.size());
                    }
                    map1.put("fieldWorkInfo", types);
                    fieldWorkInfo.add(map1);
                } else {
                    if (isAdmin) {
                        officeWorkNum.getAndIncrement();
                    } else {
                        officeWorkNum.set(types.size());
                    }
                    map1.put("officeWorkInfo", types);
                    officeWorkInfo.add(map1);
                }
            } catch (Exception e) {
            }
        });
        // 内勤 数量
        map.put("officeWorkNum", officeWorkNum.get());
        // 内勤 详情
        map.put("officeWorkInfo", officeWorkInfo);
        // 外勤 数量
        map.put("fieldWorkNum", fieldWorkNum.get());
        // 外勤 详情
        map.put("fieldWorkInfo", fieldWorkInfo);
    }

    public int getDayList(String startDate, String endDate, List<String> res) {
        // 这里先对startDate
        LocalDate newStartDate = LocalDate.parse(startDate, dateTimeFormatter).minusDays(1);
        // 减一天，最后的结果才能包含startDate
        LocalDate newEndDate = LocalDate.parse(endDate, dateTimeFormatter);
        int count = 0;
        while (!newStartDate.equals(newEndDate)) {
            newStartDate = newStartDate.plusDays(1);
            String dateString = dateTimeFormatter.format(newStartDate);
            // 节假日||(周六||周日) 过滤
            // TODO: 2022/6/1 特殊日子过滤
            if (!Holidays.isHolidays(dateString)) {
                res.add(dateString);
                count++;
            }
        }
        return count;
    }

    /**
     * 是否在打卡区域,是否可以打卡
     *
     * @return {@link Boolean}
     */
    @Override
    public Boolean areInPunchArea(String latitude, String longitude) {
        PunchArea userPunches = userFunMapper.areInPunchArea(latitude, longitude, FebsUtil.getCurrentUserId());
        return userPunches != null && userPunches.getPunchAreaId() > 0;
    }

    @Override
    public List<PunchAreaClocktime> areInPunchAreaNew(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        return userFunMapper.arePunchAreaTime(userId, (now.getHour() * 60) + now.getMinute());
    }

    public List<PunchArea> hasPuncharea(Long userId) {
        return userFunMapper.hasPuncharea(userId);
    }

    public List<PunchAreaClocktime> userPunchareaInfo(List<PunchArea> punchAreas) {
        return userFunMapper.userPunchareaInfo(punchAreas);
    }


    /**
     * 打卡
     *
     * @return {@link UserPunch}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserPunch userPush(String latitude, String longitude, String punchAddr) throws FebsException {
        if (StringUtils.isBlank(latitude) || StringUtils.isBlank(longitude)) {
            throw new FebsException("数据异常");
        }
        UserPunch.AreaByDay areaByDay = arePunchAreaByDay();
        // if (areaByDay.getHoliday() == 1) {
        // throw new FebsException("节假日不能打卡！");
        // }
        // if (areaByDay.getIsPunch() == 1) {
        // throw new FebsException("近期已经打卡！");
        // }
        Long userId = FebsUtil.getCurrentUserId();
        try {
            List<PunchArea> punchAreas = hasPuncharea(userId);
            if (punchAreas == null || punchAreas.isEmpty()) {
                throw new FebsException("没有考勤区域，请联系管理员！");
            }

            List<PunchAreaClocktime> punchAreaClocktimes = areInPunchAreaNew(userId);
            System.out.println(latitude + "--" + longitude);
            PunchArea punchArea = userFunMapper.areInPunchArea(latitude, longitude, userId);
            System.out.println(punchArea);
            if (punchAreaClocktimes == null || punchAreaClocktimes.isEmpty()) {
                List<PunchAreaClocktime> punchAreaClocktimes1 = userPunchareaInfo(punchAreas);
                if (punchAreaClocktimes1 == null || punchAreaClocktimes1.isEmpty()) {
                    throw new FebsException("您所在的考勤区域尚未配置打卡规则(时间设置)，请联系管理员");
                }
                String errorMsg = "";
                for (PunchAreaClocktime punchAreaClocktime : punchAreaClocktimes1) {
                    errorMsg += punchAreaClocktime.getStartTime() + "-" + punchAreaClocktime.getEndTime() + " ";
                }
                throw new FebsException("未在打卡时间：" + errorMsg);
            }
            UserPunch userPunch = new UserPunch();
            userPunch.setPunchUserid(userId);
            userPunch.setPunchTime(new Date());
            AtomicReference<Long> chockTimeId = new AtomicReference<>(punchAreaClocktimes.get(0).getId());
            if (punchArea != null && punchArea.getPunchAreaId() != null) {
                punchAreaClocktimes.parallelStream()
                        .filter(punchAreaClocktime -> punchAreaClocktime.getTableId().equals(punchArea.getPunchAreaId()))
                        .limit(1).forEach(punchAreaClocktime -> {
                            chockTimeId.set(punchAreaClocktime.getId());
                        });
                // 内勤打卡
                userPunch.setPunchPunchareaid(punchArea.getPunchAreaId());
                userPunch.setPunchPunchtyepe(0);
            } else {
                // 外勤打卡
                userPunch.setPunchPunchtyepe(1);
            }
            userPunch.setPunchPunchareatimeid(chockTimeId.get());
            userPunch.setPunchAddr(punchAddr);
            areaByDay.setIsPunch(1);
            if (Boolean.FALSE
                    .equals(redisService.set(String.format(RedisKey.USER_PUNCH_DAY, DateUtil.getNowDate(), userId),
                            areaByDay, 10 * RedisKey.ONE_TIME))) {
                throw new FebsException("打卡失败,请稍后重新打卡");
            }
            userFunMapper.insert(userPunch);
            logRecordContext.putVariable("id", userPunch.getPunchId());
            return userPunch;
        } catch (FebsException e) {
            throw new FebsException(e);
        } catch (Exception e) {
            throw new FebsException(e);
        }
    }
}
