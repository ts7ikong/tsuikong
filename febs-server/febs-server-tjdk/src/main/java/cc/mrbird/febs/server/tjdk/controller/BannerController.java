package cc.mrbird.febs.server.tjdk.controller;

import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdk.Banner;
import cc.mrbird.febs.common.core.entity.tjdkxm.Askfleave;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdk.mapper.BannerMapper;
import cc.mrbird.febs.server.tjdk.service.BannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * banner管理(Banner)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:03
 */

@Slf4j
@Validated
@RestController
@Api(tags = "banner管理(Banner)控制层")
@RequestMapping("/banner")
public class BannerController {
    /**
     * 服务对象
     */
    @Autowired
    private BannerService bannerService;

    @GetMapping
    public Rest<List<Banner>> getAllBanners(Banner banner) {
        return Rest.data(bannerService.findBanners(banner));
    }

    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    public FebsResponse BannerList(QueryRequest request, Banner banner) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(bannerService.findBanners(request, banner));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    @OperateLog(mapper = BannerMapper.class, className = Banner.class, type = Operate.TYPE_ADD)
    public void addBanner(@RequestBody List<Banner> banner) throws FebsException {
        try {
            this.bannerService.createBanner(banner);
        } catch (FebsException e1) {
            throw new FebsException(e1.getMessage());
        } catch (Exception e) {
            String message = "新增banner管理失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @DeleteMapping
    @OperateLog(mapper = BannerMapper.class, className = Banner.class, type = Operate.TYPE_DELETE)

    public void deleteBanner(Banner banner) throws FebsException {
        try {
            this.bannerService.deleteBanner(banner);
        } catch (FebsException e1) {
            throw new FebsException(e1.getMessage());
        } catch (Exception e) {
            String message = "删除banner管理失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    @OperateLog(mapper = BannerMapper.class, className = Banner.class, type = Operate.TYPE_MODIFY)

    public void updateBanner(Banner banner) throws FebsException {
        try {
            this.bannerService.updateBanner(banner);
        } catch (Exception e) {
            String message = "修改banner管理失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
