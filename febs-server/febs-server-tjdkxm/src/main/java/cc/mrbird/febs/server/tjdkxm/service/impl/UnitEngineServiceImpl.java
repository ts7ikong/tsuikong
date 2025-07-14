package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdkxm.UnitEngine;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.common.core.utils.OrderUtils;
import cc.mrbird.febs.server.tjdkxm.config.LogRecordContext;
import cc.mrbird.febs.server.tjdkxm.mapper.UnitEngineMapper;
import cc.mrbird.febs.server.tjdkxm.service.CacheableService;
import cc.mrbird.febs.server.tjdkxm.service.UnitEngineService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * UnitEngineService实现
 *
 * @author zlkj_cg
 * @date 2022-01-12 15:51:07
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UnitEngineServiceImpl extends ServiceImpl<UnitEngineMapper, UnitEngine> implements UnitEngineService {

    private final UnitEngineMapper unitEngineMapper;
    private final CacheableService cacheableService;
    @Autowired
    private LogRecordContext logRecordContext;


    @Override
    public MyPage<UnitEngine> findUnitEngines(QueryRequest request, UnitEngine unitEngine) throws FebsException {
        QueryWrapper<UnitEngine> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("IS_DELETE", 0);
        queryWrapper.orderByDesc("UNIT_ID");
        OrderUtils.setQuseryOrder(queryWrapper, request);
        //项目id
        if (unitEngine.getUnitProjectid() == null) {
            throw new FebsException("请先选择项目");
        }
        queryWrapper.eq("UNIT_PROJECTID", unitEngine.getUnitProjectid());
        if (StringUtils.isNotBlank(unitEngine.getUnitName())) {
            queryWrapper.and(wapper -> {
                wapper.like("UNIT_NAME", unitEngine.getUnitName());
                wapper.or().like("UNIT_CONTRACTCODE", unitEngine.getUnitName());
                wapper.or().like("UNIT_PERMITNUMBER", unitEngine.getUnitName());
            });
        }
        Page<UnitEngine> page = new Page<>(request.getPageNum(), request.getPageSize());
        this.unitEngineMapper.selectPage(page, queryWrapper);
        return new MyPage<>(page);
    }

    @Override
    public List<UnitEngine> findUnitEngines(UnitEngine unitEngine) throws FebsException {
        QueryWrapper<UnitEngine> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("IS_DELETE", 0);
        //项目id
        if (unitEngine.getUnitProjectid() == null) {
            throw new FebsException("请先选择项目");
        }
        queryWrapper.eq("UNIT_PROJECTID", unitEngine.getUnitProjectid());
        return this.unitEngineMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUnitEngine(UnitEngine unitEngine) throws FebsException {
        Long unitProjectid = unitEngine.getUnitProjectid();
        FebsUtil.isProjectNotNull(unitProjectid);
        unitEngine.setCreateUserid(FebsUtil.getCurrentUserId());
        unitEngine.setCreateTime(new Date());
        if (unitEngine.getUnitPlanstarttime() != null && unitEngine.getUnitPlanendtime() != null) {
            Long unitPlanstarttime = unitEngine.getUnitPlanstarttime().getTime();
            Long unitPlanendtime = unitEngine.getUnitPlanendtime().getTime();
            if (unitPlanendtime < unitPlanstarttime) {
                throw new FebsException("计划竣工日期应在计划开工日期之后");
            }
            long day = (unitPlanendtime - unitPlanstarttime) / 24 / 60 / 60 / 1000;
            unitEngine.setUnitPlandays(String.valueOf(day));
        }
        if (unitEngine.getUnitActualstarttime() != null && unitEngine.getUnitActualendtime() != null) {
            Long unitPlanstarttime = unitEngine.getUnitActualstarttime().getTime();
            Long unitPlanendtime = unitEngine.getUnitActualendtime().getTime();
            if (unitPlanendtime < unitPlanstarttime) {
                throw new FebsException("实际竣工日期应在实际开工日期之后");
            }
            long day = (unitPlanendtime - unitPlanstarttime) / 24 / 60 / 60 / 1000;
            unitEngine.setUnitAcualedays(String.valueOf(day));
        }
        this.save(unitEngine);
        cacheableService.upgetAllProjectChooses(unitProjectid);
        logRecordContext.putVariable("id", unitEngine.getUnitId());
        logRecordContext.putVariable("projectId", unitEngine.getUnitProjectid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUnitEngine(UnitEngine unitEngine) throws FebsException {
        UnitEngine unitEngine1 = unitEngineMapper.selectById(unitEngine.getUnitId());
        if (unitEngine1 == null) {
            throw new FebsException("修改失败");
        }
        if (unitEngine.getUnitPlanstarttime() != null && unitEngine.getUnitPlanendtime() != null) {
            long unitPlanstarttime;
            long unitPlanendtime;
            if (unitEngine.getUnitPlanstarttime() == null) {
                unitPlanstarttime = unitEngine1.getUnitPlanstarttime().getTime();
            } else {
                unitPlanstarttime = unitEngine.getUnitPlanstarttime().getTime();
            }
            if (unitEngine.getUnitPlanendtime() == null) {
                unitPlanendtime = unitEngine1.getUnitPlanendtime().getTime();
            } else {
                unitPlanendtime = unitEngine.getUnitPlanendtime().getTime();
            }
            if (unitPlanendtime < unitPlanstarttime) {
                throw new FebsException("计划竣工日期应在计划开工日期之后");
            }
            long day = (unitPlanstarttime - unitPlanendtime) / 24 / 60 / 60 / 1000;
            unitEngine.setUnitPlandays(String.valueOf(day));
        }
        if (unitEngine.getUnitActualstarttime() != null && unitEngine.getUnitActualendtime() != null) {
            long unitPlanstarttime;
            long unitPlanendtime;
            if (unitEngine.getUnitPlanstarttime() == null) {
                unitPlanstarttime = unitEngine1.getUnitActualstarttime().getTime();
            } else {
                unitPlanstarttime = unitEngine.getUnitActualstarttime().getTime();
            }
            if (unitEngine.getUnitPlanendtime() == null) {
                unitPlanendtime = unitEngine1.getUnitActualendtime().getTime();
            } else {
                unitPlanendtime = unitEngine.getUnitActualendtime().getTime();
            }
            if (unitPlanendtime < unitPlanstarttime) {
                throw new FebsException("实际竣工日期应在实际开工日期之后");
            }
            long day = (unitPlanstarttime - unitPlanendtime) / 24 / 60 / 60 / 1000;
            unitEngine.setUnitAcualedays(String.valueOf(day));
        }
        boolean b = this.updateById(unitEngine);
        if (b) {
            logRecordContext.putVariable("projectId", unitEngine1.getUnitProjectid());
            if (unitEngine.getUnitName() != null) {
                cacheableService.upgetAllProjectChooses(unitEngine1.getUnitProjectid());
            }
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUnitEngine(UnitEngine unitEngine) throws Exception {
        UnitEngine unitEngine1 = unitEngineMapper.selectById(unitEngine.getUnitId());
        if (unitEngine1 == null) {
            throw new Exception("");
        }
        this.update(null, new LambdaUpdateWrapper<UnitEngine>().eq(UnitEngine::getUnitId, unitEngine.getUnitId())
                .set(UnitEngine::getIsDelete, 1));
        cacheableService.upgetAllProjectChooses(unitEngine1.getUnitProjectid());
        logRecordContext.putVariable("projectId", unitEngine1.getUnitProjectid());
    }
}
