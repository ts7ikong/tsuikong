package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Sgrz;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.mapper.SgrzMapper;
import cc.mrbird.febs.server.tjdkxm.service.SgrzService;
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
 * 施工日志表(Sgrz)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:07
 */

@Slf4j
@Validated
@RestController
@Api(tags = "施工日志表(Sgrz)控制层")
@RequestMapping("/sgrz")
@RequiredArgsConstructor
public class SgrzController {
    /**
     * 服务对象
     */
    @Autowired
    private SgrzService sgrzService;

    @GetMapping
    public Rest<List<Sgrz>> getAllSgrzs(Sgrz sgrz) {
        return Rest.data(sgrzService. findSgrzs(sgrz));
    }
    
    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    public FebsResponse SgrzList(QueryRequest request, Sgrz sgrz) {
        Map<String, Object> dataTable = FebsUtil.getDataTable( sgrzService.findSgrzs(request,sgrz));
        return new FebsResponse().data(dataTable);
    }
    
    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    @OperateLog(mapper = SgrzMapper.class, className = Sgrz.class, type = Operate.TYPE_ADD)

    public void addSgrz(@Valid Sgrz sgrz) throws FebsException {
        try {
            this.sgrzService. createSgrz(sgrz);
        } catch (Exception e) {
            String message = "新增施工日志表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @DeleteMapping
    @OperateLog(mapper = SgrzMapper.class, className = Sgrz.class, type = Operate.TYPE_DELETE)

    public void deleteSgrz(Sgrz sgrz) throws FebsException {
        try {
            this.sgrzService. deleteSgrz(sgrz);
        } catch (Exception e) {
            String message = "删除施工日志表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
    
    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    @OperateLog(mapper = SgrzMapper.class, className = Sgrz.class, type = Operate.TYPE_MODIFY)
    public void updateSgrz(Sgrz sgrz) throws FebsException {
        try {
            this.sgrzService. updateSgrz(sgrz);
        } catch (Exception e) {
            String message = "修改施工日志表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
    
}
