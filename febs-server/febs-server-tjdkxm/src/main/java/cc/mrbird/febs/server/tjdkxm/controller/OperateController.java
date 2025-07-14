package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Parcel;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.service.OperateService;
import cc.mrbird.febs.server.tjdkxm.service.ParcelService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/31 15:58
 */
@Slf4j
@Validated
@RestController
@Api(tags = "操作日志(Operate)控制层")
@RequestMapping("/operate")
public class OperateController {
    /**
     * 服务对象
     */
    @Autowired
    private OperateService operateService;


    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    public FebsResponse operateList(QueryRequest request, Operate operate) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(operateService.findOperates(request, operate));
        return new FebsResponse().data(dataTable);
    }
}
