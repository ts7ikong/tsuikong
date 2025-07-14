package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Datamanagement;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.mapper.DatamanagementMapper;
import cc.mrbird.febs.server.tjdkxm.service.DatamanagementService;
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
import java.util.Set;

/**
 * 资料管理(Datamanagement)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:03
 */

@Slf4j
@Validated
@RestController
@Api(tags = "资料管理(Datamanagement)控制层")
@RequestMapping("/datamanagement")
@RequiredArgsConstructor
public class DatamanagementController {
    /**
     * 服务对象
     */
    @Autowired
    private DatamanagementService datamanagementService;

    @GetMapping
    public Rest<List<Datamanagement>> getAllDatamanagements(Datamanagement datamanagement) {
        return Rest.data(datamanagementService.findDatamanagements(datamanagement));
    }

    @ApiOperation(value = "分页查询 --资料", notes = "暂无查询条件")
    @GetMapping("list")
    public FebsResponse DatamanagementList(QueryRequest request, Datamanagement.Params datamanagement) {
        Map<String, Object> dataTable =
            FebsUtil.getDataTable(datamanagementService.findDatamanagements(request, datamanagement));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "分页查询 --模板", notes = "暂无查询条件")
    @GetMapping("temp/list")
    public FebsResponse DatamanagementTempList(QueryRequest request, Datamanagement.Params datamanagement) {
        Map<String, Object> dataTable =
            FebsUtil.getDataTable(datamanagementService.findDatamanagementTemps(request, datamanagement));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    @OperateLog(mapper = DatamanagementMapper.class, className = Datamanagement.class, type = Operate.TYPE_ADD)
    public void addDatamanagement(@Valid Datamanagement datamanagement) throws FebsException {
        try {
            this.datamanagementService.createDatamanagement(datamanagement);
        } catch (Exception e) {
            String message = "新增资料管理失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "新增接口--模板", notes = "只执行新增，后端不进行任何处理")
    @PostMapping("temp")
    @OperateLog(mapper = DatamanagementMapper.class, className = Datamanagement.class, type = Operate.TYPE_ADD)

    public void addDatamanagementTemp(@RequestBody List<Datamanagement.Add> datamanagements) throws FebsException {
        try {
            this.datamanagementService.createDatamanagementTemp(datamanagements);
        } catch (Exception e) {
            String message = "新增资料管理失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "新增接口[多个]", notes = "只执行新增，后端不进行任何处理")
    @PostMapping("add/list")
    @OperateLog(mapper = DatamanagementMapper.class, className = Datamanagement.class, type = Operate.TYPE_ADD)
    public void addDatamanagement(@RequestBody List<Datamanagement.Add> datamanagements) throws FebsException {
        try {
            this.datamanagementService.createDatamanagement(datamanagements);
        } catch (Exception e) {
            String message = "新增资料管理失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @DeleteMapping
    @OperateLog(mapper = DatamanagementMapper.class, className = Datamanagement.class, type = Operate.TYPE_DELETE)

    public void deleteDatamanagement(Datamanagement datamanagement) throws FebsException {
        try {
            this.datamanagementService.deleteDatamanagement(datamanagement);
        } catch (Exception e) {
            if (e instanceof FebsException) {
                throw new FebsException(e.getMessage());
            }
            String message = "删除资料管理失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口 多", notes = "根据datamanagementIds删除多条数据，只执行删除操作")
    @DeleteMapping("ids")
    @OperateLog(mapper = DatamanagementMapper.class, className = Datamanagement.class, type = Operate.TYPE_DELETE)

    public void deleteDatamanagements(String datamanagementIds) throws FebsException {
        try {
            this.datamanagementService.deleteDatamanagements(datamanagementIds);
        } catch (Exception e) {
            if (e instanceof FebsException) {
                throw new FebsException(e.getMessage());
            }
            String message = "删除资料管理失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    @OperateLog(mapper = DatamanagementMapper.class, className = Datamanagement.class, type = Operate.TYPE_MODIFY)

    public void updateDatamanagement(Datamanagement datamanagement) throws FebsException {
        try {
            this.datamanagementService.updateDatamanagement(datamanagement);
        } catch (Exception e) {
            String message = "修改资料管理失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
