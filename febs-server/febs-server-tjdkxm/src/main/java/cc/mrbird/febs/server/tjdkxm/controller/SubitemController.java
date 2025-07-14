package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Subitem;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.mapper.SubitemMapper;
import cc.mrbird.febs.server.tjdkxm.service.SubitemService;
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
 * 分项表(Subitem)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:07
 */

@Slf4j
@Validated
@RestController
@Api(tags = "分项表(Subitem)控制层")
@RequestMapping("/subitem")
@RequiredArgsConstructor
public class SubitemController {
    /**
     * 服务对象
     */
    @Autowired
    private SubitemService subitemService;

    @GetMapping
    @ApiOperationSupport(ignoreParameters = {"parcel", "project"})
    public Rest<List<Subitem>> getAllSubitems(Subitem subitem) throws FebsException {
        return Rest.data(subitemService.findSubitems(subitem));
    }

    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    @ApiOperationSupport(ignoreParameters = {"parcel", "project"})
    public FebsResponse SubitemList(QueryRequest request, Subitem subitem) throws FebsException {
        Map<String, Object> dataTable = FebsUtil.getDataTable(subitemService.findSubitems(request, subitem));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperationSupport(ignoreParameters = {"parcel", "project"})
    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    @OperateLog(mapper = SubitemMapper.class, className = Subitem.class, type = Operate.TYPE_ADD)

    public void addSubitem(@Valid Subitem subitem) throws FebsException {
        try {
            this.subitemService.createSubitem(subitem);
        } catch (FebsException e) {
            throw new FebsException(e.getMessage());
        } catch (Exception e) {
            String message = "新增分项表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperationSupport(ignoreParameters = {"parcel", "project"})
    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @DeleteMapping
    @OperateLog(mapper = SubitemMapper.class, className = Subitem.class, type = Operate.TYPE_DELETE)

    public void deleteSubitem(Subitem subitem) throws FebsException {
        try {
            this.subitemService.deleteSubitem(subitem);
        } catch (Exception e) {
            String message = "删除分项表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    @OperateLog(mapper = SubitemMapper.class, className = Subitem.class, type = Operate.TYPE_MODIFY)

    @ApiOperationSupport(ignoreParameters = {"parcel", "project"})
    public void updateSubitem(Subitem subitem) throws FebsException {
        try {
            this.subitemService.updateSubitem(subitem);
        } catch (Exception e) {
            String message = "修改分项表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
