package cc.mrbird.febs.server.system.service.impl;


import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.system.Set;
import cc.mrbird.febs.server.system.mapper.SetMapper;
import cc.mrbird.febs.server.system.service.SetService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * SetService实现
 *
 * @author zlkj_cg
 * @date 2021-03-03 16:44:15
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SetServiceImpl extends ServiceImpl<SetMapper, Set> implements SetService {

    private final SetMapper SetMapper;


    @Override
    public IPage<Set> findSets(QueryRequest request, Set Set) {
        LambdaQueryWrapper<Set> queryWrapper = new LambdaQueryWrapper<>();

        Page<Set> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Set> findSets(Set Set) {
        LambdaQueryWrapper<Set> queryWrapper = new LambdaQueryWrapper<>();

        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createSet(Set Set) {
        this.save(Set);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSet(Set Set) {
        this.updateById(Set);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSet(Set Set) {
        this.removeById(Set);
    }

}
