package cc.mrbird.febs.server.system.controller;


import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.system.TRoleButton;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.system.service.TRoleButtonService;
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
 * (TRoleButton)表控制层
 *
 * @author zlkj_cg
 * @since 2021-04-22 10:48:09
 */

@Slf4j
@Validated
@RestController
@Api(tags = "(TRoleButton)控制层")
@RequestMapping("tRoleButton")
@RequiredArgsConstructor
public class TRoleButtonController {
    /**
     * 服务对象
     */
    @Autowired
    private TRoleButtonService tRoleButtonService;

    @GetMapping
    public Rest<List<TRoleButton>> getAllTRoleButtons(TRoleButton tRoleButton) {
        return Rest.data(tRoleButtonService.findTRoleButtons(tRoleButton));
    }

    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    public FebsResponse TRoleButtonList(QueryRequest request, TRoleButton tRoleButton) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(tRoleButtonService.findTRoleButtons(request, tRoleButton));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    public void addTRoleButton(@Valid TRoleButton tRoleButton) throws FebsException {
        try {
            this.tRoleButtonService.createTRoleButton(tRoleButton);
        } catch (Exception e) {
            String message = "新增失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @DeleteMapping
    public void deleteTRoleButton(TRoleButton tRoleButton) throws FebsException {
        try {
            this.tRoleButtonService.deleteTRoleButton(tRoleButton);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    public void updateTRoleButton(TRoleButton tRoleButton) throws FebsException {
        try {
            this.tRoleButtonService.updateTRoleButton(tRoleButton);
        } catch (Exception e) {
            String message = "修改失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
