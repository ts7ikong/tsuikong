package cc.mrbird.febs.server.tjdk.controller;

import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdk.Sysnotify;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdk.mapper.SysnotifyMapper;
import cc.mrbird.febs.server.tjdk.service.SysnotifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * 系统通知(Sysnotify)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:08
 */

@Slf4j
@Validated
@RestController
@Api(tags = "系统通知(Sysnotify)控制层")
@RequestMapping("/sysnotify")
@RequiredArgsConstructor
public class SysnotifyController {
    /**
     * 服务对象
     */
    @Autowired
    private SysnotifyService sysnotifyService;

    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    public FebsResponse sysnotifyList(QueryRequest request, Sysnotify.Params sysnotify) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(sysnotifyService.findSysnotifys(request, sysnotify));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    @OperateLog(mapper = SysnotifyMapper.class, className = Sysnotify.class, type = Operate.TYPE_ADD)

    public void addSysnotify(@Valid @RequestBody Sysnotify sysnotify) throws FebsException {
        try {
            this.sysnotifyService.createSysnotify(sysnotify);
        } catch (Exception e) {
            String message = "新增系统通知失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @DeleteMapping
    @OperateLog(mapper = SysnotifyMapper.class, className = Sysnotify.class, type = Operate.TYPE_DELETE)

    public void deleteSysnotify(Sysnotify sysnotify) throws FebsException {
        try {
            this.sysnotifyService.deleteSysnotify(sysnotify);
        } catch (Exception e) {
            String message = "删除系统通知失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    @OperateLog(mapper = SysnotifyMapper.class, className = Sysnotify.class, type = Operate.TYPE_DELETE)

    public void updateSysnotify(@RequestBody Sysnotify sysnotify) throws FebsException {
        try {
            this.sysnotifyService.updateSysnotify(sysnotify);
        } catch (Exception e) {
            String message = "修改系统通知失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
