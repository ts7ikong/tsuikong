package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Project;
import cc.mrbird.febs.common.core.entity.tjdkxm.PunchArea;
import cc.mrbird.febs.common.core.entity.tjdkxm.model.Add;
import cc.mrbird.febs.common.core.entity.tjdkxm.model.Delete;
import cc.mrbird.febs.common.core.entity.tjdkxm.model.Update;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.mapper.PunchAreaMapper;
import cc.mrbird.febs.server.tjdkxm.service.PunchAreaService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Map;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/9 17:22
 */
@Slf4j
@RestController
@Api(tags = "项目打卡区域")
@RequestMapping("/puncharea")
@RequiredArgsConstructor
public class PunchAreaController {
    /**
     * 服务对象
     */
    @Autowired
    private PunchAreaService punchAreaService;

    @GetMapping
    public Rest<List<PunchArea>> getAllPunchAreas() {
        return Rest.data(punchAreaService.findPunchAreas());
    }

    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    public FebsResponse punchAreaList(QueryRequest request, PunchArea punchArea) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(punchAreaService.findPunchAreas(request, punchArea));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    @OperateLog(mapper = PunchAreaMapper.class, className = PunchArea.class, type = Operate.TYPE_ADD)
    public void addPunchArea(@Validated(Add.class) PunchArea punchArea) throws FebsException {
        try {
            this.punchAreaService.createPunchArea(punchArea);
        } catch (FebsException e) {
            throw new FebsException(e);
        } catch (Exception e) {
            String message = "新增打卡区域失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "区域人员配置")
    @PostMapping("up/user")
    @ApiIgnore
    @OperateLog(mapper = PunchAreaMapper.class, className = PunchArea.class, type = Operate.TYPE_MODIFY)
    @ApiImplicitParams({@ApiImplicitParam(name = "userIds", value = "用户ids", required = false),
        @ApiImplicitParam(name = "punchAreaId", value = "区域id", required = true)})
    public void upPunchAreaUser(String userIds, @NotNull(message = "请选择区域") Long punchAreaId) throws FebsException {
        try {
            this.punchAreaService.upPunchAreaUser(userIds, punchAreaId);
        } catch (FebsException e) {
            throw new FebsException(e);
        } catch (Exception e) {
            String message = "区域人员配置失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "添加区域人员")
    @PostMapping("add/user")
    @OperateLog(mapper = PunchAreaMapper.class, className = PunchArea.class, type = Operate.TYPE_MODIFY)
    @ApiImplicitParams({@ApiImplicitParam(name = "userIds", value = "用户ids", required = true),
        @ApiImplicitParam(name = "punchAreaId", value = "区域id", required = true)})
    public void addPunchAreaUser(@NotNull(message = "请选择人员") String userIds,
        @NotNull(message = "请选择区域") Long punchAreaId) throws FebsException {
        try {
            this.punchAreaService.addPunchAreaUser(userIds, punchAreaId);
        } catch (FebsException e) {
            throw new FebsException(e);
        } catch (Exception e) {
            String message = "区域人员配置失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除区域人员")
    @PostMapping("del/user")
    @OperateLog(mapper = PunchAreaMapper.class, className = PunchArea.class, type = Operate.TYPE_MODIFY)
    @ApiImplicitParams({@ApiImplicitParam(name = "userIds", value = "用户ids", required = true),
        @ApiImplicitParam(name = "punchAreaId", value = "区域id", required = true)})
    public void delPunchAreaUser(@NotNull(message = "请选择人员") String userIds,
        @NotNull(message = "请选择区域") Long punchAreaId) throws FebsException {
        try {
            this.punchAreaService.delPunchAreaUser(userIds, punchAreaId);
        } catch (FebsException e) {
            throw new FebsException(e);
        } catch (Exception e) {
            String message = "区域人员配置失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "获取用户 {userId:用户id,username:用户名,realname:用户姓名,deptName:用户部门,projectName:用户项目名称}")
    @GetMapping("get/user")
    @ApiImplicitParams({@ApiImplicitParam(name = "username", value = "用户名、姓名", required = false),
        @ApiImplicitParam(name = "projectId", value = "项目id", required = false),
        @ApiImplicitParam(name = "punchAreaId", value = "区域id", required = true),
        @ApiImplicitParam(name = "deptId", value = "部门id", required = false),
        @ApiImplicitParam(name = "type", value = "类型 2区域中  1不在区域中", required = true, defaultValue = "2")})
    public FebsResponse getPunchAreaUser(QueryRequest request, @RequestParam(value = "punchAreaId") Long punchAreaId,
        @RequestParam(value = "username", required = false) String username,
        @RequestParam(value = "deptId", required = false) Long deptId,
        @RequestParam(value = "projectId", required = false) Long projectId,
        @RequestParam(value = "type", required = false, defaultValue = "2") Integer type) throws FebsException {
        try {
            Map<String, Object> dataTable = FebsUtil.getDataTable(
                punchAreaService.getPunchAreaUser(punchAreaId, username, deptId, projectId, request, type));
            return new FebsResponse().data(dataTable);
        } catch (Exception e) {
            String message = "查询失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "区域打卡规则配置")
    @PostMapping("up/rule")
    @OperateLog(mapper = PunchAreaMapper.class, className = PunchArea.class, type = Operate.TYPE_MODIFY)
    @ApiImplicitParams({@ApiImplicitParam(name = "punchAreaId", value = "区域id", required = true),
        @ApiImplicitParam(name = "rules",
            value = "区域id 打卡时间段格式 String [{'id':1,'startTime':'9:00'," + "'endTime':'10:00'}] 更新id必传",
            required = true)})
    public void upPunchAreaRule(String rules, Long punchAreaId) throws FebsException {
        try {
            this.punchAreaService.upPunchAreaRule(rules, punchAreaId);
        } catch (FebsException e) {
            throw new FebsException(e);
        } catch (Exception e) {
            String message = "区域打卡规则配置失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @DeleteMapping
    @OperateLog(mapper = PunchAreaMapper.class, className = PunchArea.class, type = Operate.TYPE_DELETE)
    public void deletePunchArea(@Validated(Delete.class) PunchArea punchArea) throws FebsException {
        try {
            this.punchAreaService.deletePunchArea(punchArea);
        } catch (Exception e) {
            String message = "删除打卡区域失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    @OperateLog(mapper = PunchAreaMapper.class, className = PunchArea.class, type = Operate.TYPE_MODIFY)
    public void updatePunchArea(@Validated(Update.class) PunchArea punchArea) throws FebsException {
        try {
            this.punchAreaService.updatePunchArea(punchArea);
        } catch (FebsException e1) {
            throw new FebsException(e1.getMessage());
        } catch (Exception e) {
            String message = "修改打卡区域失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
