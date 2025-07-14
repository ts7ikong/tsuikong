package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdkxm.PunchArea;
import cc.mrbird.febs.common.core.entity.tjdkxm.PunchAreaClocktime;
import cc.mrbird.febs.common.core.entity.tjdkxm.PunchAreaUser;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.mapper.PunchAreaMapper;
import cc.mrbird.febs.server.tjdkxm.service.PunchAreaClocktimeService;
import cc.mrbird.febs.server.tjdkxm.service.PunchAreaService;
import cc.mrbird.febs.server.tjdkxm.service.PunchAreaUserService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/9 17:25
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class PunchAreaServiceImpl extends ServiceImpl<PunchAreaMapper, PunchArea> implements PunchAreaService {
    private final PunchAreaMapper punchAreaMapper;
    private final PunchAreaClocktimeService punchAreaClocktimeService;
    private final PunchAreaUserService punchAreaUserService;

    /**
     * 查询（所有）
     *
     * @return List<PunchArea>
     */
    @Override
    public List<PunchArea> findPunchAreas() {
        return punchAreaMapper.selectList(new LambdaQueryWrapper<PunchArea>()
                .inSql(PunchArea::getPunchAreaId,
                        "select DISTINCT TABLE_ID from p_puncharea_user where USER_ID=" + FebsUtil.getCurrentUserId())
                .orderByDesc(PunchArea::getPunchAreaId).eq(PunchArea::getIsDelete, 0));
    }

    @Override
    public IPage<?> findPunchAreas(QueryRequest request, PunchArea punchArea) {
        LambdaQueryWrapper<PunchArea> eq = new LambdaQueryWrapper<PunchArea>().eq(PunchArea::getIsDelete, 0);
        if (StringUtils.isNotBlank(punchArea.getPunchAreaName())) {
            eq.like(PunchArea::getPunchAreaName, punchArea.getPunchAreaName());
        }
        eq.orderByDesc(PunchArea::getPunchAreaId);
        Integer integer = punchAreaMapper.selectCount(eq.select(PunchArea::getPunchAreaId));
        if (integer != null && integer > 0) {
            eq.groupBy(PunchArea::getPunchAreaId);
            IPage<PunchArea> punchAreaIPage =punchAreaMapper.selectPageInfo(new Page<>(request.getPageNum(), request.getPageSize(), false), eq);
            punchAreaIPage.setTotal(integer);
            return punchAreaIPage;
        }
        return new Page<>();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void upPunchAreaUser(String userIds, Long punchAreaId) throws FebsException {
        if (StringUtils.isBlank(userIds)) {
            throw new FebsException("请选择人员");
        }
        if (punchAreaId == null) {
            throw new FebsException("请选择区域");
        }
        punchAreaUserService.remove(new LambdaQueryWrapper<PunchAreaUser>().eq(PunchAreaUser::getTableId, punchAreaId));
        String[] split = userIds.split(",");
        ArrayList<PunchAreaUser> punchAreaUsers = new ArrayList<>();
        for (String s : split) {
            if (!StringUtils.isNumeric(s)) {
                log.error(s + "不是数字");
                throw new FebsException("新增失败");
            }
            PunchAreaUser punchAreaUser = new PunchAreaUser();
            punchAreaUser.setTableId(punchAreaId);
            punchAreaUser.setUserId(Long.valueOf(s));
            punchAreaUsers.add(punchAreaUser);
        }
        if (punchAreaUsers.size() > 0) {
            punchAreaUserService.saveBatch(punchAreaUsers, punchAreaUsers.size());
        }
    }

    /**
     * 规则配置
     *
     * @param object 规则
     */
    @Override
    public void upPunchAreaRule(String object, Long tableId) throws FebsException {
        if (StringUtils.isNotBlank(object)) {
            List<PunchAreaClocktime> punchAreaClocktimes = JSON.parseArray(object, PunchAreaClocktime.class);
            checkClockTimes(tableId, punchAreaClocktimes);
            if (punchAreaClocktimes.size() > 0) {
                punchAreaClocktimeService.saveOrUpdateBatch(punchAreaClocktimes, punchAreaClocktimes.size());
            }
        }
    }

    /**
     * 获取没在打卡区域的用户 {userId:用户id,username:用户名,realname:用户姓名,deptName:用户部门,projectName:用户项目名称}
     *
     * @return {@link List< Map < String, Object>>}
     */
    @Override
    public IPage<Map<String, Object>> getPunchAreaUser(Long punchAreaId, String username, Long deptId, Long projectId,
                                                       QueryRequest request, Integer type) {
        return punchAreaMapper.getPunchAreaUser(new Page<>(request.getPageNum(), request.getPageSize()), username,
                deptId, projectId, punchAreaId, type);
    }

    /**
     * 添加人员
     *
     * @param userIds     用户ids
     * @param punchAreaId 区域id
     */
    @Override
    public void addPunchAreaUser(String userIds, Long punchAreaId) throws FebsException {
        String[] split = userIds.split(",");
        ArrayList<PunchAreaUser> punchAreaUsers = new ArrayList<>();
        for (String s : split) {
            if (!StringUtils.isNumeric(s)) {
                throw new FebsException("数据错误");
            }
            PunchAreaUser punchAreaUser = new PunchAreaUser();
            punchAreaUser.setUserId(Long.parseLong(s));
            punchAreaUser.setTableId(punchAreaId);
            punchAreaUsers.add(punchAreaUser);
        }
        if (!punchAreaUsers.isEmpty()) {
            punchAreaUserService.saveBatch(punchAreaUsers, punchAreaUsers.size());
        }
    }

    /**
     * 删除人员
     *
     * @param userIds     用户ids
     * @param punchAreaId 区域id
     */
    @Override
    public void delPunchAreaUser(String userIds, Long punchAreaId) throws FebsException {
        punchAreaUserService.remove(new LambdaQueryWrapper<PunchAreaUser>().eq(PunchAreaUser::getTableId, punchAreaId)
                .in(PunchAreaUser::getUserId, Arrays.stream(userIds.split(",")).collect(Collectors.toSet())));
    }

    /**
     * 新增
     *
     * @param punchArea 项目打卡区域
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPunchArea(PunchArea punchArea) throws FebsException {
        save(punchArea);
        Long tableId = punchArea.getPunchAreaId();
        if (StringUtils.isNotBlank(punchArea.getClockTimes())) {
            List<PunchAreaClocktime> punchAreaClocktimes =
                    JSON.parseArray(punchArea.getClockTimes(), PunchAreaClocktime.class);
            checkClockTimes(tableId, punchAreaClocktimes);
            if (punchAreaClocktimes.size() > 0) {
                punchAreaClocktimeService.saveBatch(punchAreaClocktimes, punchAreaClocktimes.size());
            }
        }
        if (StringUtils.isNotBlank(punchArea.getUserIds())) {
            String[] split = punchArea.getUserIds().split(",");
            ArrayList<PunchAreaUser> punchAreaUsers = new ArrayList<>();
            for (String s : split) {
                if (!StringUtils.isNumeric(s)) {
                    log.error(s + "不是数字");
                    throw new FebsException("新增失败");
                }
                PunchAreaUser punchAreaUser = new PunchAreaUser();
                punchAreaUser.setTableId(tableId);
                punchAreaUser.setUserId(Long.valueOf(s));
                punchAreaUsers.add(punchAreaUser);
            }
            if (punchAreaUsers.size() > 0) {
                punchAreaUserService.saveBatch(punchAreaUsers, punchAreaUsers.size());
            }
        }
    }

    /**
     * 修改
     *
     * @param punchArea 项目打卡区域
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePunchArea(PunchArea punchArea) throws FebsException {
        updateById(punchArea);
        Long tableId = punchArea.getPunchAreaId();
        if (StringUtils.isNotBlank(punchArea.getClockTimes())) {
            punchAreaClocktimeService
                    .remove(new LambdaQueryWrapper<PunchAreaClocktime>().eq(PunchAreaClocktime::getTableId, tableId));
            List<PunchAreaClocktime> punchAreaClocktimes =
                    JSON.parseArray(punchArea.getClockTimes(), PunchAreaClocktime.class);
            checkClockTimes(tableId, punchAreaClocktimes);
            if (punchAreaClocktimes.size() > 0) {
                punchAreaClocktimeService.saveBatch(punchAreaClocktimes, punchAreaClocktimes.size());
            }
        }
        if (StringUtils.isNotBlank(punchArea.getUserIds())) {
            punchAreaUserService.remove(new LambdaQueryWrapper<PunchAreaUser>().eq(PunchAreaUser::getTableId, tableId));
            String[] split = punchArea.getUserIds().split(",");
            ArrayList<PunchAreaUser> punchAreaUsers = new ArrayList<>();
            for (String s : split) {
                if (!StringUtils.isNumeric(s)) {
                    log.error(s + "不是数字");
                    throw new FebsException("新增失败");
                }
                PunchAreaUser punchAreaUser = new PunchAreaUser();
                punchAreaUser.setTableId(tableId);
                punchAreaUser.setUserId(Long.valueOf(s));
                punchAreaUsers.add(punchAreaUser);
            }
            if (punchAreaUsers.size() > 0) {
                punchAreaUserService.saveBatch(punchAreaUsers, punchAreaUsers.size());
            }
        }
    }

    /**
     * 区域时间段设置
     *
     * @param tableId             区域id
     * @param punchAreaClocktimes 时间段
     */
    private void checkClockTimes(Long tableId, List<PunchAreaClocktime> punchAreaClocktimes) throws FebsException {
        for (PunchAreaClocktime punchAreaClocktime : punchAreaClocktimes) {
            punchAreaClocktime.setTableId(null);
            if (StringUtils.isNotBlank(punchAreaClocktime.getStartTime())) {
                if (punchAreaClocktime.getStartTime().matches("^\\d{2}:\\d{2}$")) {
                } else if (punchAreaClocktime.getStartTime().matches("^\\d:\\d{2}$")) {
                    punchAreaClocktime.setStartTime("0" + punchAreaClocktime.getStartTime());
                } else {
                    throw new FebsException("时间格式错误 00:00、0:00");
                }
                LocalTime parse = LocalTime.parse(punchAreaClocktime.getStartTime() + ":00");
                punchAreaClocktime.setStartTimeColumn(parse.getHour() * 60 + parse.getMinute());
            }
            if (StringUtils.isNotBlank(punchAreaClocktime.getEndTime())) {
                if (punchAreaClocktime.getEndTime().matches("^\\d{2}:\\d{2}$")) {
                } else if (punchAreaClocktime.getEndTime().matches("^\\d:\\d{2}$")) {
                    punchAreaClocktime.setEndTime("0" + punchAreaClocktime.getEndTime());
                } else {
                    throw new FebsException("时间格式错误 00:00、0:00");
                }
                LocalTime parse = LocalTime.parse(punchAreaClocktime.getEndTime() + ":00");
                punchAreaClocktime.setEndTimeColumn(parse.getHour() * 60 + parse.getMinute());
            }
            punchAreaClocktime.setTableId(tableId);
        }
    }

    /**
     * 删除
     *
     * @param punchArea 项目打卡区域
     */
    @Override
    public void deletePunchArea(PunchArea punchArea) {
        this.update(null, new LambdaUpdateWrapper<PunchArea>().eq(PunchArea::getPunchAreaId, punchArea.getPunchAreaId())
                .set(PunchArea::getIsDelete, 1));
    }
}
