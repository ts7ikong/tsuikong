package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Parcel;
import cc.mrbird.febs.common.core.entity.tjdkxm.ParcelUnit;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.common.core.utils.OrderUtils;
import cc.mrbird.febs.server.tjdkxm.config.LogRecordContext;
import cc.mrbird.febs.server.tjdkxm.mapper.ParcelUnitMapper;
import cc.mrbird.febs.server.tjdkxm.service.CacheableService;
import cc.mrbird.febs.server.tjdkxm.service.ParcelUnitService;
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
import java.util.List;
import java.util.Map;

/**
 * ParcelUnitService实现
 *
 * @author 14059
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ParcelUnitServiceImpl extends ServiceImpl<ParcelUnitMapper, ParcelUnit> implements ParcelUnitService {

    private final ParcelUnitMapper parcelUnitMapper;
    private final CacheableService cacheableService;
    @Autowired
    private LogRecordContext logRecordContext;

    @Override
    public IPage<ParcelUnit> findParcelUnits(QueryRequest request, ParcelUnit parcelUnit) {
        QueryWrapper<ParcelUnit> queryWrapper = new QueryWrapper<ParcelUnit>();
        queryWrapper.eq("IS_DELETE", 0);
        OrderUtils.setQuseryOrder(queryWrapper, request);
        queryWrapper.orderByDesc("PARCELUNIT_ID");
        if (StringUtils.isNotBlank(parcelUnit.getParcelUnitName())) {
            queryWrapper.like("PARCELUNIT_NAME", parcelUnit.getParcelUnitName());
        }
        if (StringUtils.isNotBlank(parcelUnit.getParcelUnitPerson())) {
            queryWrapper.like("PARCELUNIT_PERSON", parcelUnit.getParcelUnitPerson());
        }
        return parcelUnitMapper.selectPage(new Page<>(request.getPageNum(), request.getPageSize()), queryWrapper);
    }

    @Override
    public List<ParcelUnit> findParcelUnits(ParcelUnit parcel) {
        LambdaQueryWrapper<ParcelUnit> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ParcelUnit::getIsDelete, 0);
        return this.parcelUnitMapper.selectList(queryWrapper);
    }

    @Override
    public List<Map<String, Object>> findParcelUnits() {
        return cacheableService.findParcelUnits();

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createParcelUnit(ParcelUnit parcel) {
        parcel.setCreateUserid(FebsUtil.getCurrentUserId());
        parcel.setCreateTime(new Date());
        this.save(parcel);
        cacheableService.upFindParcelUnits();
        logRecordContext.putVariable("id", parcel.getParcelUnitId());

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateParcelUnit(ParcelUnit parcel) throws FebsException {
        ParcelUnit parcel1 = parcelUnitMapper.selectById(parcel.getParcelUnitId());
//        if (!cacheableService.isDeleteAuth(parcel1.getCreateUserid())) {
//            throw new FebsException("权限不足");
//        }
        this.updateById(parcel);
        cacheableService.upFindParcelUnits();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteParcelUnit(ParcelUnit parcel) throws FebsException {
        ParcelUnit parcel1 = parcelUnitMapper.selectById(parcel.getParcelUnitId());
//        if (!cacheableService.isDeleteAuth(parcel1.getCreateUserid())) {
//            throw new FebsException("权限不足");
//        }
        this.update(null, new LambdaUpdateWrapper<ParcelUnit>()
                .eq(ParcelUnit::getParcelUnitId, parcel.getParcelUnitId())
                .set(ParcelUnit::getIsDelete, 1));
        cacheableService.upFindParcelUnits();
    }
}
