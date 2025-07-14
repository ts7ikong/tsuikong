package cc.mrbird.febs.server.system.controller;

import cc.mrbird.febs.common.core.annotation.AuthMenu;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.constant.MenuConstant;
import cc.mrbird.febs.common.core.entity.constant.StringConstant;
import cc.mrbird.febs.common.core.entity.router.VueRouter;
import cc.mrbird.febs.common.core.entity.system.Menu;
import cc.mrbird.febs.common.core.entity.system.TButton;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.server.system.annotation.ControllerEndpoint;
import cc.mrbird.febs.server.system.service.IMenuService;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
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
@RequestMapping("/menu")
public class MenuController {

    private final IMenuService menuService;

    @GetMapping("/{username}")
    public FebsResponse getUserRouters(@NotBlank(message = "{required}") @PathVariable String username)
        throws FebsException {
        return new FebsResponse().data(menuService.getUserOldRouters(username));

    }

    @ApiOperation("获取权限--v2")
    @GetMapping("v2/{username}")
    @ApiIgnore
    public FebsResponse getNewUserRouters(@NotBlank(message = "{required}") @PathVariable String username)
        throws FebsException {
        return new FebsResponse().data(menuService.getUserRouters());
    }

    @ApiOperation("权限菜单")
    @GetMapping
    @AuthMenu(MenuConstant.ROLE_ID)
    @ApiIgnore
    public FebsResponse menuList(Menu menu, HttpServletRequest request) throws FebsException {
        return new FebsResponse().data(this.menuService.findMenus(menu, request));
    }

    @ApiOperation("权限菜单-v2")
    @GetMapping("v2")
    @AuthMenu(MenuConstant.ROLE_ID)
    public FebsResponse menuNewList(Menu menu, HttpServletRequest request) throws FebsException {
        return new FebsResponse().data(this.menuService.findNewMenus(menu, request));
    }

    @GetMapping("/permissions")
    public String findUserPermissions(String username) {
        return this.menuService.findUserPermissions(username);
    }

    @PostMapping
    @ApiIgnore
    @ControllerEndpoint(operation = "新增菜单", exceptionMessage = "新增菜单失败")
    public void addMenu(@Valid Menu menu) {
        // this.menuService.createMenu(menu);
    }

    @DeleteMapping("/{menuIds}")
    @ApiIgnore
    @ControllerEndpoint(operation = "删除菜单", exceptionMessage = "删除菜单失败")
    public void deleteMenus(@NotBlank(message = "{required}") @PathVariable String menuIds) {
        String[] ids = menuIds.split(StringConstant.COMMA);
        // this.menuService.deleteMeuns(ids);
    }

    @PutMapping
    @ControllerEndpoint(operation = "修改菜单", exceptionMessage = "修改菜单失败")
    public void updateMenu(@Valid Menu menu) {
        // this.menuService.updateMenu(menu);
    }

    @PostMapping("excel")
    @ApiIgnore
    @ControllerEndpoint(operation = "导出菜单数据", exceptionMessage = "导出Excel失败")
    public void export(Menu menu, HttpServletResponse response) {
        List<Menu> menus = this.menuService.findMenuList(menu);
        ExcelKit.$Export(Menu.class, response).downXlsx(menus, false);
    }
}