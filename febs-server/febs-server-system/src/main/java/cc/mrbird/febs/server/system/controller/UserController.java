package cc.mrbird.febs.server.system.controller;

import cc.mrbird.febs.common.core.annotation.AuthMenu;
import cc.mrbird.febs.common.core.annotation.AuthMenus;
import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.constant.ButtonConstant;
import cc.mrbird.febs.common.core.entity.constant.MenuConstant;
import cc.mrbird.febs.common.core.entity.constant.StringConstant;
import cc.mrbird.febs.common.core.entity.system.LoginLog;
import cc.mrbird.febs.common.core.entity.system.SystemUser;
import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
import cc.mrbird.febs.common.core.entity.tjdk.Banner;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.system.annotation.ControllerEndpoint;
import cc.mrbird.febs.server.system.mapper.UserMapper;
import cc.mrbird.febs.server.system.service.ILoginLogService;
import cc.mrbird.febs.server.system.service.IUserAuthService;
import cc.mrbird.febs.server.system.service.IUserService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Api("系统管理-用户管理")
@RequestMapping("user")
public class UserController {

    private final IUserService userService;
    private final IUserAuthService userAuthService;
    private final ILoginLogService loginLogService;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation(value = "查询--安全检查计划验收人", notes = "暂无查询条件")
    @GetMapping("list/safe/plan/ysr")
    public FebsResponse projectUserInfoListSafePlanYsr(Long projectId, Long buttonId) {
        return new FebsResponse().data(userService.projectUserInfoListSafePlanYsr(projectId, buttonId));
    }

    @GetMapping("register/user")
    public FebsResponse registerUser() {
        SystemUser user = userService.getById(FebsUtil.getCurrentUser().getUserId());
        return new FebsResponse().put("register", user);
    }

    @GetMapping("user/all")
    @ApiOperation(value = "获取所有用户--用于选择框")
    public FebsResponse userAll() {
        return new FebsResponse().data(userService.getAllUsers());
    }

    @GetMapping("auth/{userId}")
    @ApiIgnore
    public AuthUserModel userAuth(@PathVariable("userId") Long userId) {
        if (userId == null) {
            userId = FebsUtil.getCurrentUserId();
        }
        return userAuthService.userBackendPermissions(userId);
    }

    @GetMapping("auth/")
    @ApiOperation(value = "获取用户数据权限")
    public FebsResponse auth() {
        return new FebsResponse().data(userAuthService.userFrontEndPermission(null));
    }

    @GetMapping("success")
    public void loginSuccess(HttpServletRequest request) {
        String currentUsername = FebsUtil.getCurrentUsername();
        // update last login time
        // this.userService.updateLoginTime(currentUsername);
        // save login log
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(currentUsername);
        loginLog.setSystemBrowserInfo(request.getHeader("user-agent"));
        this.loginLogService.saveLoginLog(loginLog);
    }

    @GetMapping("index")
    public FebsResponse index() {
        Map<String, Object> data = new HashMap<>(5);
        // 获取系统访问记录
        Long totalVisitCount = loginLogService.findTotalVisitCount();
        data.put("totalVisitCount", totalVisitCount);
        Long todayVisitCount = loginLogService.findTodayVisitCount();
        data.put("todayVisitCount", todayVisitCount);
        Long todayIp = loginLogService.findTodayIp();
        data.put("todayIp", todayIp);
        // 获取近期系统访问记录
        List<Map<String, Object>> lastTenVisitCount = loginLogService.findLastTenDaysVisitCount(null);
        data.put("lastTenVisitCount", lastTenVisitCount);
        SystemUser param = new SystemUser();
        param.setUsername(FebsUtil.getCurrentUsername());
        List<Map<String, Object>> lastTenUserVisitCount = loginLogService.findLastTenDaysVisitCount(param);
        data.put("lastTenUserVisitCount", lastTenUserVisitCount);
        return new FebsResponse().data(data);
    }

    @ApiOperation(value = "查询用户列表-分页")
    @GetMapping
    @AuthMenu(value = MenuConstant.USER_ID)
    public FebsResponse userList(QueryRequest queryRequest, SystemUser user) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(userService.findUserDetailList(user, queryRequest));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "查询项目中的用户列表-分页")
    @GetMapping("userInProject")
    @ApiIgnore
    public FebsResponse userByProjectList(QueryRequest queryRequest, Long projectId, SystemUser user)
        throws FebsException {
        Map<String, Object> dataTable =
            FebsUtil.getDataTable(userService.userByProjectList(user, projectId, queryRequest));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "查询没在此项目中的用户列表-分页")
    @GetMapping("userNotInProject")
    @ApiIgnore
    public FebsResponse userNotInProjectList(QueryRequest queryRequest, Long projectId, SystemUser user)
        throws FebsException {
        Map<String, Object> dataTable =
            FebsUtil.getDataTable(userService.userNotInProject(user, projectId, queryRequest));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "查询员工用户列表-分页")
    @GetMapping("userYgList")
    @AuthMenu(value = MenuConstant.USER_ID)
    @ApiOperationSupport(ignoreParameters = {"roleList[0]"})
    public FebsResponse userYgList(QueryRequest queryRequest, SystemUser user) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(userService.findYgUserDetailList(user, queryRequest));
        return new FebsResponse().data(dataTable);
    }

    @PostMapping("addYgUser")
    @ApiOperation(value = "新增公司员工用户")
    @AuthMenu(value = MenuConstant.USER_ID, buttonId = ButtonConstant.BUTTON_1_ID)
    @ApiOperationSupport(ignoreParameters = {"roleList[0]"})
    @ControllerEndpoint(operation = "新增公司员工用户", exceptionMessage = "新增失败")
    @OperateLog(mapper = UserMapper.class, className = SystemUser.class, type = "新增公司员工")
    public void addYgUser(SystemUser user) throws FebsException {
        this.userService.addYgUser(user);
    }

    @PutMapping("updateYgUser")
    @ApiOperation(value = "修改公司员工用户")
    @AuthMenu(value = MenuConstant.USER_ID, buttonId = ButtonConstant.BUTTON_2_ID)
    @ApiOperationSupport(ignoreParameters = {"roleList[0]"})
    @ControllerEndpoint(operation = "修改公司员工用户", exceptionMessage = "修改用户失败")
    @OperateLog(mapper = UserMapper.class, className = SystemUser.class, type = Operate.TYPE_MODIFY)
    public void updateYgUser(@Valid SystemUser user) throws FebsException {
        this.userService.updateYgUser(user);
    }

    /**
     * 临时用户修改为正式用户
     */
    @PutMapping("updateTempUserToUser")
    @ApiOperation(value = "临时用户修改为正式用户")
    @AuthMenu(value = MenuConstant.TEMPORARY_ACCOUNTANT_ID, buttonId = ButtonConstant.BUTTON_308_ID)
    @ControllerEndpoint(operation = "临时用户修改为正式用户", exceptionMessage = "修改为正式用户失败")
    @OperateLog(mapper = UserMapper.class, className = SystemUser.class, type = Operate.TYPE_MODIFY)
    public void updateTempUserToUser(@Valid SystemUser user) throws FebsException {
        this.userService.updateTempUserToUser(false, user);
    }

    /**
     * 正式用户修改为临时用户
     */
    @PutMapping("updateUserToTempUser")
    @ApiOperation(value = "正式用户修改为临时用户")
    @AuthMenu(value = MenuConstant.USER_ID, buttonId = ButtonConstant.BUTTON_309_ID)
    @ControllerEndpoint(operation = "正式用户修改为临时用户", exceptionMessage = "修改为临时用户失败")
    @OperateLog(mapper = UserMapper.class, className = SystemUser.class, type = Operate.TYPE_MODIFY)
    public void updateUserToTempUser(@Valid SystemUser user) throws FebsException {
        this.userService.updateTempUserToUser(true, user);
    }

    @PutMapping("updateYgUserToProject")
    @ApiOperation(value = "项目分配")
    @ApiIgnore
    @ControllerEndpoint(operation = "项目分配", exceptionMessage = "项目分配失败")
    public void updateYgUserToProject(@Valid SystemUser user) throws FebsException {
        this.userService.updateYgUserToProject(user);
    }

    @ApiOperation(value = "查询项目临时用户列表-分页")
    @GetMapping("userTempList")
    @ApiOperationSupport(ignoreParameters = {"roleList[0]"})
    @AuthMenu(value = MenuConstant.TEMPORARY_ACCOUNTANT_ID)
    public FebsResponse userTempList(QueryRequest queryRequest, SystemUser user) throws FebsException {
        Map<String, Object> dataTable = FebsUtil.getDataTable(userService.findTempUserList(user, queryRequest));
        return new FebsResponse().data(dataTable);
    }

    @PostMapping("addTempUser")
    @ApiOperation(value = "新增项目临时公司用户")
    @AuthMenu(value = MenuConstant.TEMPORARY_ACCOUNTANT_ID, buttonId = ButtonConstant.BUTTON_14_ID)
    @ControllerEndpoint(operation = "新增公司员工用户", exceptionMessage = "新增失败")
    @OperateLog(mapper = UserMapper.class, className = SystemUser.class, type = "新增临时公司用户")
    public void addTempUser(SystemUser user) throws FebsException {
        this.userService.addTempUser(user);
    }

    @PutMapping("updateTempUser")
    @ApiOperation(value = "修改项目临时用户")
    @AuthMenu(value = MenuConstant.TEMPORARY_ACCOUNTANT_ID, buttonId = ButtonConstant.BUTTON_15_ID)
    @ControllerEndpoint(operation = "修改公司员工用户", exceptionMessage = "修改用户失败")
    @OperateLog(mapper = UserMapper.class, className = SystemUser.class, type = Operate.TYPE_MODIFY)
    public void updateTempUser(@Valid SystemUser user) throws FebsException {
        this.userService.updateTempUser(user);
    }

    @GetMapping("check/{username}")
    @ApiOperation(value = "是否有该用户")
    public boolean checkUserName(@NotBlank(message = "{required}") @PathVariable String username) {
        return this.userService.findByName(username) == null;
    }

    @PostMapping
    @ApiOperation(value = "新增用户")
    @ControllerEndpoint(operation = "新增用户", exceptionMessage = "新增失败")
    @AuthMenu(value = MenuConstant.USER_ID, buttonId = ButtonConstant.BUTTON_1_ID)
    @OperateLog(mapper = UserMapper.class, className = SystemUser.class, type = Operate.TYPE_ADD)
    public void addUser(SystemUser user) throws FebsException {
        this.userService.createUser(user);
    }

    @PutMapping
    @ApiOperation(value = "修改用户")
    @ControllerEndpoint(operation = "修改用户", exceptionMessage = "修改用户失败")
    @AuthMenu(value = MenuConstant.USER_ID, buttonId = ButtonConstant.BUTTON_2_ID)
    @OperateLog(mapper = UserMapper.class, className = SystemUser.class, type = Operate.TYPE_MODIFY)
    public void updateUser(@Valid SystemUser user) throws FebsException {
        this.userService.updateUser(user);
    }

    @DeleteMapping("multi/{userIds}")
    @ApiOperation(value = "删除系统用户-多选")
    @AuthMenu(value = MenuConstant.USER_ID, buttonId = ButtonConstant.BUTTON_3_ID)
    @ControllerEndpoint(operation = "系统-删除用户", exceptionMessage = "删除用户失败")
    @OperateLog(mapper = UserMapper.class, className = SystemUser.class, type = Operate.TYPE_DELETE)
    public void deleteUsers(@NotBlank(message = "{required}") @PathVariable String userIds) throws FebsException {
        String[] ids = userIds.split(StringConstant.COMMA);
        this.userService.deleteUsers(ids);
    }

    @DeleteMapping("one/{userId}")
    @ApiOperation(value = "删除系统用户-单选")
    @AuthMenu(value = MenuConstant.USER_ID, buttonId = ButtonConstant.BUTTON_3_ID)
    @ControllerEndpoint(operation = "系统-删除用户", exceptionMessage = "删除用户失败")
    @OperateLog(mapper = UserMapper.class, className = SystemUser.class, type = Operate.TYPE_DELETE)
    public void deleteUser(@NotBlank(message = "{required}") @PathVariable String userId) throws FebsException {
        this.userService.deleteUser(userId);
    }

    @DeleteMapping("oneyg/{userId}")
    @ApiOperation(value = "删除员工用户")
    @ControllerEndpoint(operation = "删除员工用户", exceptionMessage = "删除用户失败")
    @AuthMenus({@AuthMenu(value = MenuConstant.USER_ID, buttonId = ButtonConstant.BUTTON_3_ID),
        @AuthMenu(value = MenuConstant.TEMPORARY_ACCOUNTANT_ID, buttonId = ButtonConstant.BUTTON_16_ID),})
    @OperateLog(mapper = UserMapper.class, className = SystemUser.class, type = Operate.TYPE_DELETE)
    public void deleteygUser(@NotBlank(message = "{required}") @PathVariable String userId) throws FebsException {
        this.userService.deleteYgUser(userId);
    }

    @PutMapping("profile")
    @OperateLog(mapper = UserMapper.class, className = SystemUser.class, type = Operate.TYPE_MODIFY)
    public void updateProfile(@Valid SystemUser user) throws FebsException {
        this.userService.updateProfile(user);
    }

    @PutMapping("avatar")
    @OperateLog(mapper = UserMapper.class, className = SystemUser.class, type = Operate.TYPE_MODIFY)
    public void updateAvatar(@NotBlank(message = "{required}") String avatar) {
        this.userService.updateAvatar(avatar);
    }

    @GetMapping("get/avatar")
    public FebsResponse getAvatar(@NotNull(message = "选择用户") Long userId) {
        return new FebsResponse().data(this.userService.getAvatar(userId));
    }

    @GetMapping("password/check")
    public boolean checkPassword(@NotBlank(message = "{required}") String password) {
        String currentUsername = FebsUtil.getCurrentUsername();
        SystemUser user = userService.findByName(currentUsername);
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }

    @PutMapping("password")
    @OperateLog(mapper = UserMapper.class, className = SystemUser.class, type = "修改密码")
    public void updatePassword(@NotBlank(message = "{required}") String password) throws FebsException {
        userService.updatePassword(password);
    }

    @PutMapping("password/reset")
    @OperateLog(mapper = UserMapper.class, className = SystemUser.class, type = "重置密码")
    public void resetPassword(@NotBlank(message = "{required}") String usernames) {
        String[] usernameArr = usernames.split(StringConstant.COMMA);
        this.userService.resetPassword(usernameArr);
    }

    @PostMapping("excel")
    @ControllerEndpoint(operation = "导出用户数据", exceptionMessage = "导出Excel失败")
    public void export(QueryRequest queryRequest, SystemUser user, HttpServletResponse response) {
        List<SystemUser> users = this.userService.findUserDetailList(user, queryRequest).getRecords();
        ExcelKit.$Export(SystemUser.class, response).downXlsx(users, false);
    }

    /**
     * 重置密码
     *
     * @param
     * @return boolean
     */
    @PutMapping("reset")
    @ApiOperation("根据id重置密码")
    @ControllerEndpoint(operation = "重置密码", exceptionMessage = "重置密码失败")
    @OperateLog(mapper = UserMapper.class, className = SystemUser.class, type = "重置密码")
    public void restPassword(SystemUser user) {
        userService.resetPassword(user);
    }

    /**
     * 重置密码
     *
     * @param
     * @return boolean
     */
    @PutMapping("myReset")
    @ApiOperation("根据id修改个人密码")
    @ControllerEndpoint(operation = "根据id修改个人密码", exceptionMessage = "修改密码失败")
    @OperateLog(mapper = UserMapper.class, className = SystemUser.class, type = "重置密码")
    public void restMyPassword(SystemUser user) throws FebsException {
        log.warn("user为{}", user);
        userService.restMyPassword(user);
    }

    @GetMapping("ws-name")
    @ApiOperation("获取websocket连接名")
    public FebsResponse getCurrentUser() {
        return new FebsResponse().data(userService.getCurrentUser());
    }

}
