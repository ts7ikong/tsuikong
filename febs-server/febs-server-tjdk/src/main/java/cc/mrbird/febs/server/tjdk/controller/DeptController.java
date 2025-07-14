package cc.mrbird.febs.server.tjdk.controller;

import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdk.Banner;
import cc.mrbird.febs.common.core.entity.tjdk.Dept;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdk.mapper.BannerMapper;
import cc.mrbird.febs.server.tjdk.mapper.DeptMapper;
import cc.mrbird.febs.server.tjdk.service.DeptService;
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
 * 部门表(Dept)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:04
 */

@Slf4j
@Validated
@RestController
@Api(tags = "部门表(Dept)控制层")
@RequestMapping("/dept")
public class DeptController {
    /**
     * 服务对象
     */
    @Autowired
    private DeptService deptService;

    @GetMapping
    public Rest<List<Dept>> getAllDepts(Dept dept) {
        return Rest.data(deptService.findDepts(dept));
    }

    @GetMapping("all")
    @ApiOperation(value = "查询所有 -- 用于选择框", notes = "暂无查询条件")
    public FebsResponse getAllDepts() {
        return new FebsResponse().data(deptService.findDepts(new Dept()));
    }

    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    public FebsResponse deptList(QueryRequest request, Dept dept) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(deptService.findDepts(request, dept));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    @OperateLog(mapper = DeptMapper.class, className = Dept.class, type = Operate.TYPE_ADD)

    public void addDept(@Valid Dept dept) throws FebsException {
        try {
            this.deptService.createDept(dept);
        } catch (Exception e) {
            String message = "新增部门表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @DeleteMapping
    @OperateLog(mapper = DeptMapper.class, className = Dept.class, type = Operate.TYPE_DELETE)

    public void deleteDept(Dept dept) throws FebsException {
        try {
            this.deptService.deleteDept(dept);
        } catch (Exception e) {
            String message = "删除部门表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    @OperateLog(mapper = DeptMapper.class, className = Dept.class, type = Operate.TYPE_MODIFY)

    public void updateDept(Dept dept) throws FebsException {
        try {
            this.deptService.updateDept(dept);
        } catch (Exception e) {
            String message = "修改部门表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
