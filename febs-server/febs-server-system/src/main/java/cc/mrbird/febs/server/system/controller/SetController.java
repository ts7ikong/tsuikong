package cc.mrbird.febs.server.system.controller;


import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.system.Set;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.system.service.SetService;
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
 * 广告信息表(Set)表控制层
 *
 * @author zlkj_cg
 * @since 2021-03-03 16:44:16
 */

@Slf4j
@Validated
@RestController
@Api(tags = "广告信息表(Set)控制层")
@RequestMapping("set")
@RequiredArgsConstructor
public class SetController {
    /**
     * 服务对象
     */
    @Autowired
    private SetService SetService;


    @GetMapping
    public Rest<List<Set>> getAllSets(Set Set) {
        return Rest.data(SetService.findSets(Set));
    }

    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    public FebsResponse SetList(QueryRequest request, Set Set) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(SetService.findSets(request, Set));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    public void addSet(@Valid Set Set) throws FebsException {
        try {
            this.SetService.createSet(Set);
        } catch (Exception e) {
            String message = "新增广告信息表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @DeleteMapping
    public void deleteSet(Set Set) throws FebsException {
        try {
            this.SetService.deleteSet(Set);
        } catch (Exception e) {
            String message = "删除广告信息表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    public void updateSet(Set Set) throws FebsException {
        log.warn("Set为：{}", Set);
        try {
            this.SetService.updateSet(Set);
        } catch (Exception e) {
            String message = "修改广告信息表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
