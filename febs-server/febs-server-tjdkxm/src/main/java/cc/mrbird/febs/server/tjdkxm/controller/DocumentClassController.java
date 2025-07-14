package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.annotation.AuthMenu;
import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Datamanagement;
import cc.mrbird.febs.common.core.entity.tjdkxm.DocumentClass;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.mapper.DatamanagementMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.DocumentClassMapper;
import cc.mrbird.febs.server.tjdkxm.service.DocumentClassService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 资料管理(DocumentClass)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:03
 */

@Slf4j
@Validated
@RestController
@Api(tags = "资料管理(DocumentClass)控制层")
@RequestMapping("/documentClass")
@RequiredArgsConstructor
public class DocumentClassController {
    /**
     * 服务对象
     */
    @Autowired
    private DocumentClassService documentClassService;

    @GetMapping("/menu")
    @ApiOperation(value = "查询 资料", notes = "只执行新增，后端不进行任何处理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "documentclassMenu", value = "对应菜单的type", required = true, dataType = "String"),
            @ApiImplicitParam(name = "documentclassName", value = "分类名称", required = false, dataType = "String"),})
    public FebsResponse getAllDocumentClasss(String documentclassMenu, String documentclassName) {
        return new FebsResponse().data(documentClassService.findDocumentClasss(documentclassMenu, documentclassName));
    }

    @GetMapping("temp/menu")
    @ApiOperation(value = "查询 模板", notes = "只执行新增，后端不进行任何处理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "documentclassMenu", value = "对应菜单的type", required = true, dataType = "String"),
            @ApiImplicitParam(name = "documentclassName", value = "分类名称", required = false, dataType = "String"),})
    public FebsResponse getAllDocumentTempClasss(String documentclassMenu, String documentclassName)
            throws FebsException {
        return new FebsResponse()
                .data(documentClassService.findDocumentTempClasss(documentclassMenu, documentclassName));
    }

    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    @OperateLog(mapper = DocumentClassMapper.class, className = DocumentClass.class, type = Operate.TYPE_ADD)
    public void addDocumentClass(@Valid DocumentClass.Add documentClass) throws FebsException {
        this.documentClassService.createDocumentClass(documentClass);
    }

    @ApiOperation(value = "新增接口--temp", notes = "只执行新增，后端不进行任何处理")
    @PostMapping("temp")
    @OperateLog(mapper = DocumentClassMapper.class, className = DocumentClass.class, type = Operate.TYPE_ADD)
    public FebsResponse addDocumentTempClass(@Valid DocumentClass.Add documentClass) throws FebsException {
        try {
            this.documentClassService.createDocumenTempClass(documentClass);
            return new FebsResponse().message("删除成功");
        } catch (Exception e) {
            String message = "新增资料管理失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @DeleteMapping
    @OperateLog(mapper = DocumentClassMapper.class, className = DocumentClass.class, type = Operate.TYPE_DELETE)
    public void deleteDocumentClass(DocumentClass documentClass) throws FebsException {
        try {
            this.documentClassService.deleteDocumentClass(documentClass);
        } catch (Exception e) {
            if (e instanceof FebsException) {
                throw new FebsException(e.getMessage());
            }
            String message = "删除资料管理失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    @OperateLog(mapper = DocumentClassMapper.class, className = DocumentClass.class, type = Operate.TYPE_MODIFY)
    @AuthMenu(value = 36, buttonId = 261)
    public void updateDocumentClass(DocumentClass documentClass) throws FebsException {
        this.documentClassService.updateDocumentClass(documentClass);
    }

}
