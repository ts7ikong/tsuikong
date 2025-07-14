package cc.mrbird.febs.server.system.service;


import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.system.TButton;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 按钮表(TButton)表服务实现类
 *
 * @author zlkj_cg
 * @since 2021-04-22 10:48:09
 */
@Service("tButtonService")
public interface TButtonService extends IService<TButton> {

    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param tButton 按钮表实体类
     * @return IPage<TButton>
     */
    IPage<TButton> findTButtons(QueryRequest request, TButton tButton);

    /**
     * 查询（所有）
     *
     * @param tButton 按钮表实体类
     * @return List<TButton>
     */
    List<TButton> findTButtons(TButton tButton);

    /**
     * 新增
     *
     * @param tButton 按钮表实体类
     */
    void createTButton(TButton tButton);

    /**
     * 修改
     *
     * @param tButton 按钮表实体类
     */
    void updateTButton(TButton tButton);

    /**
     * 删除
     *
     * @param tButton 按钮表实体类
     */
    void deleteTButton(TButton tButton);

    List<TButton> getButtonByUserId(Long currentUserId);
}
