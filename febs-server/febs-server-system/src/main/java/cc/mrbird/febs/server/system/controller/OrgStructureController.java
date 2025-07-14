package cc.mrbird.febs.server.system.controller;

import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.system.OrgStructure;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.system.mapper.OrgStructureMapper;
import cc.mrbird.febs.server.system.service.IOrgStructureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author MrBird
 */
@Slf4j
@Validated
@RestController
@Api(tags = "系统管理-组织结构")
@RequestMapping("structure")
public class OrgStructureController {
    @Autowired
    private IOrgStructureService orgStructureService;

    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    public FebsResponse orgStructureList(QueryRequest request, OrgStructure.Params orgStructure) {
        Map<String, Object> dataTable = FebsUtil.getDataTable(orgStructureService.findOrgStructures(request, orgStructure));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    @OperateLog(mapper = OrgStructureMapper.class, className = OrgStructure.class, type = Operate.TYPE_ADD)
    public void addOrgStructure(@Valid OrgStructure orgStructure) throws FebsException {
        try {
            this.orgStructureService.createOrgStructure(orgStructure);
        } catch (Exception e) {
            String message = "新增组织结构失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
    @DeleteMapping
    @OperateLog(mapper = OrgStructureMapper.class, className = OrgStructure.class, type = Operate.TYPE_DELETE)

    public void deleteOrgStructure(OrgStructure orgStructure) throws FebsException {
        try {
            this.orgStructureService.deleteOrgStructure(orgStructure);
        } catch (FebsException e1) {
            throw new FebsException(e1.getMessage());
        } catch (Exception e) {
            String message = "删除组织结构失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    @OperateLog(mapper = OrgStructureMapper.class, className = OrgStructure.class, type = Operate.TYPE_MODIFY)

    public void updateOrgStructure(OrgStructure orgStructure) throws FebsException {
        try {
            this.orgStructureService.updateOrgStructure(orgStructure);
        } catch (FebsException e1) {
            throw new FebsException(e1.getMessage());
        } catch (Exception e) {
            String message = "修改组织结构失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

}
