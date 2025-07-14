package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Askfleave;
import cc.mrbird.febs.common.core.entity.tjdkxm.Qualityplan;
import cc.mrbird.febs.common.core.entity.tjdkxm.Safeplan;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.mapper.AskfleaveMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.QualityplanMapper;
import cc.mrbird.febs.server.tjdkxm.service.QualityplanService;
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
 * 质量检查计划(Qualityplan)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:02
 */

@Slf4j
@Validated
@RestController
@Api(tags = "质量检查计划(Qualityplan)控制层")
@RequestMapping("/qualityplan")
@RequiredArgsConstructor
public class QualityplanController {
    /**
     * 服务对象
     */
    @Autowired
    private QualityplanService qualityplanService;

    @GetMapping
    @ApiOperationSupport(ignoreParameters = {"parcel", "subitem"})
    public Rest<List<Qualityplan>> getAllQualityplans(Qualityplan qualityplan) {
        return Rest.data(qualityplanService.findQualityplans(qualityplan));
    }

    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @ApiOperationSupport(ignoreParameters = {"parcel", "subitem"})
    @GetMapping("list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "全部代实施 0; 全部待验收 1; 我的代实施 2;我的代验收 3;全部 4 ;我的全部 5", required = false,
                    dataType =
                    "String"),
    })
    public FebsResponse QualityplanList(QueryRequest request, Qualityplan.Params qualityplan, String type) throws FebsException {
        Map<String, Object> dataTable = FebsUtil.getDataTable(qualityplanService.findQualityplans(request,
                qualityplan, type));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "分页查询-检查记录", notes = "暂无查询条件")
    @GetMapping("list/record")
    @ApiOperationSupport(ignoreParameters = {"parcel", "subitem"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "全部 0; 我的新增 1; 我的实施 2; 我的验收 3", required = false, dataType =
                    "String"),
    })
    public FebsResponse safeplanRecordList(QueryRequest request, Qualityplan.Params safeplan, String type) throws FebsException {
        Map<String, Object> dataTable = FebsUtil.getDataTable(qualityplanService.findSafeplanRecords(request, safeplan,
                type));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @ApiOperationSupport(ignoreParameters = {"parcel", "subitem"})
    @PostMapping
    @OperateLog(mapper = QualityplanMapper.class, className = Qualityplan.class, type = Operate.TYPE_ADD)
    public void addQualityplan(@Valid @RequestBody Qualityplan qualityplan) throws FebsException {
        try {
            this.qualityplanService.createQualityplan(qualityplan);
        } catch (FebsException e1) {
            throw new FebsException(e1.getMessage());
        } catch (Exception e) {
            String message = "新增质量检查计划失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @ApiOperationSupport(ignoreParameters = {"parcel", "subitem"})
    @DeleteMapping
    @OperateLog(mapper = QualityplanMapper.class, className = Qualityplan.class, type = Operate.TYPE_DELETE)
    public void deleteQualityplan(Qualityplan qualityplan) throws FebsException {
        try {
            this.qualityplanService.deleteQualityplan(qualityplan);
        } catch (Exception e) {
            String message = "删除质量检查计划失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @ApiOperationSupport(ignoreParameters = {"parcel", "subitem"})
    @PutMapping
    @OperateLog(mapper = QualityplanMapper.class, className = Qualityplan.class, type = Operate.TYPE_MODIFY)
    public void updateQualityplan(@RequestBody Qualityplan qualityplan) throws FebsException {
        try {
            this.qualityplanService.updateQualityplan(qualityplan);
        } catch (Exception e) {
            String message = "修改质量检查计划失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
