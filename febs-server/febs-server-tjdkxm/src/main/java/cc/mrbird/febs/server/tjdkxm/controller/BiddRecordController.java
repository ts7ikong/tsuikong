package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Askfleave;
import cc.mrbird.febs.common.core.entity.tjdkxm.BiddRecord;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.mapper.BiddRecordMapper;
import cc.mrbird.febs.server.tjdkxm.service.BiddRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 业绩统计(BiddRecord)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:03
 */

@Slf4j
@Validated
@RestController
@Api(tags = "业绩统计")
@RequestMapping("/biddRecord")
public class BiddRecordController {
    /**
     * 服务对象
     */
    @Autowired
    private BiddRecordService biddRecordService;

    @GetMapping
    public Rest<List<BiddRecord>> getAllBiddRecords(BiddRecord biddRecord) {
        return Rest.data(biddRecordService.findBiddRecords(biddRecord));
    }

    @ApiOperation(value = "分页查询--用户另外请求", notes = "暂无查询条件")
    @GetMapping("list")
    public FebsResponse biddRecordList(QueryRequest request, BiddRecord biddRecord) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(biddRecordService.findBiddRecords(request, biddRecord));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    @OperateLog(mapper = BiddRecordMapper.class, className = BiddRecord.class, type = Operate.TYPE_ADD)
    public void addBiddRecord(@Valid @RequestBody BiddRecord biddRecord) throws FebsException {
        try {
            this.biddRecordService.createBiddRecord(biddRecord);
        } catch (Exception e) {
            String message = "新增业绩统计失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @DeleteMapping
    @OperateLog(mapper = BiddRecordMapper.class, className = BiddRecord.class, type = Operate.TYPE_DELETE)
    public void deleteBiddRecord(BiddRecord biddRecord) throws FebsException {
        try {
            this.biddRecordService.deleteBiddRecord(biddRecord);
        } catch (FebsException e) {
            throw new FebsException(e);
        } catch (Exception e) {
            String message = "删除业绩统计失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    @OperateLog(mapper = BiddRecordMapper.class, className = BiddRecord.class, type = Operate.TYPE_MODIFY)
    public void updateBiddRecord(@RequestBody BiddRecord biddRecord) throws FebsException {
        try {
            this.biddRecordService.updateBiddRecord(biddRecord);
        } catch (FebsException e) {
            throw new FebsException(e);
        } catch (Exception e) {
            String message = "修改业绩统计失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
