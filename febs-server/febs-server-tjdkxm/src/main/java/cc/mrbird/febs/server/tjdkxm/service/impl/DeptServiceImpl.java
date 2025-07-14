package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdk.Dept;
import cc.mrbird.febs.common.core.utils.OrderUtils;
import cc.mrbird.febs.server.tjdkxm.mapper.DeptMapper;
import cc.mrbird.febs.server.tjdkxm.service.DeptService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * DeptService实现
 *
 * @author zlkj_cg
 * @date 2022-01-12 15:51:04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {
    @Autowired
    private DeptMapper deptMapper;

    @Override
    public IPage<Dept> findDepts(QueryRequest request, Dept dept) {
//        MPJQueryWrapper<Dept> queryWrapper=new MPJQueryWrapper<>();
//        queryWrapper.selectAll(Dept.class);
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("IS_DELETE", 0);
        if (StringUtils.isNotBlank(dept.getDeptName())) {
            queryWrapper.like("dept_name", dept.getDeptName());
        }
        OrderUtils.setQuseryOrder(queryWrapper, request);
        Page<Dept> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Dept> findDepts(Dept dept) {
        LambdaQueryWrapper<Dept> queryWrapper = new LambdaQueryWrapper<Dept>()
                .select(Dept::getDeptId, Dept::getDeptName)
                .eq(Dept::getIsDelete, 0);
        if (StringUtils.isNotBlank(dept.getDeptName())) {
            queryWrapper.like(Dept::getDeptName, dept.getDeptName());
        }
        return this.list(queryWrapper);
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDept(Dept dept) {
        this.updateById(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDept(Dept dept) {
        this.update(new LambdaUpdateWrapper<Dept>().eq(Dept::getDeptId, dept.getDeptId())
                .set(Dept::getIsDelete, 1));
    }
}
