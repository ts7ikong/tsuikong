package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.constant.ButtonConstant;
import cc.mrbird.febs.common.core.entity.constant.MenuConstant;
import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
import cc.mrbird.febs.common.core.entity.tjdkxm.Project;
import cc.mrbird.febs.common.core.entity.tjdkxm.Safeplan;
import cc.mrbird.febs.common.core.entity.tjdkxm.Sgrz;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.common.core.utils.OrderUtils;
import cc.mrbird.febs.server.tjdkxm.config.LogRecordContext;
import cc.mrbird.febs.server.tjdkxm.mapper.ProjectMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.SgrzMapper;
import cc.mrbird.febs.server.tjdkxm.service.CacheableService;
import cc.mrbird.febs.server.tjdkxm.service.SgrzService;
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SgrzService实现
 *
 * @author zlkj_cg
 * @date 2022-01-12 15:51:07
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SgrzServiceImpl extends ServiceImpl<SgrzMapper, Sgrz> implements SgrzService {

    private final CacheableService cacheableService;
    private final SgrzMapper sgrzMapper;
    private final ProjectMapper projectMapper;
    @Autowired
    private LogRecordContext logRecordContext;

    @Override
    public MyPage<Sgrz> findSgrzs(QueryRequest request, Sgrz sgrz) {
        QueryWrapper<Sgrz> queryWrapper = new QueryWrapper<>();
        AuthUserModel userAuth = cacheableService.getAuthUser(MenuConstant.CONSTRUCTION_ID);
        if (getParams(request, sgrz, queryWrapper, userAuth)) {
            return new MyPage<>();
        }
        IPage<Sgrz> page = this.sgrzMapper.selectPageInfo(new Page<>(request.getPageNum(), request.getPageSize()), queryWrapper);
        if (page.getRecords().size() > 0) {
            page.getRecords().forEach(record -> {
                if (StringUtils.isNoneBlank(record.getSgrzAqxctp())) {
                    String[] split = record.getSgrzAqxctp().split(",");
                    List<String> strings = Arrays.asList(split);
                    record.setSgrzAgxctpList(strings);
                }
                if (StringUtils.isNoneBlank(record.getSgrzSgxctp())) {
                    String[] split = record.getSgrzSgxctp().split(",");
                    List<String> strings = Arrays.asList(split);
                    record.setSgrzSgxctpList(strings);
                }
                if (StringUtils.isNoneBlank(record.getSgrzQtxctp())) {
                    String[] split = record.getSgrzQtxctp().split(",");
                    List<String> strings = Arrays.asList(split);
                    record.setSgrzQtxctpList(strings);
                }
                if (StringUtils.isNoneBlank(record.getSgrzZlxctp())) {
                    String[] split = record.getSgrzZlxctp().split(",");
                    List<String> strings = Arrays.asList(split);
                    record.setSgrzZlxctpList(strings);
                }
            });
        }
        return new MyPage<>(page);
    }

    private boolean getParams(QueryRequest request, Sgrz sgrz, QueryWrapper<Sgrz> queryWrapper,
        AuthUserModel userAuth) {
        queryWrapper.eq("IS_DELETE", 0);
        queryWrapper.orderByDesc("SGRZ_ID");
        if (AuthUserModel.KEY_NONE.equals(userAuth.getKey())) {
            return true;
        }
        if (!AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) {
            queryWrapper.in("SGRZ_PROJECTID", userAuth.getProjectIds());
        }
        if (sgrz.getSgrzProjectid() != null) {
            queryWrapper.eq("SGRZ_PROJECTID", sgrz.getSgrzProjectid());
        }
        OrderUtils.setQuseryOrder(queryWrapper, request);
        if (StringUtils.isNotBlank(sgrz.getSgrzCreateman())) {
            queryWrapper.and(wapper -> {
                wapper.like("SGRZ_CREATEMAN", sgrz.getSgrzCreateman());
                wapper.or().inSql("SGRZ_PROJECTID", "select PROJECT_ID from p_project where PROJECT_NAME "
                    + "LIKE CONCAT('%','" + sgrz.getSgrzCreateman() + "','%')");
            });
        }
        if (StringUtils.isNotBlank(sgrz.getProjectName())) {
            queryWrapper.inSql("SGRZ_PROJECTID", "select PROJECT_ID from p_project where PROJECT_NAME "
                + "LIKE CONCAT('%','" + sgrz.getProjectName() + "','%')");
        }
        if (StringUtils.isNotBlank(sgrz.getStartCreateTime())) {
            queryWrapper.ge("SGRZ_CREATETIME", sgrz.getStartCreateTime());
        }
        if (StringUtils.isNotBlank(sgrz.getEndCreateTime())) {
            queryWrapper.le("SGRZ_CREATETIME", sgrz.getEndCreateTime());
        }
        return false;
    }

    @Override
    public List<Sgrz> findSgrzs(Sgrz sgrz) {
        QueryWrapper<Sgrz> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("IS_DELETE", 0);
        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createSgrz(Sgrz sgrz) throws FebsException {
        FebsUtil.isProjectNotNull(sgrz.getSgrzProjectid());
        Long userId = FebsUtil.getCurrentUserId();
        cacheableService.hasPermission(userId, MenuConstant.CONSTRUCTION_ID, ButtonConstant.BUTTON_193_ID,
            sgrz.getSgrzId());
        sgrz.setSgrzCreatetime(new Date());
        sgrz.setSgrzCreateman(FebsUtil.getCurrentRealname());
        sgrz.setSgrzCreatemanid(userId);
        this.save(sgrz);
        logRecordContext.putVariable("id", sgrz.getSgrzId());
        logRecordContext.putVariable("projectId", sgrz.getSgrzProjectid());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSgrz(Sgrz sgrz) throws FebsException {
        Sgrz sgrz1 = sgrzMapper.selectById(sgrz.getSgrzId());
        if (sgrz1 == null) {
            throw new FebsException("数据错误");
        }
        cacheableService.hasPermission(sgrz1.getSgrzCreatemanid(), MenuConstant.CONSTRUCTION_ID,
            ButtonConstant.BUTTON_194_ID, sgrz1.getSgrzProjectid());
        this.updateById(sgrz);
        logRecordContext.putVariable("sgrz1", sgrz.getSgrzProjectid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSgrz(Sgrz sgrz) throws FebsException {
        Sgrz sgrz1 = sgrzMapper.selectById(sgrz.getSgrzId());
        if (sgrz1 == null) {
            throw new FebsException("数据错误");
        }
        cacheableService.hasPermission(sgrz1.getSgrzCreatemanid(), MenuConstant.CONSTRUCTION_ID,
            ButtonConstant.BUTTON_195_ID, sgrz1.getSgrzProjectid());
        this.update(new LambdaUpdateWrapper<Sgrz>().eq(Sgrz::getSgrzId, sgrz.getSgrzId()).set(Sgrz::getIsDelete, 1));
        logRecordContext.putVariable("sgrz1", sgrz.getSgrzProjectid());
    }
}
