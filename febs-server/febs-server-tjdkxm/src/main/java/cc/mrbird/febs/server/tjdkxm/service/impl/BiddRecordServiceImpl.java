package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.constant.ButtonConstant;
import cc.mrbird.febs.common.core.entity.constant.MenuConstant;
import cc.mrbird.febs.common.core.entity.tjdkxm.BiddRecord;
import cc.mrbird.febs.common.core.entity.tjdkxm.BiddRecordUser;
import cc.mrbird.febs.common.core.entity.tjdkxm.QualityplanUser;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.DateUtil;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.common.core.utils.OrderUtils;
import cc.mrbird.febs.server.tjdkxm.config.LogRecordContext;
import cc.mrbird.febs.server.tjdkxm.mapper.BiddRecordMapper;
import cc.mrbird.febs.server.tjdkxm.service.BiddRecordService;
import cc.mrbird.febs.server.tjdkxm.service.BiddRecordUserService;
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

import java.util.*;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/9 12:04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class BiddRecordServiceImpl extends ServiceImpl<BiddRecordMapper, BiddRecord> implements BiddRecordService {
    @Autowired
    private BiddRecordMapper biddRecordMapper;
    @Autowired
    private CacheableService cacheableService;
    @Autowired
    private BiddRecordUserService biddRecordUserService;
    @Autowired
    private LogRecordContext logRecordContext;

    @Override
    public MyPage<BiddRecord> findBiddRecords(QueryRequest request, BiddRecord biddRecord) {
        QueryWrapper<BiddRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("IS_DELETE", 0);
        queryWrapper.orderByDesc("CREATE_TIME");
        OrderUtils.setQuseryOrder(queryWrapper, request);
        if (StringUtils.isNotEmpty(biddRecord.getBiddrecordCode())) {
            queryWrapper.and(wapper -> {
                wapper.like("BIDDRECORD_CODE", biddRecord.getBiddrecordCode());
                wapper.or().like("BIDDRECORD_PROJECTNAME", biddRecord.getBiddrecordCode());
            });
        }
        if (StringUtils.isNotEmpty(biddRecord.getBiddrecordProjectName())) {
            queryWrapper.like("BIDDRECORD_PROJECTNAME", biddRecord.getBiddrecordProjectName());
        }
        if (StringUtils.isNotBlank(biddRecord.getStartTime())) {
            queryWrapper.ge("CREATE_TIME", biddRecord.getStartTime());
        }
        if (StringUtils.isNotBlank(biddRecord.getEndTime())) {
            queryWrapper.le("CREATE_TIME", biddRecord.getEndTime());
        }
        Integer integer = this.biddRecordMapper.selectCount(queryWrapper.select("1"));
        if (integer == null || integer == 0) {
            return new MyPage<>();
        }
        queryWrapper.groupBy("BIDDRECORD_ID");
        IPage<BiddRecord> page = this.biddRecordMapper
            .selectPageInfo(new Page<>(request.getPageNum(), request.getPageSize(), false), queryWrapper);
        page.setTotal(integer);
        return new MyPage<>(page);
    }

    @Override
    public List<BiddRecord> findBiddRecords(BiddRecord biddRecord) {
        LambdaQueryWrapper<BiddRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BiddRecord::getIsDelete, 0);
        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createBiddRecord(BiddRecord biddRecord) throws FebsException {
        biddRecord.setCreateUserid(FebsUtil.getCurrentUserId());
        biddRecord.setCreateTime(new Date());
        this.save(biddRecord);
        logRecordContext.putVariable("id", biddRecord.getBiddrecordId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBiddRecord(BiddRecord biddRecord) throws FebsException {
        this.updateById(biddRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBiddRecord(BiddRecord biddRecord) throws FebsException {
        this.update(null, new LambdaUpdateWrapper<BiddRecord>()
            .eq(BiddRecord::getBiddrecordId, biddRecord.getBiddrecordId()).set(BiddRecord::getIsDelete, 1));

    }
}
