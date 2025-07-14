package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Project;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.service.MaillistService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 分部表(Parcel)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:04
 */

@Slf4j
@Validated
@RestController
@Api(tags = "通讯录")
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MaillistController {
    /**
     * 服务对象
     */
    @Autowired
    private MaillistService maillistService;

    @GetMapping()
    public FebsResponse getAllMailLists(QueryRequest request, String name, Long deptId) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(maillistService.findMailLists(request, name, deptId));
        return new FebsResponse().data(dataTable);
    }

}
