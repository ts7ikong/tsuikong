package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.system.SystemUser;
import cc.mrbird.febs.common.core.entity.tjdkxm.LearnRecord;
import cc.mrbird.febs.common.core.entity.tjdkxm.Partylearn;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.common.core.utils.OrderUtils;
import cc.mrbird.febs.server.tjdkxm.config.LogRecordContext;
import cc.mrbird.febs.server.tjdkxm.mapper.LearnRecordMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.PartylearnMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.ProjectMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.UserMapper;
import cc.mrbird.febs.server.tjdkxm.service.CacheableService;
import cc.mrbird.febs.server.tjdkxm.service.PartylearnService;
import cc.mrbird.febs.server.tjdkxm.service.UserProjectService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * PartylearnService实现
 *
 * @author zlkj_cg
 * @date 2022-01-12 15:51:04
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class PartylearnServiceImpl extends ServiceImpl<PartylearnMapper, Partylearn> implements PartylearnService {

    private final PartylearnMapper partylearnMapper;
    private final CacheableService cacheableService;
    private final UserMapper userMapper;
    @Autowired
    private LogRecordContext logRecordContext;

    @Override
    public IPage<Partylearn> findPartylearns(QueryRequest request, Partylearn partylearn) {
        QueryWrapper<Partylearn> queryWrapper = new QueryWrapper<>();
        getParams(request, partylearn, queryWrapper);
        return this.partylearnMapper.selectPageInfo(new Page<>(request.getPageNum(), request.getPageSize()),
            queryWrapper);
    }

    /**
     * 查询参数组装
     *
     * @param request 请求
     * @param partylearn 实体
     * @param queryWrapper 查询实体
     */
    private void getParams(QueryRequest request, Partylearn partylearn, QueryWrapper<Partylearn> queryWrapper) {
        queryWrapper.eq("IS_DELETE", 0);
        queryWrapper.orderByDesc("PARTYLEARN_TIME");
        OrderUtils.setQuseryOrder(queryWrapper, request);
        queryWrapper.eq("PARTYLEARN_TYPE", partylearn.getPartylearnType());
        if (partylearn.getPartylearnType() == 1) {
            Long userId = FebsUtil.getCurrentUserId();
            Integer integer = userMapper.selectCount(new LambdaQueryWrapper<SystemUser>().select(SystemUser::getUserId)
                .eq(SystemUser::getUserId, userId).eq(SystemUser::getPartyMember, SystemUser.IS_PARTY_MEMBER));
            if (integer == null || integer == 0) {
                queryWrapper.eq("PARTYLEARN_ID", -1);
            }
        }
        if (StringUtils.isNotBlank(partylearn.getPartylearnTitle())) {
            queryWrapper.like("PARTYLEARN_TITLE", partylearn.getPartylearnTitle());
        }
        if (StringUtils.isNotBlank(partylearn.getStartTime())) {
            queryWrapper.ge("PARTYLEARN_TIME", partylearn.getStartTime());
        }
        if (StringUtils.isNotBlank(partylearn.getEndTime())) {
            queryWrapper.le("PARTYLEARN_TIME", partylearn.getEndTime());
        }
    }

    @Override
    public List<Partylearn> findPartylearns(Partylearn partylearn) {
        LambdaQueryWrapper<Partylearn> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Partylearn::getIsDelete, 3);
        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPartylearn(Partylearn partylearn) throws FebsException {
        if (StringUtils.isEmpty(partylearn.getPartylearnTitle())) {
            throw new FebsException("党建标题标题不能为空");
        }
        if (partylearn.getPartylearnType() == 1) {
            if (StringUtils.isBlank(partylearn.getPartylearnContent()) && partylearn.getPartylearnAnnx() == null) {
                throw new FebsException("内容和党建资料至少存在一个");
            }
        }
        partylearn.setPartylearnUserid(FebsUtil.getCurrentUserId());
        partylearn.setPartylearnTime(new Date());
        this.save(partylearn);
        logRecordContext.putVariable("id", partylearn.getPartylearnId());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePartylearn(Partylearn partylearn) throws FebsException {
        Partylearn partylearn1 = partylearnMapper.selectById(partylearn.getPartylearnId());
        if (partylearn1 == null) {
            throw new FebsException("选择数据");
        }
        cacheableService.hasPermission(partylearn1.getPartylearnUserid());
        this.updateById(partylearn);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePartylearn(Partylearn partylearn) throws FebsException {
        Partylearn partylearn1 = partylearnMapper.selectById(partylearn.getPartylearnId());
        if (partylearn1 == null) {
            throw new FebsException("选择数据");
        }
        cacheableService.hasPermission(partylearn1.getPartylearnUserid());
        this.update(new LambdaUpdateWrapper<Partylearn>().eq(Partylearn::getPartylearnId, partylearn.getPartylearnId())
            .set(Partylearn::getIsDelete, 1));
    }
}
