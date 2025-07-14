package cc.mrbird.febs.server.system.service;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.system.OrgStructure;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/5/6 15:23
 */
public interface IOrgStructureService {

    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param orgStructure 组织结构实体类
     * @return IPage<OrgStructure>
     */
    IPage<OrgStructure> findOrgStructures(QueryRequest request, OrgStructure.Params orgStructure);


    /**
     * 新增
     *
     * @param orgStructure 组织结构实体类
     */
    void createOrgStructure(OrgStructure orgStructure);

    /**
     * 修改
     *
     * @param orgStructure 组织结构实体类
     */
    void updateOrgStructure(OrgStructure orgStructure) throws FebsException;

    /**
     * 删除
     *
     * @param orgStructure 组织结构实体类
     */
    void deleteOrgStructure(OrgStructure orgStructure) throws FebsException;
}
