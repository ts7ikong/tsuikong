package cc.mrbird.febs.server.system.service.impl;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.system.OrgStructure;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.server.system.mapper.OrgStructureMapper;
import cc.mrbird.febs.server.system.service.IOrgStructureService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/5/6 15:28
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class IOrgStructureServiceImpl extends ServiceImpl<OrgStructureMapper, OrgStructure> implements IOrgStructureService {
    @Autowired
    private OrgStructureMapper orgStructureMapper;

    /**
     * 查询（分页）
     *
     * @param request      QueryRequest
     * @param orgStructure 组织结构实体类
     * @return IPage<OrgStructure>
     */
    @Override
    public IPage<OrgStructure> findOrgStructures(QueryRequest request, OrgStructure.Params orgStructure) {
        QueryWrapper<Object> query = Wrappers.query();
        if (StringUtils.isNotBlank(orgStructure.getName())) {
            query.like("NAME", orgStructure.getName());
        }
        return orgStructureMapper.selectPageInfo(new Page<>(request.getPageNum(), request.getPageSize()), query);
    }

    /**
     * 新增
     *
     * @param orgStructure 组织结构实体类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrgStructure(OrgStructure orgStructure) {
        save(orgStructure);
    }

    /**
     * 修改
     *
     * @param orgStructure 组织结构实体类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrgStructure(OrgStructure orgStructure) throws FebsException {
        updateById(orgStructure);
    }

    /**
     * 删除
     *
     * @param orgStructure 组织结构实体类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrgStructure(OrgStructure orgStructure) throws FebsException {
        this.orgStructureMapper.update(null,
                new LambdaUpdateWrapper<OrgStructure>()
                        .and(wapper -> {
                            wapper.eq(OrgStructure::getId, orgStructure.getId());
                            wapper.or().eq(OrgStructure::getParentId, orgStructure.getId());
                        }).set(OrgStructure::getIsDelete, 1));
    }
}
