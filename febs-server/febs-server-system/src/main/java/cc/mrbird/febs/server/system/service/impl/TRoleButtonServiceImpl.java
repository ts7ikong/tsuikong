package cc.mrbird.febs.server.system.service.impl;


import cc.mrbird.febs.common.core.entity.system.TRoleButton;
import cc.mrbird.febs.server.system.mapper.TRoleButtonMapper;
import cc.mrbird.febs.server.system.service.TRoleButtonService;
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
 * TRoleButtonService实现
 *
 * @author zlkj_cg
 * @date 2021-04-22 10:48:07
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TRoleButtonServiceImpl extends ServiceImpl<TRoleButtonMapper, TRoleButton> implements TRoleButtonService {

    private final TRoleButtonMapper tRoleButtonMapper;

    @Override
    public IPage<TRoleButton> findTRoleButtons(QueryRequest request, TRoleButton tRoleButton) {
        LambdaQueryWrapper<TRoleButton> queryWrapper = new LambdaQueryWrapper<>();

        Page<TRoleButton> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<TRoleButton> findTRoleButtons(TRoleButton tRoleButton) {
        LambdaQueryWrapper<TRoleButton> queryWrapper = new LambdaQueryWrapper<>();

        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTRoleButton(TRoleButton tRoleButton) {
        this.save(tRoleButton);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTRoleButton(TRoleButton tRoleButton) {
        this.updateById(tRoleButton);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTRoleButton(TRoleButton tRoleButton) {
        LambdaQueryWrapper<TRoleButton> wrapper = new LambdaQueryWrapper<>();
            
        this.removeById(tRoleButton);
    }
}
