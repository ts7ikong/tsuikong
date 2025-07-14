package cc.mrbird.febs.server.system.controller;

import cc.mrbird.febs.common.core.annotation.AuthMenu;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.constant.*;
import cc.mrbird.febs.common.core.entity.system.Role;
import cc.mrbird.febs.common.core.entity.system.RoleDto;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.system.annotation.ControllerEndpoint;
import cc.mrbird.febs.server.system.service.IRoleService;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("role")
public class RoleController {

    private final IRoleService roleService;

    @ApiOperation(value = "角色管理-角色列表", response = Role.class)
    @GetMapping
    @AuthMenu(MenuConstant.ROLE_ID)
    @ApiIgnore
    public FebsResponse roleList(QueryRequest queryRequest, Role role, HttpServletRequest request) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(roleService.findRoles(role, queryRequest));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "角色管理-角色列表-v2", response = Role.class)
    @GetMapping("v2")
    @AuthMenu(MenuConstant.ROLE_ID)
    public FebsResponse roleListNew(QueryRequest queryRequest, Role role, HttpServletRequest request) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(roleService.findNewRoles(role, queryRequest));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "角色管理-角色列表-v2", response = Role.class)
    @GetMapping("v2New")
    @AuthMenu(MenuConstant.ROLE_ID)
    public FebsResponse roleListNewV2(QueryRequest queryRequest, Role role, HttpServletRequest request) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(roleService.findNewRoles(role, queryRequest));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "角色管理-根据rId查询", response = Role.class)
    @GetMapping("v3")
    @AuthMenu(MenuConstant.ROLE_ID)
    public FebsResponse roleListNew3(Long rId) {
        return new FebsResponse().data(roleService.findNewRoles3(rId));
    }


    @GetMapping("options")
    @ApiOperation("下拉选择")
    @ApiIgnore
    public FebsResponse roles(Role role, HttpServletRequest request) {
        List<Role> allRoles = roleService.findAllRoles(role);
        return new FebsResponse().data(allRoles);
    }

    @GetMapping("v2/options")
    @ApiOperation("下拉选择-v2")
    public FebsResponse roleNews(Role role, HttpServletRequest request) {
        List<Role> allRoles = roleService.findAllNewRoles(role);
        return new FebsResponse().data(allRoles);
    }

    @GetMapping("check/{roleName}")
    public boolean checkRoleName(@NotBlank(message = "{required}") @PathVariable String roleName,
                                 HttpServletRequest request) {
        Role result = this.roleService.findByName(roleName);
        return result == null;
    }

    @ApiOperation("新增角色")
    @PostMapping
    // @ApiIgnore
    @AuthMenu(value = MenuConstant.ROLE_ID, buttonId = ButtonConstant.BUTTON_6_ID)
    @ControllerEndpoint(operation = "新增角色", exceptionMessage = "新增角色失败")
    public void addRole(@Valid Role role, HttpServletRequest request) throws FebsException {
        this.roleService.createRole(role);
    }

    @ApiOperation("新增角色-v2")
    @PostMapping("v2")
    @AuthMenu(value = MenuConstant.ROLE_ID, buttonId = ButtonConstant.BUTTON_6_ID)
    @ControllerEndpoint(operation = "新增角色", exceptionMessage = "新增角色失败")
    public void addNewRole(@Valid @RequestBody RoleDto.Params role, HttpServletRequest request) throws FebsException {
        this.roleService.createNewRole(role);
    }

    @ApiOperation("删除角色-多个")
    @DeleteMapping("/multi/{roleIds}")
    @ControllerEndpoint(operation = "删除角色-多个", exceptionMessage = "删除角色失败")
    @AuthMenu(value = MenuConstant.ROLE_ID, buttonId = ButtonConstant.BUTTON_8_ID)
    public void deleteRoles(@NotBlank(message = "{required}") @PathVariable String roleIds, HttpServletRequest request)
            throws FebsException {
        String[] ids = roleIds.split(StringConstant.COMMA);
        this.roleService.deleteRoles(ids);
    }

    @ApiOperation("删除角色-单个")
    @DeleteMapping("/one/{roleId}")
    // @PreAuthorize("hasAuthority('role:delete')")
    @ControllerEndpoint(operation = "删除角色-单个", exceptionMessage = "删除角色失败")
    @AuthMenu(value = MenuConstant.ROLE_ID, buttonId = ButtonConstant.BUTTON_8_ID)
    public void deleteRole(@NotBlank(message = "{required}") @PathVariable String roleId, HttpServletRequest request)
            throws FebsException {
        this.roleService.deleteRole(roleId);
    }

    @ApiOperation("修改角色")
    @PutMapping
    // @ApiIgnore
    @AuthMenu(value = MenuConstant.ROLE_ID, buttonId = ButtonConstant.BUTTON_7_ID)
    @ControllerEndpoint(operation = "修改角色", exceptionMessage = "修改角色失败")
    public void updateRole(@Valid Role role, HttpServletRequest request) throws FebsException {
        this.roleService.updateRole(role);
    }

    @ApiOperation("修改角色-v2")
    @PutMapping("v2")
    @AuthMenu(value = MenuConstant.ROLE_ID, buttonId = ButtonConstant.BUTTON_7_ID)
    @ControllerEndpoint(operation = "修改角色", exceptionMessage = "修改角色失败")
    public void updateNewRole(@Valid @RequestBody RoleDto.Params role, HttpServletRequest request)
            throws FebsException {
        this.roleService.updateNewRole(role);
    }

    @PostMapping("excel")
    @ControllerEndpoint(operation = "导出角色数据", exceptionMessage = "导出Excel失败")
    public void export(QueryRequest queryRequest, Role role, HttpServletResponse response, HttpServletRequest request) {
        List<Role> roles = this.roleService.findRoles(role, queryRequest).getRecords();
        ExcelKit.$Export(Role.class, response).downXlsx(roles, false);
    }
}
