// package cc.mrbird.febs.server.tjdkxm.service;
//
// import cc.mrbird.febs.common.core.entity.MyPage;
// import cc.mrbird.febs.common.core.entity.tjdkxm.Overtime;
// import cc.mrbird.febs.common.core.entity.QueryRequest;
// import cc.mrbird.febs.common.core.exception.FebsException;
// import com.baomidou.mybatisplus.extension.service.IService;
//
// import java.util.List;
/// **
// * 加班申请审批表(Overtime)表服务实现类
// *
// * @author zlkj_cg
// * @since 2022-01-12 15:51:02
// */
// public interface OvertimeService extends IService<Overtime> {
//
// /**
// * 查询（分页）
// *
// * @param request QueryRequest
// * @param overtime 加班申请审批表实体类
// * @return IPage<Overtime>
// */
// MyPage<Overtime> findOvertimes(QueryRequest request, Overtime overtime);
//
// /**
// * 查询（所有）
// *
// * @param overtime 加班申请审批表实体类
// * @return List<Overtime>
// */
// List<Overtime> findOvertimes(Overtime overtime);
//
// /**
// * 新增
// *
// * @param overtime 加班申请审批表实体类
// */
// void createOvertime(Overtime overtime);
//
// /**
// * 修改
// *
// * @param overtime 加班申请审批表实体类
// * @param approval
// */
// void updateOvertime(Overtime overtime, boolean approval) throws FebsException;
//
// /**
// * 删除
// *
// * @param overtime 加班申请审批表实体类
// */
// void deleteOvertime(Overtime overtime) throws FebsException;
// /**
// * 未审批
// *
// * @param userId
// * @param projectId
// * @return {@link Integer}
// */
// Integer notChecked(Long userId, Long projectId);
//
// MyPage<?> findOvertimeApprovals(QueryRequest request, Overtime overtime) throws FebsException;
// }
