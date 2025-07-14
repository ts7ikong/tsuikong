package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Aqjlrz;
import cc.mrbird.febs.common.core.entity.tjdkxm.Askfleave;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.mapper.AqjlrzMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.AskfleaveMapper;
import cc.mrbird.febs.server.tjdkxm.service.AskfleaveService;
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
 * 请假申请审批表(Askfleave)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:05
 */

@Slf4j
@Validated
@RestController
@Api(tags = "请假申请审批表(Askfleave)控制层")
@RequestMapping("/askfleave")
@RequiredArgsConstructor
public class AskfleaveController {
    /**
     * 服务对象
     */
    @Autowired
    private AskfleaveService askfleaveService;

    @GetMapping
    public Rest<List<Askfleave>> getAllAskfleaves(Askfleave askfleave) {
        return Rest.data(askfleaveService.findAskfleaves(askfleave));
    }

    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    public FebsResponse AskfleaveList(QueryRequest request, Askfleave askfleave) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(askfleaveService.findAskfleaves(request, askfleave));
        return new FebsResponse().data(dataTable);
    }
    @ApiOperation(value = "分页查询 审批", notes = "暂无查询条件")
    @GetMapping("list/approval")
    public FebsResponse askfleaveListApproval(QueryRequest request, Askfleave askfleave) throws FebsException {
        Map<String, Object> dataTable = FebsUtil.getDataTable(askfleaveService.findAskfleaveApprovals(request, askfleave));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    @OperateLog(mapper = AskfleaveMapper.class, className = Askfleave.class, type = Operate.TYPE_ADD)
    public void addAskfleave(@Valid  Askfleave askfleave) throws FebsException {
        try {
            this.askfleaveService.createAskfleave(askfleave);
        } catch (Exception e) {
            String message = "新增请假申请失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @DeleteMapping
    @OperateLog(mapper = AskfleaveMapper.class, className = Askfleave.class, type = Operate.TYPE_DELETE)
    public void deleteAskfleave(Askfleave askfleave) throws FebsException {
        try {
            this.askfleaveService.deleteAskfleave(askfleave);
        } catch (FebsException e1) {
            throw new FebsException(e1.getMessage());
        } catch (Exception e) {
            String message = "删除请假信息失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    @OperateLog(mapper = AskfleaveMapper.class, className = Askfleave.class, type = Operate.TYPE_MODIFY)
    public void updateAskfleave(Askfleave askfleave) throws FebsException {
        try {
            this.askfleaveService.updateAskfleave(askfleave,false);
        } catch (FebsException e1) {
            throw new FebsException(e1.getMessage());
        } catch (Exception e) {
            String message = "修改请假申请审批表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping("approval")
    @OperateLog(mapper = AskfleaveMapper.class, className = Askfleave.class, type = Operate.TYPE_APPROVAL)
    public void updateAskfleaveApproval(Askfleave askfleave) throws FebsException {
        try {
            this.askfleaveService.updateAskfleave(askfleave,true);
        } catch (FebsException e1) {
            throw new FebsException(e1.getMessage());
        } catch (Exception e) {
            String message = "修改请假申请审批表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
