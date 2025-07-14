package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Jsdwaqrz;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.service.JsdwaqrzService;
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
 * 建设单位安全日志(Jsdwaqrz)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:02
 */

@Slf4j
@Validated
@RestController
@Api(tags = "建设单位安全日志(Jsdwaqrz)控制层")
@RequestMapping("/jsdwaqrz")
@RequiredArgsConstructor
public class JsdwaqrzController {
//    /**
//     * 服务对象
//     */
//    @Autowired
//    private JsdwaqrzService jsdwaqrzService;
//
//    @GetMapping
//    public Rest<List<Jsdwaqrz>> getAllJsdwaqrzs(Jsdwaqrz jsdwaqrz) {
//        return Rest.data(jsdwaqrzService. findJsdwaqrzs(jsdwaqrz));
//    }
//
//    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
//    @GetMapping("list")
//    public FebsResponse JsdwaqrzList(QueryRequest request, Jsdwaqrz jsdwaqrz) {
//        Map<String, Object> dataTable = FebsUtil.getDataTable( jsdwaqrzService. findJsdwaqrzs(request,jsdwaqrz));
//        return new FebsResponse().data(dataTable);
//    }
//
//    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
//    @PostMapping
//    public void addJsdwaqrz(@Valid Jsdwaqrz jsdwaqrz) throws FebsException {
//        try {
//            this.jsdwaqrzService. createJsdwaqrz(jsdwaqrz);
//        } catch (Exception e) {
//            String message = "新增建设单位安全日志失败";
//            log.error(message, e);
//            throw new FebsException(message);
//        }
//    }
//
//    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
//    @DeleteMapping
//    public void deleteJsdwaqrz(Jsdwaqrz jsdwaqrz) throws FebsException {
//        try {
//            this.jsdwaqrzService. deleteJsdwaqrz(jsdwaqrz);
//        } catch (Exception e) {
//            String message = "删除建设单位安全日志失败";
//            log.error(message, e);
//            throw new FebsException(message);
//        }
//    }
//
//    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
//    @PutMapping
//    public void updateJsdwaqrz(Jsdwaqrz jsdwaqrz) throws FebsException {
//        try {
//            this.jsdwaqrzService. updateJsdwaqrz(jsdwaqrz);
//        } catch (Exception e) {
//            String message = "修改建设单位安全日志失败";
//            log.error(message, e);
//            throw new FebsException(message);
//        }
//    }
//
}
