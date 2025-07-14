package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Parcel;
import cc.mrbird.febs.common.core.entity.tjdkxm.Subitem;
import cc.mrbird.febs.common.core.entity.tjdkxm.UnitEngine;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.common.core.utils.OrderUtils;
import cc.mrbird.febs.server.tjdkxm.config.LogRecordContext;
import cc.mrbird.febs.server.tjdkxm.mapper.SubitemMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.UnitEngineMapper;
import cc.mrbird.febs.server.tjdkxm.service.CacheableService;
import cc.mrbird.febs.server.tjdkxm.service.SubitemService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * SubitemService实现
 *
 * @author zlkj_cg
 * @date 2022-01-12 15:51:07
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SubitemServiceImpl extends ServiceImpl<SubitemMapper, Subitem> implements SubitemService {

    private final SubitemMapper subitemMapper;
    private final UnitEngineMapper unitEngineMapper;
    private final CacheableService cacheableService;
    @Autowired
    private LogRecordContext logRecordContext;

    @Override
    public IPage<Subitem> findSubitems(QueryRequest request, Subitem subitem) throws FebsException {
        QueryWrapper<Subitem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("IS_DELETE", 0);
        OrderUtils.setQuseryOrder(queryWrapper, request);
        // 分部id
        if (subitem.getSubitemProjectid() == null) {
            queryWrapper.eq("SUBITEM_PROJECTID", subitem.getSubitemProjectid());
        }
        // 单位项目id
        if (subitem.getSubitemUnitengineid() != null) {
            queryWrapper.eq("SUBITEM_UNITENGINEID", subitem.getSubitemUnitengineid());
        }
        // 分部id
        if (subitem.getSubitemParcelid() == null) {
            throw new FebsException("请先选择分部项目");
        }
        queryWrapper.eq("SUBITEM_PARCELID", subitem.getSubitemParcelid());
        if (StringUtils.isNotBlank(subitem.getSubitemName())) {
            queryWrapper.like("SUBITEM_NAME", subitem.getSubitemName());
        }
        if (StringUtils.isNotBlank(subitem.getSubitemContent())) {
            queryWrapper.and(wapper -> {
                wapper.like("SUBITEM_CONTENT", subitem.getSubitemContent());
                wapper.or().like("SUBITEM_PERSON", subitem.getSubitemContent());
            });
        }
        Page<Subitem> page =
            subitemMapper.selectPageDeep(new Page<>(request.getPageNum(), request.getPageSize()), queryWrapper);
        List<Subitem> records = page.getRecords();
        if (records.size() > 0) {
            Set<Long> subitemUnitengineids = new HashSet<>();
            records.forEach(record -> {
                subitemUnitengineids.add(record.getSubitemUnitengineid());
            });
            if (subitemUnitengineids.size() > 0) {
                List<UnitEngine> unitEngines = unitEngineMapper.selectList(
                    new LambdaQueryWrapper<UnitEngine>().select(UnitEngine::getUnitId, UnitEngine::getUnitName)
                        .in(UnitEngine::getUnitId, subitemUnitengineids));
                records.forEach(record -> {
                    Long subitemUnitengineid = record.getSubitemUnitengineid();
                    unitEngines.forEach(unitEngine -> {
                        if (subitemUnitengineid != null) {
                            if (unitEngine.getUnitId().equals(subitemUnitengineid)) {
                                record.setSubitemUnitengineName(unitEngine.getUnitName());
                            }
                        }
                    });
                });
            }
            page.setRecords(records);
        }
        return page;
    }

    @Override
    public List<Subitem> findSubitems(Subitem subitem) throws FebsException {
        LambdaQueryWrapper<Subitem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Subitem::getIsDelete, 0);
        // 项目id
        // 单位工程
        if (subitem.getSubitemParcelid() == null) {
            throw new FebsException("请先选择分部工程");
        }
        queryWrapper.eq(Subitem::getSubitemParcelid, subitem.getSubitemParcelid());
        return this.subitemMapper.selectListDeep(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createSubitem(Subitem subitem) throws FebsException {
        FebsUtil.isProjectNotNull(subitem.getSubitemProjectid());
        subitem.setCreateUserid(FebsUtil.getCurrentUserId());
        subitem.setCreateTime(new Date());
        this.save(subitem);
        cacheableService.upgetAllProjectChooses(subitem.getSubitemProjectid());
        logRecordContext.putVariable("id", subitem.getSubitemId());
        logRecordContext.putVariable("projectId", subitem.getSubitemProjectid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSubitem(Subitem subitem) {
        // subitem.setSubitemProjectid(null);
        boolean b = this.updateById(subitem);
        if (b) {
            Subitem subitem1 = subitemMapper.selectById(subitem.getSubitemId());
            logRecordContext.putVariable("projectId", subitem1.getSubitemProjectid());
            if (subitem.getSubitemName() != null) {
                cacheableService.upgetAllProjectChooses(subitem.getSubitemProjectid());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSubitem(Subitem subitem) throws Exception {
        Subitem subitem1 = subitemMapper.selectById(subitem.getSubitemId());
        if (subitem1 == null) {
            throw new Exception("");
        }
        this.update(new LambdaUpdateWrapper<Subitem>().eq(Subitem::getSubitemId, subitem.getSubitemId())
            .set(Subitem::getIsDelete, 1));
        cacheableService.upgetAllProjectChooses(subitem1.getSubitemProjectid());
        logRecordContext.putVariable("projectId", subitem1.getSubitemProjectid());
    }
}
