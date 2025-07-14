package cc.mrbird.febs.server.system.service;


import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.system.TRoleButton;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (TRoleButton)表服务实现类
 *
 * @author zlkj_cg
 * @since 2021-04-22 10:48:06
 */
@Service("tRoleButtonService")
public interface TRoleButtonService extends IService<TRoleButton> {

    /**
     * 查询（分页）
     *
     * @param request     QueryRequest
     * @param tRoleButton 实体类
     * @return IPage<TRoleButton>
     */
    IPage<TRoleButton> findTRoleButtons(QueryRequest request, TRoleButton tRoleButton);

    /**
     * 查询（所有）
     *
     * @param tRoleButton 实体类
     * @return List<TRoleButton>
     */
    List<TRoleButton> findTRoleButtons(TRoleButton tRoleButton);

    /**
     * 新增
     *
     * @param tRoleButton 实体类
     */
    void createTRoleButton(TRoleButton tRoleButton);

    /**
     * 修改
     *
     * @param tRoleButton 实体类
     */
    void updateTRoleButton(TRoleButton tRoleButton);

    /**
     * 删除
     *
     * @param tRoleButton 实体类
     */
    void deleteTRoleButton(TRoleButton tRoleButton);

}
