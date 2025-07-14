// package cc.mrbird.febs.server.tjdkxm.service.impl;
//
// import cc.mrbird.febs.common.core.entity.MyPage;
// import cc.mrbird.febs.common.core.entity.QueryRequest;
// import cc.mrbird.febs.common.core.entity.constant.MenuConstant;
// import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
// import cc.mrbird.febs.common.core.entity.tjdkxm.Overtime;
// import cc.mrbird.febs.common.core.exception.FebsException;
// import cc.mrbird.febs.common.core.utils.FebsUtil;
// import cc.mrbird.febs.common.core.utils.OrderUtils;
// import cc.mrbird.febs.server.tjdkxm.config.LogRecordContext;
// import cc.mrbird.febs.server.tjdkxm.mapper.OvertimeMapper;
// import cc.mrbird.febs.server.tjdkxm.service.CacheableService;
// import cc.mrbird.febs.server.tjdkxm.service.OvertimeService;
// import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
// import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
// import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
// import com.baomidou.mybatisplus.core.metadata.IPage;
// import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
// import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
// import lombok.RequiredArgsConstructor;
// import org.apache.commons.lang3.StringUtils;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Propagation;
// import org.springframework.transaction.annotation.Transactional;
//
// import java.util.Date;
// import java.util.List;
//
/// **
// * OvertimeService实现
// *
// * @author zlkj_cg
// * @date 2022-01-12 15:51:02
// */
// @Service
// @RequiredArgsConstructor
// @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
// public class OvertimeServiceImpl extends ServiceImpl<OvertimeMapper, Overtime> implements OvertimeService {
//
// private final OvertimeMapper overtimeMapper;
// private final CacheableService cacheableService;
// @Autowired
// private LogRecordContext logRecordContext;
//
// @Override
// public MyPage<Overtime> findOvertimes(QueryRequest request, Overtime overtime) {
// QueryWrapper<Overtime> queryWrapper = new QueryWrapper<>();
// queryWrapper.eq("IS_DELETE", 0);
// Long userId = FebsUtil.getCurrentUserId();
// getParams(request, overtime, queryWrapper);
// queryWrapper.orderByDesc("OVERTIME_CREATETIME");
// AuthUserModel userAuth = cacheableService.getUserAuth(userId);
// if (!AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) {
// queryWrapper.eq("OVERTIME_CREATEUSERID", userId);
// }
// return getOvertimeMyPage(request, queryWrapper);
// }
//
// private MyPage<Overtime> getOvertimeMyPage(QueryRequest request, QueryWrapper<Overtime> queryWrapper) {
// IPage<Overtime> page = this.overtimeMapper.selectPageInfo(new Page<>(request.getPageNum(),
// request.getPageSize()), queryWrapper);
// return new MyPage<>(page);
// }
//
// private void getParams(QueryRequest request, Overtime overtime, QueryWrapper<Overtime> queryWrapper) {
// queryWrapper.orderByAsc("OVERTIME_CHECKSTATE");
// OrderUtils.setQuseryOrder(queryWrapper, request);
// if (StringUtils.isNotEmpty(overtime.getOvertimeCause())) {
// queryWrapper.like("OVERTIME_CAUSE", overtime.getOvertimeCause());
// }
// if (StringUtils.isNotEmpty(overtime.getStartCreateTime())) {
// queryWrapper.ge("OVERTIME_CREATETIME", overtime.getStartCreateTime());
// }
// if (StringUtils.isNotEmpty(overtime.getEndCreateTime())) {
// queryWrapper.le("OVERTIME_CREATETIME", overtime.getEndCreateTime());
// }
// if (StringUtils.isNotEmpty(overtime.getOvertimeCheckstate())) {
// queryWrapper.eq("OVERTIME_CHECKSTATE", overtime.getOvertimeCheckstate());
// }
// }
//
// @Override
// public MyPage<?> findOvertimeApprovals(QueryRequest request, Overtime overtime) throws FebsException {
// QueryWrapper<Overtime> queryWrapper = new QueryWrapper<>();
// queryWrapper.eq("IS_DELETE", 0);
// getParams(request, overtime, queryWrapper);
// queryWrapper.orderByAsc("OVERTIME_CREATETIME");
// cacheableService.hasPermission(null,MenuConstant.OVERTIME_APPROVAL_ID);
// return getOvertimeMyPage(request, queryWrapper);
// }
//
// @Override
// public List<Overtime> findOvertimes(Overtime overtime) {
// LambdaQueryWrapper<Overtime> queryWrapper = new LambdaQueryWrapper<>();
// queryWrapper.eq(Overtime::getIsDelete, 0);
// return this.list(queryWrapper);
// }
//
// @Override
// @Transactional(rollbackFor = Exception.class)
// public void createOvertime(Overtime overtime) {
// overtime.setOvertimeCreateuserid(FebsUtil.getCurrentUserId());
// overtime.setOvertimeCreatetime(new Date());
// overtime.setOvertimeCheckstate("0");
// this.save(overtime);
// logRecordContext.putVariable("id", overtime.getOvertimeId());
//
// }
//
// @Override
// @Transactional(rollbackFor = Exception.class)
// public void updateOvertime(Overtime overtime, boolean approval) throws FebsException {
// Overtime overtime1 = overtimeMapper.selectById(overtime.getOvertimeId());
// if ("1".equals(overtime1.getOvertimeCheckstate())) {
// throw new FebsException("已审批不可修改");
// }
// if (approval) {
// Long userId = FebsUtil.getCurrentUserId();
// if (cacheableService.hasNotPermission(MenuConstant.OVERTIME_APPROVAL_ID)) {
// throw new FebsException("权限不足");
// }
// if (StringUtils.isNotBlank(overtime.getOvertimeCheckstate())) {
// overtime.setOvertimeCheckuserid(userId);
// overtime.setOvertimeChecktime(new Date());
// } else {
// throw new FebsException("审批出错了");
// }
// } else {
// if (!cacheableService.isDeleteAuth(overtime1.getOvertimeCreateuserid())) {
// throw new FebsException("权限不足");
// }
// }
//
// this.updateById(overtime);
// }
//
// @Override
// @Transactional(rollbackFor = Exception.class)
// public void deleteOvertime(Overtime overtime) throws FebsException {
// LambdaQueryWrapper<Overtime> wrapper = new LambdaQueryWrapper<>();
// Overtime overtime1 = overtimeMapper.selectById(overtime.getOvertimeId());
// if ("1".equals(overtime1.getOvertimeCheckstate())) {
// throw new FebsException("已审批不可修改");
// }
// if (!cacheableService.isDeleteAuth(overtime1.getOvertimeCreateuserid())) {
// throw new FebsException("权限不足");
// }
// this.update(new LambdaUpdateWrapper<Overtime>().eq(Overtime::getOvertimeId, overtime.getOvertimeId())
// .set(Overtime::getIsDelete, 1));
// }
//
// @Override
// public Integer notChecked(Long userId, Long projectId) {
// return overtimeMapper.selectCount(
// new LambdaQueryWrapper<Overtime>()
// .select(Overtime::getOvertimeId)
// .eq(Overtime::getOvertimeCheckstate, 0)
// .eq(Overtime::getIsDelete, 0)
// );
// }
//
//
// }
