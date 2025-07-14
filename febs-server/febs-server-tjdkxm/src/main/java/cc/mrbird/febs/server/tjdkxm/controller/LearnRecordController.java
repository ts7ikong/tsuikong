package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdkxm.LearnRecord;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.service.LearnRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 党建学习表(LearnRecord)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:04
 */

@Slf4j
@Validated
@RestController
@Api(tags = "党建用户学习表(LearnRecord)控制层")
@RequestMapping("/learnRecord")
@RequiredArgsConstructor
public class LearnRecordController {
    /**
     * 服务对象
     */
    @Autowired
    private LearnRecordService learnRecordService;

    @GetMapping
    @ApiIgnore
    public Rest<List<LearnRecord>> getAllLearnRecords(LearnRecord learnRecord) {
        return Rest.data(learnRecordService.findLearnRecords(learnRecord));
    }

    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    @ApiIgnore
    public FebsResponse learnRecordList(QueryRequest request, LearnRecord learnRecord) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(learnRecordService.findLearnRecords(request, learnRecord));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "新增接口 json", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    @ApiIgnore
    public void addLearnRecord(@Valid @RequestBody LearnRecord learnRecord) throws FebsException {
        try {
            this.learnRecordService.createLearnRecord(learnRecord);
        } catch (Exception e) {
            String message = "新增党建学习表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @DeleteMapping
    @ApiIgnore
    public void deleteLearnRecord(@RequestBody LearnRecord learnRecord) throws FebsException {
        try {
            this.learnRecordService.deleteLearnRecord(learnRecord);
        } catch (Exception e) {
            String message = "删除党建学习表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口 json", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    @ApiIgnore
    public void updateLearnRecord(LearnRecord learnRecord) throws FebsException {
        try {
            this.learnRecordService.updateLearnRecord(learnRecord);
        } catch (Exception e) {
            String message = "修改党建学习表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
