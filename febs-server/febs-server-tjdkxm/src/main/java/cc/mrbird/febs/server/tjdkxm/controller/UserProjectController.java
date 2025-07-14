package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.constant.ParamsConstant;
import cc.mrbird.febs.common.core.entity.tjdkxm.UserProject;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.service.UserProjectService;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 用户项目(Parcel)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:04
 */

@Slf4j
@Validated
@RestController
@Api(tags = "用户项目")
@RequestMapping("/userproject")
@RequiredArgsConstructor
public class UserProjectController {
    /**
     * 服务对象
     */
    @Autowired
    private UserProjectService userProjectService;

    @GetMapping
    public Rest<List<UserProject>> getAllUserProjects() {

        return Rest.data(userProjectService.findUserProject());
    }

    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    public FebsResponse userProjectList(QueryRequest request, UserProject userProject) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(userProjectService.findUserProject(request, userProject));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "查询--用户拥有的项目只包含 项目id 项目名称 用于单级的", notes = "暂无查询条件")
    @GetMapping("user/project/list")
    public FebsResponse getUserAllProjectList(@RequestParam("menuId") String menuId) {
        return new FebsResponse().data(userProjectService.getUserAllProject(menuId));
    }

    @ApiOperation(value = "查询--项目关系 (分部分项)", notes = "暂无查询条件")
    @GetMapping("project/list")
    public FebsResponse userProjectInfoList(@RequestParam("menuId") String menuId,
        @RequestParam("buttonId") Integer buttonId) {
        return new FebsResponse().data(userProjectService.getAllProjectChooses(menuId, buttonId));
    }

    @ApiOperation(value = "查询--项目人员", notes = "暂无查询条件")
    @GetMapping("project/user/list")
    public FebsResponse userProjectUserInfoList() {
        return new FebsResponse().data(userProjectService.getAllUserByProject());
    }

    @ApiOperation(value = "查询--质量检查计划/质量问题检查人", notes = "暂无查询条件")
    @GetMapping("user/list/quality/plan")
    public FebsResponse projectUserInfoListQualityPlan() {
        return new FebsResponse().data(userProjectService.projectUserInfoListQualityPlan());
    }

    @ApiOperation(value = "查询--质量问题整改人", notes = "暂无查询条件")
    @GetMapping("user/list/quality/rectify")
    public FebsResponse projectUserInfoListQualityRectify() {
        return new FebsResponse().data(userProjectService.projectUserInfoListQualityRectify());
    }

    @ApiOperation(value = "查询--质量问题验收人", notes = "暂无查询条件")
    @GetMapping("user/list/quality/acceptance")
    public FebsResponse projectUserInfoListQualityAcceptance() {
        return new FebsResponse().data(userProjectService.projectUserInfoListQualityAcceptance());
    }

    @ApiOperation(value = "查询--安全检查计划/安全隐患检查人", notes = "暂无查询条件")
    @GetMapping("user/list/safe/plan")
    public FebsResponse projectUserInfoListSafePlan() {
        return new FebsResponse().data(userProjectService.projectUserInfoListSafePlan());
    }

    @ApiOperation(value = "查询--安全隐患整改人", notes = "暂无查询条件")
    @GetMapping("user/list/safe/rectify")
    public FebsResponse projectUserInfoListSafeRectify() {
        return new FebsResponse().data(userProjectService.projectUserInfoListSafeRectify());
    }

    @ApiOperation(value = "查询--安全隐患验收人", notes = "暂无查询条件")
    @GetMapping("user/list/safe/acceptance")
    public FebsResponse projectUserInfoListSafeAcceptance() {
        return new FebsResponse().data(userProjectService.projectUserInfoListSafeAcceptance());
    }

    @ApiOperation(value = "查询--拥有工作审批的用户", notes = "暂无查询条件")
    @GetMapping("user/list/work/approval")
    public FebsResponse userInfoWorkApproval() {
        return new FebsResponse().data(userProjectService.userInfoWorkApproval());
    }

    @ApiOperation(value = "查询--拥有会议管理的用户", notes = "暂无查询条件")
    @GetMapping("user/list/management")
    public FebsResponse userInfoManagement() {
        return new FebsResponse().data(userProjectService.userInfoManagement());
    }

    @ApiOperation(value = "查询--所有人员", notes = "暂无查询条件")
    @GetMapping("user/list")
    public FebsResponse userUserInfoList() {
        return new FebsResponse().data(userProjectService.getAllUser());
    }

    @ApiOperation(value = "查询--所有人员", notes = "暂无查询条件")
    @GetMapping("user/list/new")
    public FebsResponse userUserInfoNewList() {
        return new FebsResponse().data(userProjectService.getAllUserNew());
    }

    @ApiOperation(value = "分页查询--项目员工信息", notes = "暂无查询条件")
    @GetMapping("Yglist")
    public FebsResponse findUserByProjectId(QueryRequest request, UserProject.UserModel userProject)
        throws FebsException {
        Map<String, Object> dataTable =
            FebsUtil.getDataTable(userProjectService.findUserByProjectId(request, userProject));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "分页查询--未在项目的员工信息", notes = "暂无查询条件")
    @GetMapping("not/Yglist")
    public FebsResponse findNotUserByProjectId(QueryRequest request, UserProject.UserModel userProject)
        throws FebsException {
        Map<String, Object> dataTable =
            FebsUtil.getDataTable(userProjectService.findNotUserByProjectId(request, userProject));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    @ApiIgnore
    public void addUserProject(@Valid UserProject userProject) throws FebsException {
        try {
            // this.userProjectService.createUserProject(userProject);
        } catch (Exception e) {
            if (e instanceof FebsException) {
                throw new FebsException(e.getMessage());
            }
            String message = "新增用户项目表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "批量新增接口", notes = "只执行新增，后端不进行任何处理 {userIds ','分割 projectId 项目id}")
    @PostMapping("list")
    public void createUserProjectList(UserProject.Add userProject) throws FebsException {
        try {
            this.userProjectService.createUserProjectList(userProject);
        } catch (FebsException e) {
            throw new FebsException(e.getMessage());
        } catch (Exception e) {
            String message = "新增用户项目表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "移除项目", notes = "根据项目id，用户id 移除")
    @DeleteMapping
    public void deleteUserProject(UserProject.Add userProject) throws FebsException {
        try {
            this.userProjectService.deleteUserProject(userProject);
        } catch (FebsException e) {
            throw new FebsException(e.getMessage());
        } catch (Exception e) {
            String message = "删除用户项目表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口 <可设为默认>", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    @ApiIgnore
    public void updateUserProject(UserProject userProject) throws FebsException {
        try {
            this.userProjectService.updateUserProject(userProject);
        } catch (Exception e) {
            String message = "修改用户项目表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "切换项目")
    @GetMapping("/toggle")
    @ApiIgnore
    public FebsResponse toggleProject(Long projectId, String password, HttpServletRequest request)
        throws FebsException {
        try {
            String type = request.getHeader(ParamsConstant.LOGIN_TYPE);
            return new FebsResponse().data(userProjectService.toggleProject(projectId, type, password));
        } catch (Exception e1) {
            throw new FebsException(e1.getMessage());
        }
    }

    @ApiOperation(value = "获取用户登录的项目")
    @GetMapping("/project")
    @ApiIgnore
    public FebsResponse getUserProject() {
        return new FebsResponse().data(null);
    }

    @ApiOperation(value = "获取用户用户拥有的项目")
    @GetMapping("/project/all")
    @ApiIgnore
    public FebsResponse getAllProject() throws FebsException {
        return new FebsResponse().data(userProjectService.getAllUserProject());
    }

    @ApiOperation(value = "获取所有项目以及用户用户拥有的项目")
    @GetMapping("/have/project")
    public FebsResponse getAllProject(Long userId) throws FebsException {
        // 项目name，项目id，用户是否在其中，用户id，用户项目id
        if (userId == null) {
            throw new FebsException("用户信息不能为空！！");
        }
        return new FebsResponse().data(userProjectService.getAllProject(userId));
    }

    @ApiOperation(value = "获取所有项目以及用户用户拥有的项目---更新")
    @PostMapping("/have/project/update")
    @ApiIgnore
    public FebsResponse updateAllProject(@RequestBody List<UserProject> userProjects) throws FebsException {
        // 项目name，项目id，用户是否在其中，用户id，用户项目id
        try {
            return new FebsResponse().data(userProjectService.updateUserAllProject(userProjects));
        } catch (Exception e) {
            throw new FebsException("修改失败！");
        }
    }

    @ApiOperation(value = "获取所有项目以及用户用户拥有的项目---更新--new")
    @PostMapping("/v2/have/project/update")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projectIds", value = "更新后的项目id','分割", required = true, dataType = "String"),
        @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String"),})
    public void updateAllProjectNew(String projectIds, Long userId) throws FebsException {
        // 项目name，项目id，用户是否在其中，用户id，用户项目id
        try {
            userProjectService.updateAllProjectNew(projectIds, userId);
        } catch (Exception e) {
            throw new FebsException("修改失败！");
        }
    }
}
