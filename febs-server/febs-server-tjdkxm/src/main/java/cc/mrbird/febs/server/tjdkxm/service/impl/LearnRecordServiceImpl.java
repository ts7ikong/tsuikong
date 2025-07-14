package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdkxm.LearnRecord;
import cc.mrbird.febs.common.core.entity.tjdkxm.Project;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.config.LogRecordContext;
import cc.mrbird.febs.server.tjdkxm.mapper.LearnRecordMapper;
import cc.mrbird.febs.server.tjdkxm.service.CacheableService;
import cc.mrbird.febs.server.tjdkxm.service.LearnRecordService;
import cc.mrbird.febs.server.tjdkxm.service.ProjectService;
import cc.mrbird.febs.server.tjdkxm.service.UserProjectService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.processing.FilerException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * LearnRecordService实现
 *
 * @author zlkj_cg
 * @date 2022-01-12 15:51:04
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class LearnRecordServiceImpl extends ServiceImpl<LearnRecordMapper, LearnRecord> implements LearnRecordService {

    private final LearnRecordMapper learnRecordMapper;
    private final CacheableService cacheableService;
    @Autowired
    private LogRecordContext logRecordContext;

    @Override
    public IPage<LearnRecord> findLearnRecords(QueryRequest request, LearnRecord learnRecord) {
        QueryWrapper<LearnRecord> queryWrapper = new QueryWrapper<>();
        getParam(queryWrapper);
        return this.learnRecordMapper.selectPageInfo(new Page<>(request.getPageNum(), request.getPageSize()), queryWrapper);
    }

    private void getParam(QueryWrapper<LearnRecord> queryWrapper) {
        queryWrapper.eq("IS_DELETE", 0);
    }

    @Override
    public List<LearnRecord> findLearnRecords(LearnRecord learnRecord) {
        LambdaQueryWrapper<LearnRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LearnRecord::getIsDelete, 0);
        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createLearnRecord(LearnRecord learnRecord) {
        learnRecord.setLearnrecordUserid(FebsUtil.getCurrentUserId());
        learnRecord.setCreateTime(new Date());
        this.save(learnRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLearnRecord(LearnRecord learnRecord) throws FilerException {
        this.updateById(learnRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLearnRecord(LearnRecord learnRecord) throws FilerException {
        this.update(new LambdaUpdateWrapper<LearnRecord>().
                eq(LearnRecord::getLearnrecordId, learnRecord.getLearnrecordId()).
                set(LearnRecord::getIsDelete, 1));
    }
}
