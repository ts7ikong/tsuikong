package cc.mrbird.febs.server.tjdk.service.impl;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdk.Sysnotify;
import cc.mrbird.febs.common.core.entity.tjdk.SystnotityMiddle;
import cc.mrbird.febs.common.core.entity.tjdkxm.UserSysnotify;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.server.tjdk.config.LogRecordContext;
import cc.mrbird.febs.server.tjdk.mapper.SysnotifyMapper;
import cc.mrbird.febs.server.tjdk.service.SysnotifyService;
import cc.mrbird.febs.server.tjdk.service.SystnotityMiddlesService;
import cc.mrbird.febs.server.tjdk.service.UserSysnotifyService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SysnotifyService实现
 *
 * @author zlkj_cg
 * @date 2022-01-12 15:51:08
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SysnotifyServiceImpl extends ServiceImpl<SysnotifyMapper, Sysnotify> implements SysnotifyService {
    @Autowired
    private SysnotifyMapper sysnotifyMapper;
    @Autowired
    private SystnotityMiddlesService systnotityMiddlesService;
    @Autowired
    private UserSysnotifyService userSysnotifyService;
    @Autowired
    private LogRecordContext logRecordContext;

    @Override
    public IPage<Sysnotify> findSysnotifys(QueryRequest request, Sysnotify.Params sysnotify) {
        QueryWrapper<Sysnotify> queryWrapper = new QueryWrapper<Sysnotify>();
        queryWrapper.orderByDesc("SYSNOTIFY_ID");
        queryWrapper.eq("IS_DELETE", 0);
        if (StringUtils.isNotBlank(sysnotify.getSysnotifyTitle())) {
            queryWrapper.and(wapper -> {
                wapper.like("SYSNOTIFY_TITLE", sysnotify.getSysnotifyTitle());
                wapper.or().like("SYSNOTIFY_CONTENT", sysnotify.getSysnotifyTitle());
            });
        }
        if (StringUtils.isNotBlank(sysnotify.getSysnotifyStartTime())) {
            queryWrapper.ge("SYSNOTIFY_TIME", sysnotify.getSysnotifyStartTime());
        }
        if (StringUtils.isNotBlank(sysnotify.getSysnotifyEndTime())) {
            queryWrapper.le("SYSNOTIFY_TIME", sysnotify.getSysnotifyEndTime());
        }
        Integer integer = this.sysnotifyMapper.selectCount(queryWrapper);
        if (integer == null || integer == 0) {
            return new Page<>();
        }
        Integer integer1 = this.sysnotifyMapper.selectCount(queryWrapper.select("1"));
        if (integer1 != null && integer1 > 0) {
            queryWrapper.groupBy("SYSNOTIFY_ID");
            IPage<Sysnotify> sysnotifyIPage = this.sysnotifyMapper.selectPageInfo(new Page<>(request.getPageNum(),
                            request.getPageSize(), false)
                    , queryWrapper);
            sysnotifyIPage.setTotal(integer);
            return sysnotifyIPage;
        }
        return new Page<>();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createSysnotify(Sysnotify sysnotify) throws FebsException {
        String sysnotifyPid = sysnotify.getSysnotityContrant();
        String[] split = null;
        if (!StringUtils.isEmpty(sysnotifyPid)) {
            split = sysnotifyPid.split(",");
            for (String s : split) {
                if (!StringUtils.isNumeric(s)) {
                    log.error(s + "不是数字");
                    throw new FebsException("新增失败");
                }
            }
        }
        sysnotify.setSysnotifyTime(new Date());
        this.save(sysnotify);
        List<SystnotityMiddle> systnotityMiddles = new ArrayList<>();
        if (split != null) {
            for (String s : split) {
                SystnotityMiddle systnotityMiddle = new SystnotityMiddle();
                systnotityMiddle.setSystnotityId(sysnotify.getSysnotifyId());
                systnotityMiddle.setTableId(Long.valueOf(s));
                systnotityMiddles.add(systnotityMiddle);
            }
            systnotityMiddlesService.saveBatch(systnotityMiddles, systnotityMiddles.size());
        }
        userSysnotifyService.createUserSysnotify(sysnotify.getSysnotifyId());
        logRecordContext.putVariable("id", sysnotify.getSysnotifyId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSysnotify(Sysnotify sysnotify) throws FebsException {
        String sysnotifyContent = sysnotify.getSysnotifyContent();
        if (!StringUtils.isEmpty(sysnotifyContent)) {
            String[] split = sysnotifyContent.split(",");
            for (String s : split) {
                if (!StringUtils.isNumeric(s)) {
                    log.error(s + "不是数字");
                    throw new FebsException("修改失败");
                }
            }
            systnotityMiddlesService.remove(new LambdaQueryWrapper<SystnotityMiddle>()
                    .eq(SystnotityMiddle::getSystnotityId, sysnotify.getSysnotifyId()));
            List<SystnotityMiddle> qualityproblemUsers = new ArrayList<>();
            for (String s : split) {
                SystnotityMiddle qualityproblemUser = new SystnotityMiddle();
                qualityproblemUser.setTableId(sysnotify.getSysnotifyId());
                qualityproblemUser.setSystnotityId(Long.valueOf(s));
                qualityproblemUsers.add(qualityproblemUser);
            }
            systnotityMiddlesService.saveBatch(qualityproblemUsers, qualityproblemUsers.size());
        }
        this.updateById(sysnotify);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSysnotify(Sysnotify sysnotify) {
        this.update(new LambdaUpdateWrapper<Sysnotify>().eq(Sysnotify::getSysnotifyId, sysnotify.getSysnotifyId())
                .set(Sysnotify::getIsDelete, 1));
        userSysnotifyService.update(new LambdaUpdateWrapper<UserSysnotify>()
                .eq(UserSysnotify::getSysnotifyId, sysnotify.getSysnotifyId())
                .set(UserSysnotify::getIsDelete, 1));
    }
}
