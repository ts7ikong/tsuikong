package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Reporting;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.mapper.ReportingMapper;
import cc.mrbird.febs.server.tjdkxm.service.ReportingService;
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
 * 工作汇报审批表(Reporting)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:05
 */

@Slf4j
@Validated
@RestController
@Api(tags = "工作汇报审批表(Reporting)控制层")
@RequestMapping("/reporting")
@RequiredArgsConstructor
public class ReportingController {
    /**
     * 服务对象
     */
    @Autowired
    private ReportingService reportingService;

    @GetMapping
    public Rest<List<Reporting>> getAllReportings(Reporting reporting) {
        return Rest.data(reportingService.findReportings(reporting));
    }

    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    public FebsResponse ReportingList(QueryRequest request, Reporting reporting) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(reportingService.findReportings(request, reporting));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "分页查询 审批", notes = "暂无查询条件")
    @GetMapping("list/approval")
    public FebsResponse ReportingListApproval(QueryRequest request, Reporting reporting) throws FebsException {
        Map<String, Object> dataTable =
            FebsUtil.getDataTable(reportingService.findReportingsApproval(request, reporting));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    @OperateLog(mapper = ReportingMapper.class, className = Reporting.class, type = Operate.TYPE_ADD)

    public void addReporting(@Valid Reporting reporting) throws FebsException {
        try {
            this.reportingService.createReporting(reporting);
        } catch (Exception e) {
            String message = "新增工作汇报审批表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @DeleteMapping
    @OperateLog(mapper = ReportingMapper.class, className = Reporting.class, type = Operate.TYPE_MODIFY)
    public void deleteReporting(Reporting reporting) throws FebsException {
        try {
            this.reportingService.deleteReporting(reporting);
        } catch (FebsException e) {
            throw new FebsException(e.getMessage());
        } catch (Exception e) {
            String message = "删除工作汇报审批表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    @OperateLog(mapper = ReportingMapper.class, className = Reporting.class, type = Operate.TYPE_MODIFY)
    public void updateReporting(Reporting reporting) throws FebsException {
        try {
            this.reportingService.updateReporting(reporting, false);
        } catch (FebsException e) {
            throw new FebsException(e.getMessage());
        } catch (Exception e) {
            String message = "修改工作汇报审批表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "审批接口", notes = "根据id修改一条数据审批")
    @PutMapping("approval")
    public void updateReportingApproval(Reporting reporting) throws FebsException {
        try {
            this.reportingService.updateReporting(reporting, true);
        } catch (FebsException e) {
            throw new FebsException(e.getMessage());
        } catch (Exception e) {
            String message = "修改工作汇报审批表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
