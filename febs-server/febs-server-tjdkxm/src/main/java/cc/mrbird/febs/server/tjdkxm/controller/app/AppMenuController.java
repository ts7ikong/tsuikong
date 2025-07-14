package cc.mrbird.febs.server.tjdkxm.controller.app;

import cc.mrbird.febs.common.core.annotation.AuthMenu;
import cc.mrbird.febs.common.core.annotation.AuthMenus;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.server.tjdkxm.service.AppMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * app端菜单相关
 *
 * @author 14059
 * @version V1.0
 * @date 2022/3/8 11:32
 */
@RestController
@Api(tags = "App首页，我的任务")
@RequestMapping("/menu")
public class AppMenuController {
    @Autowired
    private AppMenuService appMenuService;

    @GetMapping("/agency/num")
    @ApiOperation(value = "获取app待完成数量",
        notes = "{\n" + "  \"data\": [\n" + "    {\n" + "      \"name\": \"质量检查计划\",\n" + "      \"count\": 4\n"
            + "    },\n" + "    {\n" + "      \"name\": \"安全检查计划\",\n" + "      \"count\": 3\n" + "    },\n" + "    {\n"
            + "      \"name\": \"质量问题清单\",\n" + "      \"count\": 3\n" + "    },\n" + "    {\n"
            + "      \"name\": \"安全隐患清单\",\n" + "      \"count\": 3\n" + "    },\n" + "    {\n"
            + "      \"name\": \"请假审批\",\n" + "      \"count\": 4\n" + "    },\n" + "    {\n"
            + "      \"name\": \"加班审批\",\n" + "      \"count\": 4\n" + "    },\n" + "    {\n"
            + "      \"name\": \"工作审批\",\n" + "      \"count\": 4\n" + "    }\n" + "  ],\n" + "  \"state\": 0\n" + "}")
    // @AuthMenus({
    // @AuthMenu(value = 10000,buttonId = 1),
    //// @AuthMenu(value = 24)
    // })
    public FebsResponse getAppMenuAgencyNum() {
        return new FebsResponse().data(appMenuService.getAppMenuAgencyNum());
    }

    @GetMapping("/app/home")
    @ApiOperation(value = "首页数据")
    public FebsResponse appHomepageInfo(QueryRequest request) {
        return new FebsResponse().data(appMenuService.appHomepageInfo(request));
    }
}
