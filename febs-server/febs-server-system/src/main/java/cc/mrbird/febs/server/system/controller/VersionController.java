package cc.mrbird.febs.server.system.controller;

import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.system.Version;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.system.dto.VersionDto;
import cc.mrbird.febs.server.system.service.VersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 广告信息表(Version)表控制层
 *
 * @author zlkj_cg
 * @since 2021-03-03 16:44:16
 */

@Slf4j
@Validated
@RestController
@Api(tags = "版本信息(Version)控制层")
@RequestMapping("version")
@RequiredArgsConstructor
public class VersionController {
    /**
     * 服务对象
     */
    @Autowired
    private VersionService versionService;

    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    public FebsResponse versionList(QueryRequest request, VersionDto version) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(versionService.findVersions(request, version));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "查询是否有新版本")
    @GetMapping("has")
    public Rest<VersionDto> hasVersion(@RequestParam(value = "version") String version) {
        return Rest.data(versionService.hasVersion(version));
    }

    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    public void addVersion(@Valid VersionDto.VersionDtoAdd version) throws FebsException {
        try {
            this.versionService.createVersion(version);
        } catch (Exception e) {
            String message = "新增版本信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @DeleteMapping
    public void deleteVersion(VersionDto version) throws FebsException {
        try {
            this.versionService.deleteVersion(version);
        } catch (Exception e) {
            String message = "删除版本信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    public void updateVersion(VersionDto version) throws FebsException {
        try {
            this.versionService.updateVersion(version);
        } catch (Exception e) {
            String message = "修改版本信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
