// package cc.mrbird.febs.server.tjdkxm.controller;
//
// import cc.mrbird.febs.common.core.annotation.AuthMenu;
// import cc.mrbird.febs.common.core.annotation.OperateLog;
// import cc.mrbird.febs.common.core.entity.FebsResponse;
// import cc.mrbird.febs.common.core.entity.Operate;
// import cc.mrbird.febs.common.core.entity.QueryRequest;
// import cc.mrbird.febs.common.core.entity.Rest;
// import cc.mrbird.febs.common.core.entity.tjdkxm.MajorProjectLog;
// import cc.mrbird.febs.common.core.entity.tjdkxm.Overtime;
// import cc.mrbird.febs.common.core.exception.FebsException;
// import cc.mrbird.febs.common.core.utils.FebsUtil;
// import cc.mrbird.febs.server.tjdkxm.mapper.MajorProjectLogMapper;
// import cc.mrbird.febs.server.tjdkxm.mapper.OvertimeMapper;
// import cc.mrbird.febs.server.tjdkxm.service.OvertimeService;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.validation.annotation.Validated;
// import org.springframework.web.bind.annotation.*;
//
// import javax.validation.Valid;
// import java.util.List;
// import java.util.Map;
//
/// **
// * 加班申请审批表(Overtime)表控制层
// *
// * @author zlkj_cg
// * @since 2022-01-12 15:51:02
// */
//
// @Slf4j
// @Validated
// @RestController
// @Api(tags = "加班申请审批表(Overtime)控制层")
// @RequestMapping("/overtime")
// @RequiredArgsConstructor
// public class OvertimeController {
// /**
// * 服务对象
// */
// @Autowired
// private OvertimeService overtimeService;
//
// @GetMapping
// public Rest<List<Overtime>> getAllOvertimes(Overtime overtime) {
// return Rest.data(overtimeService.findOvertimes(overtime));
// }
//
// @ApiOperation(value = "分页查询", notes = "暂无查询条件")
// @GetMapping("list")
// public FebsResponse overtimeList(QueryRequest request, Overtime overtime) {
// Map<String, Object> dataTable = FebsUtil.getDataTable(overtimeService.findOvertimes(request, overtime));
// return new FebsResponse().data(dataTable);
// }
// @ApiOperation(value = "分页查询", notes = "暂无查询条件")
// @GetMapping("list/approval")
// public FebsResponse overtimeApprovalList(QueryRequest request, Overtime overtime) throws FebsException {
// Map<String, Object> dataTable = FebsUtil.getDataTable(overtimeService.findOvertimeApprovals(request, overtime));
// return new FebsResponse().data(dataTable);
// }
//
// @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
// @PostMapping
// @OperateLog(mapper = OvertimeMapper.class, className = Overtime.class, type = Operate.TYPE_ADD)
//
// public void addOvertime(@Valid Overtime overtime) throws FebsException {
// try {
// this.overtimeService.createOvertime(overtime);
// } catch (Exception e) {
// String message = "新增加班申请审批表失败";
// log.error(message, e);
// throw new FebsException(message);
// }
// }
//
// @ApiOperation(value = "删除接口", notes = "根据id删除一条数据，只执行删除操作")
// @DeleteMapping
// @OperateLog(mapper = OvertimeMapper.class, className = Overtime.class, type = Operate.TYPE_DELETE)
//
// public void deleteOvertime(Overtime overtime) throws FebsException {
// try {
// this.overtimeService.deleteOvertime(overtime);
// } catch (FebsException e1) {
// throw new FebsException(e1.getMessage());
// } catch (Exception e) {
// String message = "删除加班申请审批表失败";
// log.error(message, e);
// throw new FebsException(message);
// }
// }
//
// @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
// @PutMapping
// @OperateLog(mapper = OvertimeMapper.class, className = Overtime.class, type = Operate.TYPE_MODIFY)
//
// public void updateOvertime(Overtime overtime) throws FebsException {
// try {
// this.overtimeService.updateOvertime(overtime,false);
// } catch (FebsException e1) {
// throw new FebsException(e1.getMessage());
// } catch (Exception e) {
// String message = "修改加班申请审批表失败";
// log.error(message, e);
// throw new FebsException(message);
// }
// }
// @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
// @PutMapping("approval")
// @OperateLog(mapper = OvertimeMapper.class, className = Overtime.class, type = Operate.TYPE_APPROVAL)
// public void updateOvertimeApproval(Overtime overtime) throws FebsException {
// try {
// this.overtimeService.updateOvertime(overtime,true);
// } catch (FebsException e1) {
// throw new FebsException(e1.getMessage());
// } catch (Exception e) {
// String message = "修改加班申请审批表失败";
// log.error(message, e);
// throw new FebsException(message);
// }
// }
//
// }
