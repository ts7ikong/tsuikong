package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.constant.ButtonConstant;
import cc.mrbird.febs.common.core.entity.constant.MenuConstant;
import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
import cc.mrbird.febs.common.core.entity.tjdkxm.Reporting;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.common.core.utils.OrderUtils;
import cc.mrbird.febs.server.tjdkxm.config.LogRecordContext;
import cc.mrbird.febs.server.tjdkxm.mapper.ReportingMapper;
import cc.mrbird.febs.server.tjdkxm.service.CacheableService;
import cc.mrbird.febs.server.tjdkxm.service.ReportingService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * ReportingService实现
 *
 * @author zlkj_cg
 * @date 2022-01-12 15:51:05
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ReportingServiceImpl extends ServiceImpl<ReportingMapper, Reporting> implements ReportingService {

    private final ReportingMapper reportingMapper;
    private final CacheableService cacheableService;
    @Autowired
    private LogRecordContext logRecordContext;

    @Override
    public MyPage<Reporting> findReportings(QueryRequest request, Reporting reporting) {
        QueryWrapper<Reporting> queryWrapper = new QueryWrapper<>();
        Long userId = FebsUtil.getCurrentUserId();
        getParams(request, reporting, queryWrapper);
        AuthUserModel userAuth = cacheableService.getAuthUser(MenuConstant.WORK_REPORT_ID);
        if (!AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) {
            queryWrapper.eq("REPORTING_USERID", userId);
        }
        if (StringUtils.isNotBlank(reporting.getReportingTitle())) {
            queryWrapper.and(wapper -> {
                wapper.like("REPORTING_TITLE", reporting.getReportingTitle());
                wapper.or().inSql("REPORTING_CHECKUSERID", "select USER_ID from t_user where REALNAME "
                    + "LIKE CONCAT('%','" + reporting.getReportingTitle() + "','%')");
            });
            reporting.setReportingTitle(null);
        }
        queryWrapper.orderByDesc("REPORTING_TIME");
        return getReportingPage(request, queryWrapper);
    }

    @Override
    public MyPage<Reporting> findReportingsApproval(QueryRequest request, Reporting reporting) throws FebsException {
        QueryWrapper<Reporting> queryWrapper = new QueryWrapper<>();
        Long userId = FebsUtil.getCurrentUserId();
        getParams(request, reporting, queryWrapper);
        AuthUserModel userAuth = cacheableService.getAuthUser(MenuConstant.WORK_APPROVAL_ID);
        if (!AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) {
            queryWrapper.eq("REPORTING_CHECKUSERID", userId);
        }
        if (StringUtils.isNotBlank(reporting.getReportingTitle())) {
            queryWrapper.and(wapper -> {
                wapper.like("REPORTING_TITLE", reporting.getReportingTitle());
                wapper.or().inSql("REPORTING_USERID", "select USER_ID from t_user where REALNAME " + "LIKE CONCAT('%','"
                    + reporting.getReportingTitle() + "','%')");
            });
            reporting.setReportingTitle(null);
        }
        queryWrapper.orderByAsc("REPORTING_TIME");
        return getReportingPage(request, queryWrapper);
    }

    private void getParams(QueryRequest request, Reporting reporting, QueryWrapper<Reporting> queryWrapper) {
        queryWrapper.orderByAsc("REPORTING_STATE");
        queryWrapper.eq("IS_DELETE", 0);
        OrderUtils.setQuseryOrder(queryWrapper, request);
        if (StringUtils.isNotBlank(reporting.getReportingState())) {
            queryWrapper.eq("REPORTING_STATE", reporting.getReportingState());
        }
        if (StringUtils.isNotBlank(reporting.getStartTime())) {
            queryWrapper.ge("REPORTING_TIME", reporting.getStartTime());
        }
        if (StringUtils.isNotBlank(reporting.getEndTime())) {
            queryWrapper.le("REPORTING_TIME", reporting.getEndTime());
        }
    }

    private MyPage<Reporting> getReportingPage(QueryRequest request, QueryWrapper<Reporting> queryWrapper) {
        IPage<Reporting> page =
            this.reportingMapper.selectPageInfo(new Page<>(request.getPageNum(), request.getPageSize()), queryWrapper);
        return new MyPage<>(page);
    }

    @Override
    public List<Reporting> findReportings(Reporting reporting) {
        LambdaQueryWrapper<Reporting> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Reporting::getIsDelete, 0);
        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createReporting(Reporting reporting) {
        reporting.setReportingTime(new Date());
        reporting.setReportingUserid(FebsUtil.getCurrentUserId());
        reporting.setReportingState("0");
        this.save(reporting);
        logRecordContext.putVariable("id", reporting.getReportingId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateReporting(Reporting reporting, boolean approval) throws FebsException {
        Reporting reporting1 = reportingMapper.selectById(reporting.getReportingId());
        if (reporting1 == null) {
            throw new FebsException("请选择数据");
        }
        if ("1".equals(reporting1.getReportingState())) {
            throw new FebsException("已审批不可修改和再次审批");
        }
        if (approval) {
            cacheableService.hasPermission(reporting1.getReportingCheckuserid(), MenuConstant.WORK_APPROVAL_ID,
                ButtonConstant.BUTTON_205_ID, null);
            Reporting reporting2 = new Reporting();
            reporting2.setReportingState("1");
            reporting2.setReportingChecktime(new Date());
            reporting2.setReportingId(reporting.getReportingId());
            reporting2.setReportingResult(reporting.getReportingResult());
            reporting = reporting2;
        } else {
            cacheableService.hasPermission(reporting1.getReportingUserid(), MenuConstant.WORK_REPORT_ID,
                ButtonConstant.BUTTON_203_ID, null);
        }
        this.updateById(reporting);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReporting(Reporting reporting) throws FebsException {
        Reporting reporting1 = reportingMapper.selectById(reporting.getReportingId());
        if ("1".equals(reporting1.getReportingState())) {
            throw new FebsException("已审批不可修改和再次审批");
        }
        cacheableService.hasPermission(reporting1.getReportingUserid(), MenuConstant.WORK_REPORT_ID,
            ButtonConstant.BUTTON_204_ID, null);

        this.update(new LambdaUpdateWrapper<Reporting>().eq(Reporting::getReportingId, reporting.getReportingId())
            .set(Reporting::getIsDelete, 1));
    }

    @Override
    public Integer notChecked(Long userId, Long projectId) {
        return reportingMapper.selectCount(new LambdaQueryWrapper<Reporting>().select(Reporting::getReportingId)
            .eq(Reporting::getReportingCheckuserid, userId)
            // 0未检查
            .eq(Reporting::getReportingState, 0).eq(Reporting::getIsDelete, 0));
    }
}
