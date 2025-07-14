package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.tjdkxm.Datamanagement;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * 资料管理(Datamanagement)表服务实现类
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:03
 */
public interface DatamanagementService extends IService<Datamanagement> {

    /**
     * 查询（分页） 资料
     *
     * @param request        QueryRequest
     * @param datamanagement 资料管理实体类
     * @return IPage<Datamanagement>
     */
    MyPage<Datamanagement> findDatamanagements(QueryRequest request, Datamanagement.Params datamanagement);

    /**
     * 查询（分页） 模板
     *
     * @param request        QueryRequest
     * @param datamanagement 资料管理实体类
     * @return IPage<Datamanagement>
     */
    MyPage<Datamanagement> findDatamanagementTemps(QueryRequest request, Datamanagement.Params datamanagement);

    /**
     * 查询（所有）
     *
     * @param datamanagement 资料管理实体类
     * @return List<Datamanagement>
     */
    List<Datamanagement> findDatamanagements(Datamanagement datamanagement);

    /**
     * 新增
     *
     * @param datamanagement 资料管理实体类
     */
    void createDatamanagement(Datamanagement datamanagement) throws FebsException;

    /**
     * 新增 模板
     *
     * @param datamanagement 资料管理实体类
     */
    void createDatamanagementTemp(Datamanagement datamanagement) throws FebsException;
    /**
     * 新增 模板
     *
     * @param datamanagement 资料管理实体类
     */
    void createDatamanagementTemp(List<Datamanagement.Add> datamanagements) throws FebsException;

    /**
     * 新增
     *
     * @param datamanagement 资料管理实体类
     */
    void createDatamanagement(List<Datamanagement.Add> datamanagement) throws FebsException;

    /**
     * 修改
     *
     * @param datamanagement 资料管理实体类
     */
    void updateDatamanagement(Datamanagement datamanagement);

    /**
     * 删除
     *
     * @param datamanagement 资料管理实体类
     */
    void deleteDatamanagement(Datamanagement datamanagement) throws FebsException;

    /**
     * 删除
     *
     * @param datamanagementIds 资料管理实体类
     */
    void deleteDatamanagements(String datamanagementIds) throws FebsException;
}
