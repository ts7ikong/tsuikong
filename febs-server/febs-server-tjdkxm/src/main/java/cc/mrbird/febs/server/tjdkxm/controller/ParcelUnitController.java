package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdkxm.ParcelUnit;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.mapper.ParcelUnitMapper;
import cc.mrbird.febs.server.tjdkxm.service.ParcelUnitService;
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
 * 分部表(ParcelUnit)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:04
 */

@Slf4j
@Validated
@RestController
@Api(tags = "分部单位表(ParcelUnit)控制层")
@RequestMapping("/parcelunit")
@RequiredArgsConstructor
public class ParcelUnitController {
    /**
     * 服务对象
     */
    @Autowired
    private ParcelUnitService parcelUnitService;

    @GetMapping
    public Rest<List<ParcelUnit>> getAllParcelUnits(ParcelUnit parcelUnit) {
        return Rest.data(parcelUnitService.findParcelUnits(parcelUnit));
    }

    @GetMapping("all")
    @ApiOperation(value = "查询所有 用于选择框", notes = " id name ")
    public FebsResponse getAllParcelUnitMaps() {
        return new FebsResponse().data(parcelUnitService.findParcelUnits());
    }

    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    public FebsResponse parcelUnitList(QueryRequest request, ParcelUnit parcelUnit) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(parcelUnitService.findParcelUnits(request, parcelUnit));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    @OperateLog(mapper = ParcelUnitMapper.class, className = ParcelUnit.class, type = Operate.TYPE_ADD)

    public void addParcelUnit(@Valid @RequestBody ParcelUnit parcelUnit) throws FebsException {
        try {
            this.parcelUnitService.createParcelUnit(parcelUnit);
        } catch (Exception e) {
            String message = "新增分部单位表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @DeleteMapping
    @OperateLog(mapper = ParcelUnitMapper.class, className = ParcelUnit.class, type = Operate.TYPE_DELETE)

    public void deleteParcelUnit(ParcelUnit parcelUnit) throws FebsException {
        try {
            this.parcelUnitService.deleteParcelUnit(parcelUnit);
        } catch (Exception e) {
            String message = "删除分部单位表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    @OperateLog(mapper = ParcelUnitMapper.class, className = ParcelUnit.class, type = Operate.TYPE_MODIFY)

    public void updateParcelUnit(@RequestBody ParcelUnit parcelUnit) throws FebsException {
        try {
            this.parcelUnitService.updateParcelUnit(parcelUnit);
        } catch (Exception e) {
            String message = "修改分部单位表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
