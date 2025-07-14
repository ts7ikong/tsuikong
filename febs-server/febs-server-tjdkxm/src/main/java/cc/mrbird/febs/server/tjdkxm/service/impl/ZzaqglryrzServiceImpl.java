package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.constant.ButtonConstant;
import cc.mrbird.febs.common.core.entity.constant.MenuConstant;
import cc.mrbird.febs.common.core.entity.system.SystemUser;
import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
import cc.mrbird.febs.common.core.entity.tjdkxm.Project;
import cc.mrbird.febs.common.core.entity.tjdkxm.Zzaqglryrz;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.config.LogRecordContext;
import cc.mrbird.febs.server.tjdkxm.mapper.ProjectMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.UserMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.ZzaqglryrzMapper;
import cc.mrbird.febs.server.tjdkxm.service.CacheableService;
import cc.mrbird.febs.server.tjdkxm.service.ZzaqglryrzService;
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * ZzaqglryrzService实现
 *
 * @author zlkj_cg
 * @date 2022-01-12 15:51:08
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ZzaqglryrzServiceImpl extends ServiceImpl<ZzaqglryrzMapper, Zzaqglryrz> implements ZzaqglryrzService {

    private final CacheableService cacheableService;
    private final ZzaqglryrzMapper zzaqglryrzMapper;
    @Autowired
    private LogRecordContext logRecordContext;

    @Override
    public MyPage<Zzaqglryrz> findZzaqglryrzs(QueryRequest request, Zzaqglryrz zzaqglryrz) {
        QueryWrapper<Zzaqglryrz> queryWrapper = new QueryWrapper<>();
        AuthUserModel userAuth = cacheableService.getAuthUser(MenuConstant.PERSONNEL_ID);

        if (AuthUserModel.KEY_NONE.equals(userAuth.getKey())) {
            return new MyPage<>();
        }
        getParam(zzaqglryrz, queryWrapper, userAuth);
        IPage<Zzaqglryrz> page =
            this.zzaqglryrzMapper.selectPageInfo(new Page<>(request.getPageNum(), request.getPageSize()), queryWrapper);
        return new MyPage<>(page);
    }

    /**
     * 参数封装
     *
     * @param zzaqglryrz 实体类
     * @param queryWrapper 查询
     * @param userAuth 用户数据权限
     */
    private void getParam(Zzaqglryrz zzaqglryrz, QueryWrapper<Zzaqglryrz> queryWrapper, AuthUserModel userAuth) {
        queryWrapper.eq("IS_DELETE", 0);
        queryWrapper.orderByDesc("ZZAQGLRYRZ_CREATETIME");
        if (!AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) {
            queryWrapper.in("ZZAQGLRYRZ_PROJECTID", userAuth.getProjectIds());
        }
        if (zzaqglryrz.getZzaqglryrzProjectid() != null) {
            queryWrapper.eq("ZZAQGLRYRZ_PROJECTID", zzaqglryrz.getZzaqglryrzProjectid());
        }
        if (StringUtils.isNotBlank(zzaqglryrz.getZzaqglryrzMan())) {
            queryWrapper.and(wapper -> {
                wapper.like("ZZAQGLRYRZ_MAN", zzaqglryrz.getZzaqglryrzMan());
                // 级联项目名称
                if (!AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) {
                    wapper.or().inSql("ZZAQGLRYRZ_PROJECTID", "select PROJECT_ID from p_project where PROJECT_NAME "
                        + "LIKE CONCAT('%','" + zzaqglryrz.getZzaqglryrzCase() + "','%')");
                }
            });
        }
        if (StringUtils.isNotBlank(zzaqglryrz.getZzaqglryrzCase())) {
            queryWrapper.and(wapper -> {
                wapper.like("ZZAQGLRYRZ_MAN", zzaqglryrz.getZzaqglryrzCase());
                wapper.or().like("ZZAQGLRYRZ_CASE", zzaqglryrz.getZzaqglryrzCase());
                // 级联项目名称
                if (!AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) {
                    wapper.or().inSql("ZZAQGLRYRZ_PROJECTID", "select PROJECT_ID from p_project where PROJECT_NAME "
                        + "LIKE CONCAT('%','" + zzaqglryrz.getZzaqglryrzCase() + "','%')");
                }
            });
        }
        if (StringUtils.isNotBlank(zzaqglryrz.getProjectName())) {
            queryWrapper.inSql("ZZAQGLRYRZ_PROJECTID", "select PROJECT_ID from p_project where PROJECT_NAME "
                + "LIKE CONCAT('%','" + zzaqglryrz.getProjectName() + "','%')");
        }
        if (StringUtils.isNotBlank(zzaqglryrz.getStartCreateTime())) {
            queryWrapper.ge("ZZAQGLRYRZ_CREATETIME", zzaqglryrz.getStartCreateTime());
        }
        if (StringUtils.isNotBlank(zzaqglryrz.getEndCreateTime())) {
            queryWrapper.le("ZZAQGLRYRZ_CREATETIME", zzaqglryrz.getEndCreateTime());
        }
    }

    @Override
    public List<Zzaqglryrz> findZzaqglryrzs(Zzaqglryrz zzaqglryrz) {
        QueryWrapper<Zzaqglryrz> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("IS_DELETE", 0);
        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createZzaqglryrz(Zzaqglryrz zzaqglryrz) throws FebsException {
        Long userId = FebsUtil.getCurrentUserId();
        FebsUtil.isProjectNotNull(zzaqglryrz.getZzaqglryrzProjectid());
        cacheableService.hasPermission(userId, MenuConstant.PERSONNEL_ID, ButtonConstant.BUTTON_199_ID,
            zzaqglryrz.getZzaqglryrzProjectid());
        zzaqglryrz.setZzaqglryrzCreatetime(new Date());
        zzaqglryrz.setZzaqglryrzMan(FebsUtil.getCurrentRealname());
        zzaqglryrz.setZzaqglryrzManid(userId);
        zzaqglryrz.setZzaqglryrzCreatetime(new Date());
        this.save(zzaqglryrz);
        logRecordContext.putVariable("id", zzaqglryrz.getZzaqglryrzId());
        logRecordContext.putVariable("projectId", zzaqglryrz.getZzaqglryrzProjectid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateZzaqglryrz(Zzaqglryrz zzaqglryrz) throws FebsException {
        Zzaqglryrz byId = this.getById(zzaqglryrz.getZzaqglryrzId());
        if (byId == null) {
            throw new FebsException("数据错误");
        }
        cacheableService.hasPermission(byId.getZzaqglryrzManid(), MenuConstant.PERSONNEL_ID,
            ButtonConstant.BUTTON_200_ID, byId.getZzaqglryrzProjectid());
        logRecordContext.putVariable("projectId", byId.getZzaqglryrzProjectid());
        this.updateById(zzaqglryrz);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteZzaqglryrz(Zzaqglryrz zzaqglryrz) throws FebsException {
        Zzaqglryrz byId = this.getById(zzaqglryrz.getZzaqglryrzId());
        if (byId == null) {
            throw new FebsException("数据错误");
        }
        cacheableService.hasPermission(byId.getZzaqglryrzManid(), MenuConstant.PERSONNEL_ID,
            ButtonConstant.BUTTON_201_ID, byId.getZzaqglryrzProjectid());
        this.update(null, new LambdaUpdateWrapper<Zzaqglryrz>()
            .eq(Zzaqglryrz::getZzaqglryrzId, zzaqglryrz.getZzaqglryrzId()).set(Zzaqglryrz::getIsDelete, 1));
        logRecordContext.putVariable("projectId", byId.getZzaqglryrzProjectid());
    }
}
