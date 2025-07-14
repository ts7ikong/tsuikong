package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.tjdkxm.Subitem;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 分项表(Subitem)表服务实现类
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:07
 */
public interface SubitemService extends IService<Subitem> {

    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param subitem 分项表实体类
     * @return IPage<Subitem>
     */
    IPage<Subitem> findSubitems(QueryRequest request, Subitem subitem) throws FebsException;

    /**
     * 查询（所有）
     *
     * @param subitem 分项表实体类
     * @return List<Subitem>
     */
    List<Subitem> findSubitems(Subitem subitem) throws FebsException;

    /**
     * 新增
     *
     * @param subitem 分项表实体类
     */
    void createSubitem(Subitem subitem) throws FebsException;

    /**
     * 修改
     *
     * @param subitem 分项表实体类
     */
    void updateSubitem(Subitem subitem);

    /**
     * 删除
     *
     * @param subitem 分项表实体类
     */
    void deleteSubitem(Subitem subitem) throws Exception;
}
