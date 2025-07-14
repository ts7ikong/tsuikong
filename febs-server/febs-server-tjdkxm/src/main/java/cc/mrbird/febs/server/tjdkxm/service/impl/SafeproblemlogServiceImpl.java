package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.tjdkxm.Safeproblem;
import cc.mrbird.febs.common.core.entity.tjdkxm.Safeproblemlog;
import cc.mrbird.febs.common.core.utils.OrderUtils;
import cc.mrbird.febs.server.tjdkxm.config.LogRecordContext;
import cc.mrbird.febs.server.tjdkxm.mapper.SafeproblemMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.SafeproblemlogMapper;
import cc.mrbird.febs.server.tjdkxm.service.SafeproblemlogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cc.mrbird.febs.common.core.entity.QueryRequest;

import java.util.List;

/**
 * SafeproblemlogService实现
 *
 * @author zlkj_cg
 * @date 2022-01-12 15:51:07
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SafeproblemlogServiceImpl extends ServiceImpl<SafeproblemlogMapper, Safeproblemlog> implements SafeproblemlogService {

    private final SafeproblemlogMapper safeproblemlogMapper;
    private final SafeproblemMapper safeproblemMapper;

    @Override
    public IPage<Safeproblemlog> findSafeproblemlogs(QueryRequest request, Safeproblemlog safeproblemlog) {
        QueryWrapper<Safeproblemlog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("IS_DELETE",0);
        queryWrapper.setEntity(safeproblemlog);
        OrderUtils.setQuseryOrder(queryWrapper, request);
        Page<Safeproblemlog> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Safeproblemlog> findSafeproblemlogs(Safeproblemlog safeproblemlog) {
        LambdaQueryWrapper<Safeproblemlog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Safeproblemlog::getIsDelete,0);
        queryWrapper.setEntity(safeproblemlog);

        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createSafeproblemlog(Safeproblemlog safeproblemlog, Safeproblem safeproblem) {
        safeproblemlog.setSafeproblenId(safeproblemlog.getSafeproblenId());
        int start = new Integer(safeproblemlog.getSafeproblenlogDo()) + 1;
        safeproblem.setSafeproblenState(Integer.toString(start));
        this.safeproblemMapper.updateById(safeproblem);
        this.save(safeproblemlog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSafeproblemlog(Safeproblemlog safeproblemlog) {
        this.updateById(safeproblemlog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSafeproblemlog(Safeproblemlog safeproblemlog) {
        LambdaQueryWrapper<Safeproblemlog> wrapper = new LambdaQueryWrapper<>();

        this.update(new LambdaUpdateWrapper<Safeproblemlog>().eq(Safeproblemlog::getSafeproblenlogId,safeproblemlog.getSafeproblenlogId())
                .set(Safeproblemlog::getIsDelete,1));
    }
}
