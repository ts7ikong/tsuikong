package cc.mrbird.febs.server.system.controller;


import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.system.TButton;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.system.service.TButtonService;
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
 * 按钮表(TButton)表控制层
 *
 * @author zlkj_cg
 * @since 2021-04-22 10:48:09
 */

@Slf4j
@Validated
@RestController
@Api(tags = "按钮表(TButton)控制层")
@RequestMapping("tButton")
@RequiredArgsConstructor
public class TButtonController {
    /**
     * 服务对象
     */
    @Autowired
    private TButtonService tButtonService;

    @GetMapping
    public Rest<List<TButton>> getAllTButtons(TButton tButton) {
        return Rest.data(tButtonService.findTButtons(tButton));
    }

    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    public FebsResponse TButtonList(QueryRequest request, TButton tButton) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(tButtonService.findTButtons(request, tButton));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    public void addTButton(@Valid TButton tButton) throws FebsException {
        try {
            this.tButtonService.createTButton(tButton);
        } catch (Exception e) {
            String message = "新增按钮表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @DeleteMapping
    public void deleteTButton(TButton tButton) throws FebsException {
        try {
            this.tButtonService.deleteTButton(tButton);
        } catch (Exception e) {
            String message = "删除按钮表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    public void updateTButton(TButton tButton) throws FebsException {
        try {
            this.tButtonService.updateTButton(tButton);
        } catch (Exception e) {
            String message = "修改按钮表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
