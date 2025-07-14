package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Qualityproblemlog;
import cc.mrbird.febs.common.core.entity.tjdkxm.Safeplan;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.mapper.QualityproblemlogMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.SafeplanMapper;
import cc.mrbird.febs.server.tjdkxm.service.SafeplanService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
 * 安全检查计划(Safeplan)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:03
 */

@Slf4j
@Validated
@RestController
@Api(tags = "安全检查计划(Safeplan)控制层")
@RequestMapping("/safeplan")
@RequiredArgsConstructor
public class SafeplanController {
    /**
     * 服务对象
     */
    @Autowired
    private SafeplanService safeplanService;

    @GetMapping
    @ApiOperationSupport(ignoreParameters = {"parcel", "subitem"})
    public Rest<List<Safeplan>> getAllSafeplans(Safeplan safeplan) {
        return Rest.data(safeplanService.findSafeplans(safeplan));
    }

    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    @ApiOperationSupport(ignoreParameters = {"parcel", "subitem"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "全部代实施 0; 全部待验收 1; 我的代实施 2;我的代验收 3;全部 4 ;我的全部 5", required = false, dataType =
                    "String"),
    })
    public FebsResponse safeplanList(QueryRequest request, Safeplan.Params safeplan, String type) throws FebsException {
        Map<String, Object> dataTable = FebsUtil.getDataTable(safeplanService.findSafeplans(request, safeplan, type));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "分页查询-检查记录", notes = "暂无查询条件")
    @GetMapping("list/record")
    @ApiOperationSupport(ignoreParameters = {"parcel", "subitem"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "全部 0; 我的新增 1; 我的实施 2; 我的验收 3", required = false, dataType =
                    "String"),
    })
    public FebsResponse safeplanRecordList(QueryRequest request, Safeplan.Params safeplan, String type) throws FebsException {
        Map<String, Object> dataTable = FebsUtil.getDataTable(safeplanService.findSafeplanRecords(request, safeplan,
                type));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperationSupport(ignoreParameters = {"parcel", "subitem"})
    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    @OperateLog(mapper = SafeplanMapper.class, className = Safeplan.class, type = Operate.TYPE_ADD)
    public void addSafeplan(@Valid @RequestBody Safeplan safeplan) throws FebsException {
        try {
            this.safeplanService.createSafeplan(safeplan);
        } catch (FebsException e1) {
            throw new FebsException(e1.getMessage());
        } catch (Exception e) {
            String message = "新增安全检查计划失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperationSupport(ignoreParameters = {"parcel", "subitem"})
    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @DeleteMapping
    @OperateLog(mapper = SafeplanMapper.class, className = Safeplan.class, type = Operate.TYPE_DELETE)
    public void deleteSafeplan(Safeplan safeplan) throws FebsException {
        try {
            this.safeplanService.deleteSafeplan(safeplan);
        } catch (Exception e) {
            String message = "删除安全检查计划失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperationSupport(ignoreParameters = {"parcel", "subitem"})
    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    @OperateLog(mapper = SafeplanMapper.class, className = Safeplan.class, type = Operate.TYPE_MODIFY)
    public void updateSafeplan(@RequestBody Safeplan safeplan) throws FebsException {
        try {
            this.safeplanService.updateSafeplan(safeplan);
        } catch (Exception e) {
            String message = "修改安全检查计划失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
