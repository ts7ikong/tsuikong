package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.annotation.AuthMenu;
import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.constant.ButtonConstant;
import cc.mrbird.febs.common.core.entity.constant.MenuConstant;
import cc.mrbird.febs.common.core.entity.tjdkxm.Partylearn;
import cc.mrbird.febs.common.core.entity.tjdkxm.Project;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.mapper.PartylearnMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.ProjectMapper;
import cc.mrbird.febs.server.tjdkxm.service.CacheableService;
import cc.mrbird.febs.server.tjdkxm.service.ProjectService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 项目信息(Project)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:03
 */

@Slf4j
@Validated
@RestController
@Api(tags = "项目信息(Project)控制层")
@RequestMapping("/project")
public class ProjectController {
    /**
     * 服务对象
     */
    @Autowired
    private ProjectService projectService;

    @GetMapping
    @ApiOperationSupport(ignoreParameters = {"parcelList"})
    @ApiIgnore
    public Rest<List<Map<String, Object>>> getAllProjects(Project project) {
        return Rest.data(projectService.findProjects(project));
    }

    @GetMapping("/{projectId}")
    @ApiOperationSupport(ignoreParameters = {"parcelList"})
    @ApiIgnore
    public FebsResponse getAllProjects(@PathVariable("projectId") Long projectId) {
        return new FebsResponse().data(projectService.getById(projectId));
    }

    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    @AuthMenu(value = MenuConstant.PROJECTINFO_ID)
    @ApiOperationSupport(ignoreParameters = {"parcelList"})
    public FebsResponse projectList(QueryRequest request, Project.Params project) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(projectService.findProjects(request, project));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    @AuthMenu(value = MenuConstant.PROJECTINFO_ID, buttonId = ButtonConstant.BUTTON_93_ID)
    @OperateLog(mapper = ProjectMapper.class, className = Project.class, type = Operate.TYPE_ADD)
    @ApiOperationSupport(ignoreParameters = {"parcelList"})
    public void addProject(@Valid Project project) throws FebsException {
        try {
            this.projectService.createProject(project);
        } catch (FebsException febs) {
            throw new FebsException(febs.getMessage());
        } catch (Exception e) {
            String message = "新增项目信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @ApiOperationSupport(ignoreParameters = {"parcelList"})
    @DeleteMapping
    @OperateLog(mapper = ProjectMapper.class, className = Project.class, type = Operate.TYPE_DELETE)
    public void deleteProject(Project.Params project) throws FebsException {
        try {
            this.projectService.deleteProject(project);
        } catch (Exception e) {
            if (e instanceof FebsException) {
                throw new FebsException(e.getMessage());
            }
            String message = "删除项目信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @ApiOperationSupport(ignoreParameters = {"parcelList"})
    @PutMapping
    @OperateLog(mapper = ProjectMapper.class, className = Project.class, type = Operate.TYPE_MODIFY)
    @AuthMenu(value = MenuConstant.PROJECTINFO_ID, buttonId = ButtonConstant.BUTTON_94_ID)
    public void updateProject(Project project) throws FebsException {
        try {
            this.projectService.updateProject(project);
        } catch (FebsException e) {
            throw new FebsException(e.getMessage());
        } catch (Exception e) {
            String message = "修改项目信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改---项目负责人", notes = "根据id修改一条数据，只执行修改操作")
    @ApiOperationSupport(ignoreParameters = {"parcelList"})
    @PutMapping("/user")
    @OperateLog(mapper = ProjectMapper.class, className = Project.UserModel.class, type = "修改项目负责人")
    @AuthMenu(value = MenuConstant.ADDEMPLOYEE_ID, buttonId = ButtonConstant.BUTTON_94_ID)
    public void updateProject(@Valid Project.UserModel project) throws FebsException {
        try {
            this.projectService.updateProject(project);
        } catch (Exception e) {
            if (e instanceof FebsException) {
                throw new FebsException(e.getMessage());
            }
            String message = "修改用户失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
