package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Overtime;
import cc.mrbird.febs.common.core.entity.tjdkxm.Parcel;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.mapper.OvertimeMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.ParcelMapper;
import cc.mrbird.febs.server.tjdkxm.service.ParcelService;
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
 * 分部表(Parcel)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:04
 */

@Slf4j
@Validated
@RestController
@Api(tags = "分部表(Parcel)控制层")
@RequestMapping("/parcel")
@RequiredArgsConstructor
public class ParcelController {
    /**
     * 服务对象
     */
    @Autowired
    private ParcelService parcelService;

    @GetMapping
    @ApiOperationSupport(ignoreParameters = {"subitemList[0]", "project"})
    public Rest<List<Parcel>> getAllParcels(Parcel parcel) throws FebsException {
        return Rest.data(parcelService.findParcels(parcel));
    }

    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @ApiOperationSupport(ignoreParameters = {"subitemList[0]", "project"})
    @GetMapping("list")
    public FebsResponse ParcelList(QueryRequest request, Parcel parcel) throws FebsException {
        Map<String, Object> dataTable = FebsUtil.getDataTable(parcelService.findParcels(request, parcel));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    @OperateLog(mapper = ParcelMapper.class, className = Parcel.class, type = Operate.TYPE_ADD)

    @ApiOperationSupport(ignoreParameters = {"subitemList[0]", "project"})
    public void addParcel(@Valid Parcel parcel) throws FebsException {
        try {
            this.parcelService.createParcel(parcel);
        } catch (FebsException e) {
            throw new FebsException(e.getMessage());
        } catch (Exception e) {
            String message = "新增分部表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @DeleteMapping
    @OperateLog(mapper = ParcelMapper.class, className = Parcel.class, type = Operate.TYPE_DELETE)

    @ApiOperationSupport(ignoreParameters = {"subitemList[0]", "project"})
    public void deleteParcel(Parcel parcel) throws FebsException {
        try {
            this.parcelService.deleteParcel(parcel);
        } catch (Exception e) {
            String message = "删除分部表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    @OperateLog(mapper = ParcelMapper.class, className = Parcel.class, type = Operate.TYPE_MODIFY)

    @ApiOperationSupport(ignoreParameters = {"subitemList[0]", "project"})
    public void updateParcel(Parcel parcel) throws FebsException {
        try {
            this.parcelService.updateParcel(parcel);
        } catch (Exception e) {
            String message = "修改分部表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
