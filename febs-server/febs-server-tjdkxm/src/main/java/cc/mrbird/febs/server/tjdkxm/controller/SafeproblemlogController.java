package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Safeproblem;
import cc.mrbird.febs.common.core.entity.tjdkxm.Safeproblemlog;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.mapper.SafeproblemMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.SafeproblemlogMapper;
import cc.mrbird.febs.server.tjdkxm.service.SafeproblemlogService;
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
 * 安全隐患清单(Safeproblemlog)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:07
 */

@Slf4j
@Validated
@RestController
@Api(tags = "安全隐患清单(Safeproblemlog)控制层")
@RequestMapping("/safeproblemlog")
@RequiredArgsConstructor
public class SafeproblemlogController {
    /**
     * 服务对象
     */
    @Autowired
    private SafeproblemlogService safeproblemlogService;

    @GetMapping
    public Rest<List<Safeproblemlog>> getAllSafeproblemlogs(Safeproblemlog safeproblemlog) {
        return Rest.data(safeproblemlogService. findSafeproblemlogs(safeproblemlog));
    }
    
    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    public FebsResponse SafeproblemlogList(QueryRequest request, Safeproblemlog safeproblemlog) {
        Map<String, Object> dataTable = FebsUtil.getDataTable( safeproblemlogService. findSafeproblemlogs(request,safeproblemlog));
        return new FebsResponse().data(dataTable);
    }
    
    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    @OperateLog(mapper = SafeproblemlogMapper.class, className = Safeproblemlog.class, type = Operate.TYPE_ADD)
    public void addSafeproblemlog(@Valid Safeproblemlog safeproblemlog, Safeproblem safeprobleml) throws FebsException {
        try {
            this.safeproblemlogService. createSafeproblemlog(safeproblemlog,safeprobleml);
        } catch (Exception e) {
            String message = "新增安全隐患清单失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @DeleteMapping
    @OperateLog(mapper = SafeproblemlogMapper.class, className = Safeproblemlog.class, type = Operate.TYPE_DELETE)

    public void deleteSafeproblemlog(Safeproblemlog safeproblemlog) throws FebsException {
        try {
            this.safeproblemlogService. deleteSafeproblemlog(safeproblemlog);
        } catch (Exception e) {
            String message = "删除安全隐患清单失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
    
    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    @OperateLog(mapper = SafeproblemlogMapper.class, className = Safeproblemlog.class, type = Operate.TYPE_MODIFY)
    public void updateSafeproblemlog(Safeproblemlog safeproblemlog) throws FebsException {
        try {
            this.safeproblemlogService. updateSafeproblemlog(safeproblemlog);
        } catch (Exception e) {
            String message = "修改安全隐患清单失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
    
}
