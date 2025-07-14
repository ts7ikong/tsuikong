package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.constant.ButtonConstant;
import cc.mrbird.febs.common.core.entity.constant.MenuConstant;
import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
import cc.mrbird.febs.common.core.entity.tjdkxm.Danger;
import cc.mrbird.febs.common.core.entity.tjdkxm.Project;
import cc.mrbird.febs.common.core.entity.tjdkxm.UnitEngine;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.common.core.utils.OrderUtils;
import cc.mrbird.febs.server.tjdkxm.config.LogRecordContext;
import cc.mrbird.febs.server.tjdkxm.mapper.*;
import cc.mrbird.febs.server.tjdkxm.service.CacheableService;
import cc.mrbird.febs.server.tjdkxm.service.DangerService;
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

import java.util.*;
import java.util.stream.Collectors;

/**
 * DangerService实现
 *
 * @author zlkj_cg
 * @date 2022-01-12 15:51:02
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DangerServiceImpl extends ServiceImpl<DangerMapper, Danger> implements DangerService {

    private final DangerMapper dangerMapper;
    private final CacheableService cacheableService;
    @Autowired
    private LogRecordContext logRecordContext;

    @Override
    public MyPage<Danger> findDangers(QueryRequest request, Danger.Params danger) {
        QueryWrapper<Danger> queryWrapper = new QueryWrapper<>();
        AuthUserModel userAuth = cacheableService.getAuthUser(MenuConstant.INSTALLATIONS_MAJOR_ID);
        Set<Long> projectIds = userAuth.getProjectIds();
        if (AuthUserModel.KEY_NONE.equals(userAuth.getKey())) {
            return new MyPage<>();
        }
        if (!AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) {
            queryWrapper.in("DANGER_PROJECTID", projectIds);
        }
        gtParam(request, danger, queryWrapper);
        IPage<Danger> page =
            this.dangerMapper.selectPageInfo(new Page<>(request.getPageNum(), request.getPageSize()), queryWrapper);
        return new MyPage<>(page);
    }

    /**
     * 参数封装
     *
     * @param request
     * @param danger
     * @param queryWrapper
     */
    private void gtParam(QueryRequest request, Danger.Params danger, QueryWrapper<Danger> queryWrapper) {
        queryWrapper.eq("IS_DELETE", 0);
        queryWrapper.orderByDesc("DANGER_RECORDTIME");
        // 记录日期
        if (StringUtils.isNotEmpty(danger.getStartRecordtime())) {
            queryWrapper.ge("DANGER_RECORDTIME", danger.getStartRecordtime());
        }
        if (StringUtils.isNotEmpty(danger.getEndRecordtime())) {
            queryWrapper.le("DANGER_RECORDTIME", danger.getEndRecordtime());
        }
        if (StringUtils.isNotEmpty(danger.getDangerType())) {
            queryWrapper.eq("DANGER_TYPE", danger.getDangerType());
        }
        if (StringUtils.isNotEmpty(danger.getDangerLevel())) {
            queryWrapper.eq("DANGER_LEVEL", danger.getDangerLevel());
        }
        if (StringUtils.isNotBlank(danger.getPscName())) {
            queryWrapper.and(wrapper -> {
                String pscName = danger.getPscName();
                wrapper.like("DANGER_NAME", pscName);
                wrapper.or().inSql("DANGER_PROJECTID",
                    "SELECT PROJECT_ID from p_project where IS_DELETE=0 and PROJECT_NAME like CONCAT('%','" + pscName
                        + "','%')");
                // wrapper.or().inSql("DANGER_UNITENGINEID",
                // "select UNIT_ID from p_unitengine where IS_DELETE=0 and UNIT_NAME like CONCAT('%','" + pscName +
                // "','%')");
                // wrapper.or().like("DANGER_PARCELID",
                // "SELECT PARCEL_ID from p_parcel where IS_DELETE=0 and PARCEL_NAME LIKE CONCAT('%','" + pscName +
                // "','%')");
                // wrapper.or().like("DANGER_SUBITEMID",
                // "SELECT SUBITEM_ID from p_subitem where IS_DELETE=0 and SUBITEM_NAME LIKE CONCAT('%','" + pscName +
                // "','%')");
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDanger(Danger danger) throws FebsException {
        FebsUtil.isProjectNotNull(danger.getDangerProjectid());
        danger.setDangerRecorduserid(FebsUtil.getCurrentUserId());
        danger.setDangerRecordtime(new Date());
        this.save(danger);
        logRecordContext.putVariable("id", danger.getDangerId());
        logRecordContext.putVariable("projectId", danger.getDangerProjectid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDanger(Danger danger) throws FebsException {
        Danger danger1 = dangerMapper.selectById(danger.getDangerId());
        cacheableService.hasPermission(null, MenuConstant.INSTALLATIONS_MAJOR_ID, ButtonConstant.BUTTON_191_ID,
            danger1.getDangerProjectid());
        this.updateById(danger);
        logRecordContext.putVariable("projectId", danger1.getDangerProjectid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDanger(Danger danger) throws FebsException {
        Danger danger1 = dangerMapper.selectById(danger.getDangerId());
        cacheableService.hasPermission(null, MenuConstant.INSTALLATIONS_MAJOR_ID, ButtonConstant.BUTTON_191_ID,
            danger1.getDangerProjectid());
        this.update(new LambdaUpdateWrapper<Danger>().eq(Danger::getDangerId, danger.getDangerId())
            .set(Danger::getIsDelete, 1));
        logRecordContext.putVariable("projectId", danger1.getDangerProjectid());
    }
}
