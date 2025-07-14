package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdkxm.DocumentClass;
import cc.mrbird.febs.common.core.entity.tjdkxm.MajorProjectLog;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.mapper.DocumentClassMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.MajorProjectLogMapper;
import cc.mrbird.febs.server.tjdkxm.service.MajorProjectLogService;
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
 * 重大项目日志(MajorProjectLog)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:04
 */

@Slf4j
@Validated
@RestController
@Api(tags = "重大项目日志(MajorProjectLog)控制层")
@RequestMapping("/majorProjectLog")
@RequiredArgsConstructor
public class MajorProjectLogController {
    /**
     * 服务对象
     */
    @Autowired
    private MajorProjectLogService majorProjectLogService;

    @GetMapping
    public Rest<List<MajorProjectLog>> getAllMajorProjectLogs(MajorProjectLog majorProjectLog) {
        return Rest.data(majorProjectLogService.findMajorProjectLogs(majorProjectLog));
    }

    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    public FebsResponse MajorProjectLogList(QueryRequest request, MajorProjectLog.Params majorProjectLog) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(majorProjectLogService.findMajorProjectLogs(request, majorProjectLog));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    @OperateLog(mapper = MajorProjectLogMapper.class, className = MajorProjectLog.class, type = Operate.TYPE_ADD)

    public void addMajorProjectLog(@Valid MajorProjectLog majorProjectLog) throws FebsException {
        try {
            this.majorProjectLogService.createMajorProjectLog(majorProjectLog);
        } catch (FebsException e) {
            throw new FebsException(e);
        } catch (Exception e) {
            String message = "新增重大事项日志失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @DeleteMapping
    @OperateLog(mapper = MajorProjectLogMapper.class, className = MajorProjectLog.class, type = Operate.TYPE_DELETE)
    public void deleteMajorProjectLog(MajorProjectLog majorProjectLog) throws FebsException {
        try {
            this.majorProjectLogService.deleteMajorProjectLog(majorProjectLog);
        } catch (FebsException e) {
            throw new FebsException(e);
        } catch (Exception e) {
            String message = "删除重大项目日志失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    @OperateLog(mapper = MajorProjectLogMapper.class, className = MajorProjectLog.class, type = Operate.TYPE_MODIFY)
    public void updateMajorProjectLog(MajorProjectLog majorProjectLog) throws FebsException {
        try {
            this.majorProjectLogService.updateMajorProjectLog(majorProjectLog);
        } catch (FebsException e) {
            throw new FebsException(e);
        } catch (Exception e) {
            String message = "修改重大项目日志失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
