package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdkxm.UnitEngine;
import cc.mrbird.febs.common.core.entity.tjdkxm.Zzaqglryrz;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.mapper.UnitEngineMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.ZzaqglryrzMapper;
import cc.mrbird.febs.server.tjdkxm.service.ZzaqglryrzService;
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
 * 专职安全管理人员日志表(Zzaqglryrz)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:08
 */

@Slf4j
@Validated
@RestController
@Api(tags = "专职安全管理人员日志表(Zzaqglryrz)控制层")
@RequestMapping("/zzaqglryrz")
@RequiredArgsConstructor
public class ZzaqglryrzController {
    /**
     * 服务对象
     */
    @Autowired
    private ZzaqglryrzService zzaqglryrzService;

    @GetMapping
    public Rest<List<Zzaqglryrz>> getAllZzaqglryrzs(Zzaqglryrz zzaqglryrz) {
        return Rest.data(zzaqglryrzService.findZzaqglryrzs(zzaqglryrz));
    }

    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    public FebsResponse ZzaqglryrzList(QueryRequest request, Zzaqglryrz zzaqglryrz) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(zzaqglryrzService.findZzaqglryrzs(request, zzaqglryrz));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    @OperateLog(mapper = ZzaqglryrzMapper.class, className = Zzaqglryrz.class, type = Operate.TYPE_ADD)
    public void addZzaqglryrz(@Valid @RequestBody Zzaqglryrz zzaqglryrz) throws FebsException {
        try {
            this.zzaqglryrzService.createZzaqglryrz(zzaqglryrz);
        } catch (Exception e) {
            String message = "新增专职安全管理人员日志表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @DeleteMapping
    @OperateLog(mapper = ZzaqglryrzMapper.class, className = Zzaqglryrz.class, type = Operate.TYPE_DELETE)
    public void deleteZzaqglryrz(Zzaqglryrz zzaqglryrz) throws FebsException {
        try {
            this.zzaqglryrzService.deleteZzaqglryrz(zzaqglryrz);
        } catch (Exception e) {
            String message = "删除专职安全管理人员日志表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    @OperateLog(mapper = ZzaqglryrzMapper.class, className = Zzaqglryrz.class, type = Operate.TYPE_MODIFY)
    public void updateZzaqglryrz(@RequestBody Zzaqglryrz zzaqglryrz) throws FebsException {
        try {
            this.zzaqglryrzService.updateZzaqglryrz(zzaqglryrz);
        } catch (Exception e) {
            String message = "修改专职安全管理人员日志表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
