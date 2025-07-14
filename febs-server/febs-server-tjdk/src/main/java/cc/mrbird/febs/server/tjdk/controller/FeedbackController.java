package cc.mrbird.febs.server.tjdk.controller;

import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdk.Feedback;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdk.mapper.FeedbackMapper;
import cc.mrbird.febs.server.tjdk.service.FeedbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 反馈信息(Feedback)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:07
 */

@Slf4j
@Validated
@RestController
@Api(tags = "反馈信息(Feedback)控制层")
@RequestMapping("/feedback")
public class FeedbackController {
    /**
     * 服务对象
     */
    @Autowired
    private FeedbackService feedbackService;

    @GetMapping
    public Rest<List<Feedback>> getAllFeedbacks(Feedback feedback) {
        return Rest.data(feedbackService.findFeedbacks(feedback));
    }

    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    public FebsResponse FeedbackList(QueryRequest request, Feedback feedback) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(feedbackService.findFeedbacks(request, feedback));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    @OperateLog(mapper = FeedbackMapper.class, className = Feedback.class, type = Operate.TYPE_ADD)

    public void addFeedback(@Valid Feedback feedback) throws FebsException {
        try {
            this.feedbackService.createFeedback(feedback);
        } catch (Exception e) {
            String message = "新增反馈信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @DeleteMapping
    @OperateLog(mapper = FeedbackMapper.class, className = Feedback.class, type = Operate.TYPE_DELETE)

    public void deleteFeedback(Feedback feedback) throws FebsException {
        try {
            this.feedbackService.deleteFeedback(feedback);
        } catch (Exception e) {
            String message = "删除反馈信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    @OperateLog(mapper = FeedbackMapper.class, className = Feedback.class, type = Operate.TYPE_MODIFY)

    public void updateFeedback(Feedback feedback) throws FebsException {
        try {
            this.feedbackService.updateFeedback(feedback);
        } catch (Exception e) {
            String message = "修改反馈信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
