package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Safeplan;
import cc.mrbird.febs.common.core.entity.tjdkxm.Safeproblem;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.mapper.SafeplanMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.SafeproblemMapper;
import cc.mrbird.febs.server.tjdkxm.service.SafeproblemService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 安全隐患清单(Safeproblem)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:04
 */

@Slf4j
@Validated
@RestController
@Api(tags = "安全隐患清单(Safeproblem)控制层")
@RequestMapping("/safeproblem")
@RequiredArgsConstructor
public class SafeproblemController {
    /**
     * 服务对象
     */
    @Autowired
    private SafeproblemService safeproblemService;

    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @ApiOperationSupport(ignoreParameters = {"parcel", "subitem"})
    @GetMapping("list")
    public FebsResponse safeproblemList(QueryRequest request, Safeproblem.Params  safeproblem) throws FebsException {
        Map<String, Object> dataTable = FebsUtil.getDataTable(safeproblemService.findSafeproblems(request,
                safeproblem, 0));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @ApiOperationSupport(ignoreParameters = {"parcel", "subitem"})
    @PostMapping
    @OperateLog(mapper = SafeproblemMapper.class, className = Safeproblem.class, type = Operate.TYPE_ADD)
    public void addSafeproblem(@Valid @RequestBody Safeproblem safeproblem) throws FebsException {
        try {
            this.safeproblemService.createSafeproblem(safeproblem);
        } catch (Exception e) {
            String message = "新增安全隐患清单失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @ApiOperationSupport(ignoreParameters = {"parcel", "subitem"})
    @DeleteMapping
    @OperateLog(mapper = SafeproblemMapper.class, className = Safeproblem.class, type = Operate.TYPE_DELETE)
    public void deleteSafeproblem(Safeproblem safeproblem) throws FebsException {
        try {
            this.safeproblemService.deleteSafeproblem(safeproblem);
        } catch (Exception e) {
            String message = "删除安全隐患清单失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @ApiOperationSupport(ignoreParameters = {"parcel", "subitem"})
    @PutMapping
    @OperateLog(mapper = SafeproblemMapper.class, className = Safeproblem.class, type = Operate.TYPE_MODIFY)

    public void updateSafeproblem(@RequestBody Safeproblem safeproblem) throws FebsException {
        try {
            this.safeproblemService.updateSafeproblem(safeproblem);
        } catch (Exception e) {
            String message = "修改安全隐患清单失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "安全隐患整改记录分页查询", notes = "暂无查询条件")
    @ApiOperationSupport(ignoreParameters = {"parcel", "subitem"})
    @GetMapping("rect/list")
    public FebsResponse safeproblemRectList(QueryRequest request, Safeproblem.Params  safeproblem) {
        //验收通过的
        safeproblem.setSafeproblenState("4");
        Map<String, Object> dataTable = FebsUtil.getDataTable(safeproblemService.safeproblemRectList(request,
                safeproblem, 1));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "安全隐患整改记录删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @ApiOperationSupport(ignoreParameters = {"parcel", "subitem"})
    @DeleteMapping("rect")
    @OperateLog(mapper = SafeproblemMapper.class, className = Safeproblem.class, type = Operate.TYPE_DELETE)
    public void deleteSafeproblemRectList(Safeproblem safeproblem) throws FebsException {
        try {
            this.safeproblemService.deleteSafeproblem(safeproblem);
        } catch (Exception e) {
            String message = "删除安全隐患问题清单失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "大屏数据", notes = "除待整改")
    @GetMapping("screen")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "全部 1 我的 2", required = true, dataType = "String"),
            @ApiImplicitParam(name = "date", value = "统计日期 按年 YYYY 按月 YYYY-DD", required = true, dataType = "String")
    })
    public FebsResponse bigScreen(String type, String date) throws FebsException {
        try {
            return new FebsResponse().data(safeproblemService.bigScreen(type, date));
        } catch (FebsException e1) {
            throw new FebsException(e1.getMessage());
        } catch (Exception e) {
            String message = "查询数据错误";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
