package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.tjdkxm.Qualityproblem;
import cc.mrbird.febs.common.core.entity.tjdkxm.Qualityproblemlog;
import cc.mrbird.febs.server.tjdkxm.mapper.QualityproblemMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.QualityproblemlogMapper;
import cc.mrbird.febs.server.tjdkxm.service.QualityproblemlogService;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import java.util.Map;

import io.swagger.annotations.ApiOperation;

/**
 * 质量问题清单(Qualityproblemlog)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:02
 */

@Slf4j
@Validated
@RestController
@Api(tags = "质量问题清单(Qualityproblemlog)控制层")
@RequestMapping("/qualityproblemlog")
@RequiredArgsConstructor
public class QualityproblemlogController {
    /**
     * 服务对象
     */
    @Autowired
    private QualityproblemlogService qualityproblemlogService;

    @GetMapping
    public FebsResponse getAllQualityproblemlogs(Qualityproblemlog qualityproblemlog) {
        return new FebsResponse().data(qualityproblemlogService.findQualityproblemlogs(qualityproblemlog));
    }

    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    public FebsResponse QualityproblemlogList(QueryRequest request, Qualityproblemlog qualityproblemlog) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(qualityproblemlogService.findQualityproblemlogs(request, qualityproblemlog));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    @OperateLog(mapper = QualityproblemlogMapper.class, className = Qualityproblemlog.class, type = Operate.TYPE_ADD)
    public void addQualityproblemlog(@Valid Qualityproblemlog qualityproblemlog, Qualityproblem qualityproblem) throws FebsException {
        try {
            if (qualityproblemlog.getQualityproblenId() != null) {
                this.qualityproblemlogService.createQualityproblemlog(qualityproblemlog, qualityproblem);
            }
            throw new FebsException("");
        } catch (Exception e) {
            String message = "质量问题清单失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @DeleteMapping
    @OperateLog(mapper = QualityproblemlogMapper.class, className = Qualityproblemlog.class, type = Operate.TYPE_DELETE)
    public void deleteQualityproblemlog(Qualityproblemlog qualityproblemlog) throws FebsException {
        try {
            this.qualityproblemlogService.deleteQualityproblemlog(qualityproblemlog);
        } catch (Exception e) {
            String message = "删除质量问题清单失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    @OperateLog(mapper = QualityproblemlogMapper.class, className = Qualityproblemlog.class, type = Operate.TYPE_MODIFY)
    public void updateQualityproblemlog(Qualityproblemlog qualityproblemlog) throws FebsException {
        try {
            this.qualityproblemlogService.updateQualityproblemlog(qualityproblemlog);
        } catch (Exception e) {
            String message = "修改质量问题清单失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
