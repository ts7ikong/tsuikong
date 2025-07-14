package cc.mrbird.febs.server.system.service;


import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.system.Set;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 设置表(Set)表服务实现类
 *
 * @author zlkj_cg
 * @since 2021-03-03 16:44:15
 */
public interface SetService extends IService<Set> {

    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param Set     设置表实体类
     * @return IPage<Set>
     */
    IPage<Set> findSets(QueryRequest request, Set Set);

    /**
     * 查询（所有）
     *
     * @param Set 设置表实体类
     * @return List<Set>
     */
    List<Set> findSets(Set Set);

    /**
     * 新增
     *
     * @param Set 设置表实体类
     */
    void createSet(Set Set);

    /**
     * 修改
     *
     * @param Set 设置表实体类
     */
    void updateSet(Set Set);

    /**
     * 删除
     *
     * @param Set 设置表实体类
     */
    void deleteSet(Set Set);

}
