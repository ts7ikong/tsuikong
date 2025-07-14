package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Askfleave;
import cc.mrbird.febs.common.core.entity.tjdkxm.Bidd;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.mapper.BiddMapper;
import cc.mrbird.febs.server.tjdkxm.service.BiddService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 招投标文件(Bidd)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:03
 */

@Slf4j
@Validated
@RestController
@Api(tags = "招投标文件")
@RequestMapping("/bidd")
public class BiddController {
    /**
     * 服务对象
     */
    @Autowired
    private BiddService biddService;

    @GetMapping("test")
    @ApiIgnore
    public Rest<List<Bidd>> getAllBidds(Bidd bidd) {
        return Rest.data(biddService.findBidds(bidd));
    }

    @GetMapping()
    @ApiIgnore
    public Rest<List<Bidd>> getAllBidds1(Bidd bidd) {
        return Rest.data(biddService.findBidds(bidd));
    }

    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    public FebsResponse biddList(QueryRequest request, Bidd.Params bidd) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(biddService.findBidds(request, bidd));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "分页查询-test", notes = "暂无查询条件")
    @GetMapping("/list/{biddId}")
    public Rest<Bidd> biddListById(@PathVariable("biddId") Long biddId) {
        return Rest.data(biddService.getById(biddId));
    }

    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    @OperateLog(mapper = BiddMapper.class, className = Bidd.class, type = Operate.TYPE_ADD)
    public void addBidd(@Valid @RequestBody Bidd bidd) throws FebsException {
        try {
            this.biddService.createBidd(bidd);
        } catch (Exception e) {
            String message = "新增招投标文件失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @DeleteMapping
    @OperateLog(mapper = BiddMapper.class, className = Bidd.class, type = Operate.TYPE_DELETE)
    public void deleteBidd(Bidd bidd) throws FebsException {
        try {
            this.biddService.deleteBidd(bidd);
        } catch (FebsException e) {
            throw new FebsException(e);
        } catch (Exception e) {
            String message = "删除招投标文件失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    @OperateLog(mapper = BiddMapper.class, className = Bidd.class, type = Operate.TYPE_MODIFY)
    public void updateBidd(@RequestBody Bidd bidd) throws FebsException {
        try {
            this.biddService.updateBidd(bidd);
        } catch (FebsException e) {
            throw new FebsException(e);
        } catch (Exception e) {
            String message = "修改招投标文件失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
