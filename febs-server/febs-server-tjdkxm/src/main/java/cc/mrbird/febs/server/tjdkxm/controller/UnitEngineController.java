package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdkxm.UnitEngine;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.mapper.UnitEngineMapper;
import cc.mrbird.febs.server.tjdkxm.service.UnitEngineService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
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
 * 单位工程(UnitEngine)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:07
 */

@Slf4j
@Validated
@RestController
@Api(tags = "单位项目(UnitEngine)控制层")
@RequestMapping("/unitEngine")
@RequiredArgsConstructor
public class UnitEngineController {
    /**
     * 服务对象
     */
    @Autowired
    private UnitEngineService unitEngineService;

    @GetMapping
    @ApiOperationSupport(ignoreParameters = {"parcel", "project"})
    public Rest<List<UnitEngine>> getAllUnitEngines(UnitEngine unitEngine) throws FebsException {
        return Rest.data(unitEngineService.findUnitEngines(unitEngine));
    }

    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    @ApiOperationSupport(ignoreParameters = {"parcel", "project"})
    public FebsResponse UnitEngineList(QueryRequest request, UnitEngine unitEngine) throws FebsException {
        Map<String, Object> dataTable = FebsUtil.getDataTable(unitEngineService.findUnitEngines(request, unitEngine));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperationSupport(ignoreParameters = {"parcel", "project"})
    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    @OperateLog(mapper = UnitEngineMapper.class, className = UnitEngine.class, type = Operate.TYPE_ADD)

    public void addUnitEngine(@Valid UnitEngine unitEngine) throws FebsException {
        try {
            this.unitEngineService.createUnitEngine(unitEngine);
        }catch (FebsException e){
            throw new FebsException(e);
        } catch (Exception e) {
            String message = "新增单位工程失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperationSupport(ignoreParameters = {"parcel", "project"})
    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @DeleteMapping
    @OperateLog(mapper = UnitEngineMapper.class, className = UnitEngine.class, type = Operate.TYPE_DELETE)

    public void deleteUnitEngine(UnitEngine unitEngine) throws FebsException {
        try {
            this.unitEngineService.deleteUnitEngine(unitEngine);
        } catch (Exception e) {
            String message = "删除单位工程失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    @OperateLog(mapper = UnitEngineMapper.class, className = UnitEngine.class, type = Operate.TYPE_MODIFY)

    @ApiOperationSupport(ignoreParameters = {"parcel", "project"})
    public void updateUnitEngine(UnitEngine unitEngine) throws FebsException {
        try {
            this.unitEngineService.updateUnitEngine(unitEngine);
        }catch (FebsException e){
            throw new FebsException(e);
        } catch (Exception e) {
            String message = "修改单位工程失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
