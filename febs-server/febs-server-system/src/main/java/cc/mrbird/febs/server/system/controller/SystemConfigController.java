package cc.mrbird.febs.server.system.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import cc.mrbird.febs.common.core.entity.system.SystemConfig;
import cc.mrbird.febs.common.core.entity.tjdkxm.model.Add;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.system.dto.SystemConfigDto;
import cc.mrbird.febs.server.system.service.SystemConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * (SystemConfig)表控制层
 *
 * @author zlkj_cg
 * @since 2021-03-03 16:44:16
 */

@Slf4j
@Validated
@RestController
@Api(tags = "系统配置信息(SystemConfig)控制层")
@RequestMapping("system/config")
@RequiredArgsConstructor
public class SystemConfigController {
    /**
     * 服务对象
     */
    @Resource
    private SystemConfigService systemConfigService;

    @ApiOperation(value = "查询", notes = "暂无查询条件")
    @GetMapping
    public Rest<List<SystemConfig>> systemConfigList(SystemConfigDto systemConfig) {
        return Rest.data(systemConfigService.findSystemConfigs(systemConfig));
    }

    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    public void addSystemConfig(@Validated(Add.class) SystemConfigDto systemConfig) throws FebsException {
        try {
            this.systemConfigService.createSystemConfig(systemConfig);
        } catch (Exception e) {
            String message = "系统配置信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @DeleteMapping
    @ApiImplicitParam(name = "id", value = "id 唯一标识")
    public void deleteSystemConfig(@NotEmpty(message = "请选择数据") String id) throws FebsException {
        try {
            this.systemConfigService.deleteSystemConfig(id);
        } catch (Exception e) {
            String message = "删除系统配置信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    public void updateSystemConfig(@Validated(Add.class) SystemConfigDto systemConfig) throws FebsException {
        try {
            this.systemConfigService.updateSystemConfig(systemConfig);
        } catch (Exception e) {
            String message = "修改系统配置信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
