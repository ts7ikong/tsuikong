package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.constant.ButtonConstant;
import cc.mrbird.febs.common.core.entity.constant.MenuConstant;
import cc.mrbird.febs.common.core.entity.system.SystemUser;
import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
import cc.mrbird.febs.common.core.entity.tjdkxm.MajorProjectLog;
import cc.mrbird.febs.common.core.entity.tjdkxm.Project;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.config.LogRecordContext;
import cc.mrbird.febs.server.tjdkxm.mapper.MajorProjectLogMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.ProjectMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.UserMapper;
import cc.mrbird.febs.server.tjdkxm.service.CacheableService;
import cc.mrbird.febs.server.tjdkxm.service.MajorProjectLogService;
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

import javax.annotation.processing.FilerException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * MajorProjectLogService实现
 *
 * @author zlkj_cg
 * @date 2022-01-12 15:51:04
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MajorProjectLogServiceImpl extends ServiceImpl<MajorProjectLogMapper, MajorProjectLog>
    implements MajorProjectLogService {

    private final MajorProjectLogMapper majorProjectLogMapper;
    private final CacheableService cacheableService;
    @Autowired
    private LogRecordContext logRecordContext;

    @Override
    public MyPage<MajorProjectLog> findMajorProjectLogs(QueryRequest request, MajorProjectLog.Params majorProjectLog) {
        QueryWrapper<MajorProjectLog> queryWrapper = new QueryWrapper<>();
        AuthUserModel userAuth = cacheableService.getAuthUser(MenuConstant.MAJOR_PROJECT_LOG_ID);
        getParam(queryWrapper, majorProjectLog, userAuth);
        IPage<MajorProjectLog> page = this.majorProjectLogMapper
            .selectPageInfo(new Page<>(request.getPageNum(), request.getPageSize()), queryWrapper);
        return new MyPage<>(page);
    }

    /**
     * 参数封装
     *
     * @param majorProjectLog 实体类
     * @param queryWrapper 查询
     * @param userAuth 用户数据权限
     */

    private void getParam(QueryWrapper<MajorProjectLog> queryWrapper, MajorProjectLog.Params majorProjectLog,
        AuthUserModel userAuth) {
        queryWrapper.eq("IS_DELETE", 0);
        queryWrapper.orderByDesc("MAJOR_ID");
        if (!AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) {
            queryWrapper.in("MAJOR_PROJECTID", userAuth.getProjectIds());
        }
        if (majorProjectLog.getMajorProjectid() != null) {
            queryWrapper.eq("MAJOR_PROJECTID", majorProjectLog.getMajorProjectid());
        }
        if (StringUtils.isNotBlank(majorProjectLog.getMajorTitle())) {
            queryWrapper.and(wapper -> {
                wapper.like("MAJOR_TITLE", majorProjectLog.getMajorTitle());
                wapper.or().inSql("MAJOR_PROJECTID", "select PROJECT_ID from p_project where PROJECT_NAME "
                    + "LIKE CONCAT('%','" + majorProjectLog.getMajorTitle() + "','%')");
            });
        }
        if (StringUtils.isNotBlank(majorProjectLog.getMajorProjectname())) {
            queryWrapper.inSql("MAJOR_PROJECTID", "select PROJECT_ID from p_project where PROJECT_NAME "
                + "LIKE CONCAT('%','" + majorProjectLog.getMajorProjectname() + "','%')");
        }
        if (StringUtils.isNotBlank(majorProjectLog.getMajorStartTime())) {
            queryWrapper.ge("MAJOR_TIME", majorProjectLog.getMajorStartTime());

        }
        if (StringUtils.isNotBlank(majorProjectLog.getMajorEndTime())) {
            queryWrapper.le("MAJOR_TIME", majorProjectLog.getMajorEndTime());
        }
    }

    @Override
    public List<MajorProjectLog> findMajorProjectLogs(MajorProjectLog majorProjectLog) {
        LambdaQueryWrapper<MajorProjectLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MajorProjectLog::getIsDelete, 0);
        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMajorProjectLog(MajorProjectLog majorProjectLog) throws FebsException {
        Long userId = FebsUtil.getCurrentUserId();
        FebsUtil.isProjectNotNull(majorProjectLog.getMajorProjectid());
        cacheableService.hasPermission(userId, MenuConstant.MAJOR_PROJECT_LOG_ID, ButtonConstant.BUTTON_196_ID,
            majorProjectLog.getMajorProjectid());
        majorProjectLog.setCreateUserid(userId);
        majorProjectLog.setCreateTime(new Date());
        this.save(majorProjectLog);
        logRecordContext.putVariable("id", majorProjectLog.getMajorId());
        logRecordContext.putVariable("projectId", majorProjectLog.getMajorProjectid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMajorProjectLog(MajorProjectLog majorProjectLog) throws FebsException {
        MajorProjectLog byId = this.getById(majorProjectLog.getMajorId());
        if (byId == null) {
            throw new FebsException("选择记录");
        }
        cacheableService.hasPermission(byId.getCreateUserid(), MenuConstant.MAJOR_PROJECT_LOG_ID,
            ButtonConstant.BUTTON_197_ID, byId.getMajorProjectid());
        this.updateById(majorProjectLog);
        logRecordContext.putVariable("projectId", byId.getMajorProjectid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMajorProjectLog(MajorProjectLog majorProjectLog) throws FebsException {
        MajorProjectLog byId = this.getById(majorProjectLog.getMajorId());
        if (byId == null) {
            throw new FebsException("选择记录");
        }
        cacheableService.hasPermission(byId.getCreateUserid(), MenuConstant.MAJOR_PROJECT_LOG_ID,
            ButtonConstant.BUTTON_198_ID, majorProjectLog.getMajorProjectid());
        this.update(new LambdaUpdateWrapper<MajorProjectLog>()
            .eq(MajorProjectLog::getMajorId, majorProjectLog.getMajorId()).set(MajorProjectLog::getIsDelete, 1));
        logRecordContext.putVariable("projectId", byId.getMajorProjectid());
    }
}
