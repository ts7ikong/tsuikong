package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.tjdkxm.Reporting;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 工作汇报审批表(Reporting)表服务实现类
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:05
 */
public interface ReportingService extends IService<Reporting> {

    /**
     * 查询 本人（分页）
     *
     * @param request   QueryRequest
     * @param reporting 工作汇报审批表实体类
     * @return IPage<Reporting>
     */
    MyPage<Reporting> findReportings(QueryRequest request, Reporting reporting);

    /**
     * 查询待审批 [需要有审批权限]
     *
     * @param request
     * @param reporting
     * @return {@link com.baomidou.mybatisplus.core.metadata.IPage<cc.mrbird.febs.common.core.entity.tjdkxm.Reporting>}
     */
    MyPage<Reporting> findReportingsApproval(QueryRequest request, Reporting reporting) throws FebsException;

    /**
     * 查询（所有）
     *
     * @param reporting 工作汇报审批表实体类
     * @return List<Reporting>
     */
    List<Reporting> findReportings(Reporting reporting);

    /**
     * 新增
     *
     * @param reporting 工作汇报审批表实体类
     */
    void createReporting(Reporting reporting);

    /**
     * 修改
     *
     * @param reporting 工作汇报审批表实体类
     */
    void updateReporting(Reporting reporting,boolean approval) throws FebsException;

    /**
     * 删除
     *
     * @param reporting 工作汇报审批表实体类
     */
    void deleteReporting(Reporting reporting) throws FebsException;

    /**
     * 未审批
     *
     * @param userId
     * @param projectId
     * @return {@link Integer}
     */
    Integer notChecked(Long userId, Long projectId);
}
