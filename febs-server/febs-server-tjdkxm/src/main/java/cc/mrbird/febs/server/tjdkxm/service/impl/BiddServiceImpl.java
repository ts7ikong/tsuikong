package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Bidd;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.common.core.utils.OrderUtils;
import cc.mrbird.febs.server.tjdkxm.config.LogRecordContext;
import cc.mrbird.febs.server.tjdkxm.mapper.BiddMapper;
import cc.mrbird.febs.server.tjdkxm.service.BiddService;
import cc.mrbird.febs.server.tjdkxm.service.CacheableService;
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

import javax.annotation.processing.Completion;
import java.util.Date;
import java.util.List;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/9 12:04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class BiddServiceImpl extends ServiceImpl<BiddMapper, Bidd> implements BiddService {
    @Autowired
    private LogRecordContext logRecordContext;
    @Autowired
    private CacheableService cacheableService;

    @Override
    public IPage<Bidd> findBidds(QueryRequest request, Bidd.Params bidd) {
        QueryWrapper<Bidd> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("IS_DELETE", 0);
        queryWrapper.orderByDesc("CREATE_TIME");
        OrderUtils.setQuseryOrder(queryWrapper, request);
        if (StringUtils.isNotEmpty(bidd.getBiddUnit())) {
            queryWrapper.like("BIDD_UNIT", bidd.getBiddUnit());
        }
        if (StringUtils.isNotEmpty(bidd.getBiddStartTime())) {
            queryWrapper.ge("BIDD_TIME", bidd.getBiddStartTime());
        }
        if (StringUtils.isNotEmpty(bidd.getBiddEndTime())) {
            queryWrapper.lt("BIDD_TIME", bidd.getBiddEndTime());
        }
        if (bidd.getBiddTuibond() != null) {
            queryWrapper.eq("BIDD_TUIBOND", bidd.getBiddTuibond());
        }
        return this.baseMapper.selectPage(new Page<>(request.getPageNum(), request.getPageSize()), queryWrapper);
    }

    @Override
    public List<Bidd> findBidds(Bidd bidd) {
        LambdaQueryWrapper<Bidd> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Bidd::getIsDelete, 0);
        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createBidd(Bidd bidd) {
        bidd.setCreateUserid(FebsUtil.getCurrentUserId());
        bidd.setCreateTime(new Date());
        this.save(bidd);
        logRecordContext.putVariable("id", bidd.getBiddId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBidd(Bidd bidd) throws FebsException {
        if (bidd.getBiddId() == null) {
            throw new FebsException("请选择记录");
        }
        Bidd bidd1 = baseMapper.selectById(bidd.getBiddId());
        cacheableService.hasPermission(bidd1.getCreateUserid());
        this.updateById(bidd);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBidd(Bidd bidd) throws FebsException {
        Bidd bidd1 = baseMapper.selectById(bidd.getBiddId());
        if (bidd.getBiddId() == null || bidd1 == null) {
            throw new FebsException("请选择要删除的记录");
        }
        cacheableService.hasPermission(bidd1.getCreateUserid());
        this.update(null,
            new LambdaUpdateWrapper<Bidd>().eq(Bidd::getBiddId, bidd.getBiddId()).set(Bidd::getIsDelete, 1));

    }
}
