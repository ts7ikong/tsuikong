package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.tjdkxm.Qualityproblem;
import cc.mrbird.febs.common.core.entity.tjdkxm.Qualityproblemlog;
import cc.mrbird.febs.common.core.utils.OrderUtils;
import cc.mrbird.febs.server.tjdkxm.config.LogRecordContext;
import cc.mrbird.febs.server.tjdkxm.mapper.QualityproblemMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.QualityproblemlogMapper;
import cc.mrbird.febs.server.tjdkxm.service.QualityproblemlogService;
import cc.mrbird.febs.server.tjdkxm.service.UserProjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
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
* QualityproblemlogService实现
*
* @author zlkj_cg
* @date 2022-01-12 15:51:02
*/
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class QualityproblemlogServiceImpl extends ServiceImpl<QualityproblemlogMapper, Qualityproblemlog> implements QualityproblemlogService {

    private final QualityproblemlogMapper qualityproblemlogMapper;
    private final QualityproblemMapper qualityproblemMapper;
    private final UserProjectService userProjectService;
    @Autowired
    private LogRecordContext logRecordContext;
    
    @Override
    public IPage<Qualityproblemlog> findQualityproblemlogs(QueryRequest request, Qualityproblemlog qualityproblemlog) {
        QueryWrapper<Qualityproblemlog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("IS_DELETE",0);
        queryWrapper.setEntity(qualityproblemlog);
        OrderUtils.setQuseryOrder(queryWrapper,request);
        Page<Qualityproblemlog> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }
    
    @Override
    public List<Qualityproblemlog> findQualityproblemlogs(Qualityproblemlog qualityproblemlog) {
        LambdaQueryWrapper<Qualityproblemlog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Qualityproblemlog::getIsDelete,0);
        queryWrapper.setEntity(qualityproblemlog);
        return this.list(queryWrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createQualityproblemlog(Qualityproblemlog qualityproblemlog,Qualityproblem qualityproblem) {
        qualityproblem.setQualityproblenId(qualityproblemlog.getQualityproblenId());
        int start = new Integer(qualityproblemlog.getQualityproblenlogDo()) + 1;
        qualityproblem.setQualityproblenState(Integer.toString(start));
        this.qualityproblemMapper.updateById(qualityproblem);
        this.save(qualityproblemlog);
        logRecordContext.putVariable("id", qualityproblemlog.getQualityproblenlogId());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateQualityproblemlog(Qualityproblemlog qualityproblemlog) {
        this.updateById(qualityproblemlog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteQualityproblemlog(Qualityproblemlog qualityproblemlog) {
    LambdaQueryWrapper<Qualityproblemlog> wrapper = new LambdaQueryWrapper<>();

       this.update(new LambdaUpdateWrapper<Qualityproblemlog>()
               .eq(Qualityproblemlog::getQualityproblenlogId,qualityproblemlog.getQualityproblenlogId())
               .set(Qualityproblemlog::getIsDelete,1));
    }
}
