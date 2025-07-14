package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Conference;
import cc.mrbird.febs.common.core.entity.tjdkxm.ConferenceThere;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.mapper.ConferenceThereMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.ConferenceThereUserMapper;
import cc.mrbird.febs.server.tjdkxm.service.ConferenceThereService;
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
@Api(tags = "三重一大会议（主要是用于用户学习，所以以记录为主）")
@RequestMapping("/conferencethere")
@RequiredArgsConstructor
public class ConferenceThereController {
    /**
     * 服务对象
     */
    @Autowired
    private ConferenceThereService conferenceThereService;

    @GetMapping
    public Rest<List<ConferenceThere>> getAllConferenceTheres(ConferenceThere conferenceThere) {
        return Rest.data(conferenceThereService.findConferenceTheres(conferenceThere));
    }

    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    public FebsResponse conferenceThereList(QueryRequest request, ConferenceThere conferenceThere) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(conferenceThereService.findConferenceTheres(request, conferenceThere));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    @OperateLog(mapper = ConferenceThereMapper.class, className = ConferenceThere.class, type = Operate.TYPE_ADD)
    public void addConferenceThere(@Valid @RequestBody ConferenceThere conferenceThere) throws FebsException {
        try {
            this.conferenceThereService.createConferenceThere(conferenceThere);
        } catch (Exception e) {
            String message = "新增会议表ID失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @DeleteMapping
    @OperateLog(mapper = ConferenceThereMapper.class, className = ConferenceThere.class, type = Operate.TYPE_DELETE)

    public void deleteConferenceThere(ConferenceThere conferenceThere) throws FebsException {
        try {
            this.conferenceThereService.deleteConferenceThere(conferenceThere);
        } catch (Exception e) {
            String message = "删除会议表ID失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    @OperateLog(mapper = ConferenceThereMapper.class, className = ConferenceThere.class, type = Operate.TYPE_MODIFY)
    public void updateConferenceThere(@RequestBody ConferenceThere conferenceThere) throws FebsException {
        try {
            this.conferenceThereService.updateConferenceThere(conferenceThere);
        } catch (Exception e) {
            String message = "修改会议表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
