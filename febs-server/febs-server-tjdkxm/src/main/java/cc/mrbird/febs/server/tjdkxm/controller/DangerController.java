package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdkxm.ConferenceThere;
import cc.mrbird.febs.common.core.entity.tjdkxm.Danger;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.mapper.ConferenceThereMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.DangerMapper;
import cc.mrbird.febs.server.tjdkxm.service.DangerService;
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
 * 重大危险源记录表(Danger)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:02
 */

@Slf4j
@Validated
@RestController
@Api(tags = "重大危险源记录表(Danger)控制层")
@RequestMapping("/danger")
@RequiredArgsConstructor
public class DangerController {
    /**
     * 服务对象
     */
    @Autowired
    private DangerService dangerService;


    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @ApiOperationSupport(ignoreParameters = {"parcel", "subitem"})
    @GetMapping("list")
    public FebsResponse DangerList(QueryRequest request, Danger.Params danger) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(dangerService.findDangers(request, danger));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    @OperateLog(mapper = DangerMapper.class, className = Danger.class, type = Operate.TYPE_ADD)
    @ApiOperationSupport(ignoreParameters = {"parcel", "subitem"})
    public void addDanger(@Valid Danger danger) throws FebsException {
        try {
            this.dangerService.createDanger(danger);
        } catch (Exception e) {
            String message = "新增重大危险源记录表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @ApiOperationSupport(ignoreParameters = {"parcel", "subitem"})
    @DeleteMapping
    @OperateLog(mapper = DangerMapper.class, className = Danger.class, type = Operate.TYPE_DELETE)
    public void deleteDanger(Danger danger) throws FebsException {
        try {
            this.dangerService.deleteDanger(danger);
        } catch (Exception e) {
            String message = "删除重大危险源记录表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @ApiOperationSupport(ignoreParameters = {"parcel", "subitem"})
    @PutMapping
    @OperateLog(mapper = DangerMapper.class, className = Danger.class, type = Operate.TYPE_MODIFY)
    public void updateDanger(Danger danger) throws FebsException {
        try {
            this.dangerService.updateDanger(danger);
        } catch (Exception e) {
            String message = "修改重大危险源记录表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
