package cc.mrbird.febs.server.system.service.impl;

import cc.mrbird.febs.common.core.entity.system.TButton;
import cc.mrbird.febs.server.system.mapper.TButtonMapper;
import cc.mrbird.febs.server.system.service.TButtonService;
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
 * TButtonService实现
 *
 * @author zlkj_cg
 * @date 2021-04-22 10:48:09
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TButtonServiceImpl extends ServiceImpl<TButtonMapper, TButton> implements TButtonService {

    private final TButtonMapper tButtonMapper;

    @Override
    public IPage<TButton> findTButtons(QueryRequest request, TButton tButton) {
        LambdaQueryWrapper<TButton> queryWrapper = new LambdaQueryWrapper<>();

        Page<TButton> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<TButton> findTButtons(TButton tButton) {
        LambdaQueryWrapper<TButton> queryWrapper = new LambdaQueryWrapper<>();

        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTButton(TButton tButton) {
        this.save(tButton);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTButton(TButton tButton) {
        this.updateById(tButton);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTButton(TButton tButton) {
        LambdaQueryWrapper<TButton> wrapper = new LambdaQueryWrapper<>();

        this.removeById(tButton);
    }

    @Override
    public List<TButton> getButtonByUserId(Long currentUserId) {
        return tButtonMapper.getButtonByUserId(currentUserId);
    }
}
