package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Partylearn;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.mapper.PartylearnMapper;
import cc.mrbird.febs.server.tjdkxm.service.PartylearnService;
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
 * 党建学习表(Partylearn)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:04
 */

@Slf4j
@Validated
@RestController
@Api(tags = "党建中心表(Partylearn)控制层")
@RequestMapping("/partylearn")
@RequiredArgsConstructor
public class PartylearnController {
    /**
     * 服务对象
     */
    @Autowired
    private PartylearnService partylearnService;

    @GetMapping
    public Rest<List<Partylearn>> getAllPartylearns(Partylearn partylearn) {
        return Rest.data(partylearnService.findPartylearns(partylearn));
    }

    @ApiOperation(value = "弃用--分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    @Deprecated
    public FebsResponse partylearnList(QueryRequest request, Partylearn partylearn) {
        partylearn.setPartylearnType(1);
        Map<String, Object> dataTable = FebsUtil.getDataTable(partylearnService.findPartylearns(request, partylearn));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "分页查询--党建资料", notes = "暂无查询条件")
    @GetMapping("list/ziliao")
    public FebsResponse partylearnZiliaoList(QueryRequest request, Partylearn partylearn) {
        partylearn.setPartylearnType(1);
        Map<String, Object> dataTable = FebsUtil.getDataTable(partylearnService.findPartylearns(request, partylearn));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "分页查询--宣传学习", notes = "暂无查询条件")
    @GetMapping("list/xuexi")
    public FebsResponse partylearnXuexiList(QueryRequest request, Partylearn partylearn) {
        partylearn.setPartylearnType(2);
        Map<String, Object> dataTable = FebsUtil.getDataTable(partylearnService.findPartylearns(request, partylearn));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "分页查询--综合治理", notes = "暂无查询条件")
    @GetMapping("list/zhili")
    public FebsResponse partylearnZhiliList(QueryRequest request, Partylearn partylearn) {
        partylearn.setPartylearnType(3);
        Map<String, Object> dataTable = FebsUtil.getDataTable(partylearnService.findPartylearns(request, partylearn));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "新增接口--党建资料", notes = "只执行新增，后端不进行任何处理")
    @ApiOperationSupport(ignoreParameters = {"learnRecords"})
    @PostMapping("ziliao")
    @OperateLog(mapper = PartylearnMapper.class, className = Partylearn.class, type = "新增-党建资料")
    public void addPartylearnZiliao(@Valid @RequestBody Partylearn partylearn) throws FebsException {
        try {
            partylearn.setPartylearnType(1);
            this.partylearnService.createPartylearn(partylearn);
        } catch (FebsException e) {
            throw new FebsException(e.getMessage());
        } catch (Exception e) {
            String message = "新增党建学习表失败";
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "新增接口--宣传学习", notes = "只执行新增，后端不进行任何处理")
    @ApiOperationSupport(ignoreParameters = {"learnRecords"})
    @PostMapping("xuexi")
    @OperateLog(mapper = PartylearnMapper.class, className = Partylearn.class, type = "新增-宣传学习")

    public void addPartylearnXuexi(@Valid @RequestBody Partylearn partylearn) throws FebsException {
        try {
            partylearn.setPartylearnType(2);
            this.partylearnService.createPartylearn(partylearn);
        } catch (FebsException e) {
            throw new FebsException(e.getMessage());
        } catch (Exception e) {
            String message = "新增党建学习表失败";
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "新增接口--综合治理", notes = "只执行新增，后端不进行任何处理")
    @ApiOperationSupport(ignoreParameters = {"learnRecords"})
    @PostMapping("zhili")
    @OperateLog(mapper = PartylearnMapper.class, className = Partylearn.class, type = "新增-综合治理")

    public void addPartylearnZhili(@Valid @RequestBody Partylearn partylearn) throws FebsException {
        try {
            partylearn.setPartylearnType(3);
            this.partylearnService.createPartylearn(partylearn);
        } catch (FebsException e) {
            throw new FebsException(e.getMessage());
        } catch (Exception e) {
            String message = "新增党建学习表失败";
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @ApiOperationSupport(ignoreParameters = {"learnRecords"})
    @DeleteMapping
    @OperateLog(mapper = PartylearnMapper.class, className = Partylearn.class, type = Operate.TYPE_DELETE)

    public void deletePartylearn(Partylearn partylearn) throws FebsException {
        try {
            this.partylearnService.deletePartylearn(partylearn);
        } catch (Exception e) {
            String message = "删除党建学习表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @ApiOperationSupport(ignoreParameters = {"learnRecords"})
    @PutMapping
    @OperateLog(mapper = PartylearnMapper.class, className = Partylearn.class, type = Operate.TYPE_MODIFY)

    public void updatePartylearn(@RequestBody Partylearn partylearn) throws FebsException {
        try {
            this.partylearnService.updatePartylearn(partylearn);
        } catch (Exception e) {
            String message = "修改党建学习表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
