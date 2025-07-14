package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Aqjlrz;
import cc.mrbird.febs.common.core.entity.tjdkxm.Danger;
import cc.mrbird.febs.server.tjdkxm.mapper.AqjlrzMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.DangerMapper;
import cc.mrbird.febs.server.tjdkxm.service.AqjlrzService;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiOperation;

/**
 * 安全监理日志表(Aqjlrz)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:07
 */

@Slf4j
@Validated
@RestController
@Api(tags = "安全监理日志表(Aqjlrz)控制层")
@RequestMapping("/aqjlrz")
@RequiredArgsConstructor
public class AqjlrzController {
//    /**
//     * 服务对象
//     */
//    @Autowired
    //    private AqjlrzService aqjlrzService;
//    @Autowired
//    private AqjlrzMapper aqjlrzMapper;
//
//    @GetMapping
//    public JSONObject getAllAqjlrzs(Aqjlrz aqjlrz) {
//        return aqjlrzMapper.selectInfoById(1L);
//    }

//    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
//    @GetMapping("list")
//    public FebsResponse AqjlrzList(QueryRequest request, Aqjlrz aqjlrz) {
//        Map<String, Object> dataTable = FebsUtil.getDataTable( aqjlrzService. findAqjlrzs(request,aqjlrz));
//        return new FebsResponse().data(dataTable);
//    }
//
//    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
//    @PostMapping
////    @OperateLog(mapper = AqjlrzMapper.class, className = Aqjlrz.class, type = Operate.TYPE_ADD)
//    public void addAqjlrz(@Valid Aqjlrz aqjlrz) throws FebsException {
//        try {
//            this.aqjlrzService. createAqjlrz(aqjlrz);
//        } catch (Exception e) {
//            String message = "新增安全监理日志表失败";
//            log.error(message, e);
//            throw new FebsException(message);
//        }
//    }
//
//    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
//    @DeleteMapping
////    @OperateLog(mapper = AqjlrzMapper.class, className = Aqjlrz.class, type = Operate.TYPE_DELETE)
//    public void deleteAqjlrz(Aqjlrz aqjlrz) throws FebsException {
//        try {
//            this.aqjlrzService. deleteAqjlrz(aqjlrz);
//        } catch (Exception e) {
//            String message = "删除安全监理日志表失败";
//            log.error(message, e);
//            throw new FebsException(message);
//        }
//    }
//
//    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
//    @PutMapping
////    @OperateLog(mapper = AqjlrzMapper.class, className = Aqjlrz.class, type = Operate.TYPE_MODIFY)
//    public void updateAqjlrz(Aqjlrz aqjlrz) throws FebsException {
//        try {
//            this.aqjlrzService. updateAqjlrz(aqjlrz);
//        } catch (Exception e) {
//            String message = "修改安全监理日志表失败";
//            log.error(message, e);
//            throw new FebsException(message);
//        }
//    }

}
