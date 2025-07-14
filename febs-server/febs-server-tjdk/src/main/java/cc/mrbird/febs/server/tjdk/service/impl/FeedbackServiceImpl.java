package cc.mrbird.febs.server.tjdk.service.impl;

import cc.mrbird.febs.common.core.entity.tjdk.Feedback;
import cc.mrbird.febs.server.tjdk.config.LogRecordContext;
import cc.mrbird.febs.server.tjdk.mapper.FeedbackMapper;
import cc.mrbird.febs.server.tjdk.service.FeedbackService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cc.mrbird.febs.common.core.entity.QueryRequest;

import java.util.Date;
import java.util.List;

/**
 * FeedbackService实现
 *
 * @author zlkj_cg
 * @date 2022-01-12 15:51:07
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {
    @Autowired
    private LogRecordContext logRecordContext;
    @Autowired
    private FeedbackMapper feedbackMapper;

    @Override
    public IPage<Feedback> findFeedbacks(QueryRequest request, Feedback feedback) {
        LambdaQueryWrapper<Feedback> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Feedback::getIsDelete, 0);
        if (StringUtils.isNotBlank(feedback.getFeedbackType())) {
            queryWrapper.eq(Feedback::getFeedbackType, feedback.getFeedbackType());
        }
        if (StringUtils.isNotBlank(feedback.getFeedbackContent())) {
            queryWrapper.and(wapper -> {
                wapper.like(Feedback::getFeedbackContent, feedback.getFeedbackContent());
                wapper.or().like(Feedback::getFeedbackEmal, feedback.getFeedbackContent());
                wapper.or().like(Feedback::getFeedbackNumber, feedback.getFeedbackContent());
            });
        }
        queryWrapper.orderByDesc(Feedback::getFeedbackTime);
        Page<Feedback> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Feedback> findFeedbacks(Feedback feedback) {
        LambdaQueryWrapper<Feedback> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Feedback::getIsDelete, 0);

        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createFeedback(Feedback feedback) {
        feedback.setFeedbackTime(new Date());
        this.save(feedback);
        logRecordContext.putVariable("id", feedback.getFeedbackId());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFeedback(Feedback feedback) {
        this.updateById(feedback);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFeedback(Feedback feedback) {

        this.update(new LambdaUpdateWrapper<Feedback>().eq(Feedback::getFeedbackId, feedback.getFeedbackId())
                .set(Feedback::getIsDelete, 1));
    }
}
