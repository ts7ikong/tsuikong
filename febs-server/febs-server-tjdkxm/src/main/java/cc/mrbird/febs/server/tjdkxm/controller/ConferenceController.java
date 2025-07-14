package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdkxm.BiddRecord;
import cc.mrbird.febs.common.core.entity.tjdkxm.Conference;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.mapper.BiddRecordMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.ConferenceMapper;
import cc.mrbird.febs.server.tjdkxm.service.ConferenceService;
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
 * 会议表ID(Conference)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:03
 */

@Slf4j
@Validated
@RestController
@Api(tags = "会议表ID(Conference)控制层")
@RequestMapping("/conference")
@RequiredArgsConstructor
public class ConferenceController {
    /**
     * 服务对象
     */
    @Autowired
    private ConferenceService conferenceService;

    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    public FebsResponse conferenceList(QueryRequest request, Conference.Params conference) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(conferenceService.findConferences(request, conference));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "根據id查询", notes = "暂无查询条件")
    @GetMapping("one")
    public FebsResponse conferenceList(Long conferenceId) {
        Conference conference = conferenceService.getById(conferenceId);
        return new FebsResponse().data(conference);
    }

    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    @OperateLog(mapper = ConferenceMapper.class, className = Conference.class, type = Operate.TYPE_ADD)
    public void addConference(@Valid Conference conference) throws FebsException {
        try {
            this.conferenceService.createConference(conference);
        } catch (Exception e) {
            String message = "新增会议信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @DeleteMapping
    @OperateLog(mapper = ConferenceMapper.class, className = Conference.class, type = Operate.TYPE_DELETE)
    public void deleteConference(Conference conference) throws FebsException {
        try {
            this.conferenceService.deleteConference(conference);
        } catch (FebsException e) {
            throw new FebsException(e.getMessage());
        } catch (Exception e) {
            String message = "删除会议信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    @OperateLog(mapper = ConferenceMapper.class, className = Conference.class, type = Operate.TYPE_MODIFY)
    public void updateConference(Conference conference) throws FebsException {
        try {
            this.conferenceService.updateConference(conference);
        } catch (FebsException e) {
            throw new FebsException(e.getMessage());
        } catch (Exception e) {
            String message = "修改会议信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "开始会议")
    @GetMapping("/start")
    public void conferenceStart(Long conferenceId) throws FebsException {
        try {
            this.conferenceService.conferenceStart(conferenceId);
        } catch (FebsException e1) {
            throw new FebsException(e1.getMessage());
        } catch (Exception e) {
            String message = "开启会议失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "结束会议   【所有人退出会议 算会议结束】")
    @GetMapping("/end")
    public void conferenceEnd(Long conferenceId) throws FebsException {
        try {
            this.conferenceService.conferenceEnd(conferenceId);
        } catch (FebsException e1) {
            throw new FebsException(e1.getMessage());
        } catch (Exception e) {
            String message = "开启会议失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
