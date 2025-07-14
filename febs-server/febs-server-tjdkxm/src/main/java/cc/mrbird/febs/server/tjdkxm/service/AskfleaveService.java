package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.tjdkxm.Askfleave;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * 请假申请审批表(Askfleave)表服务实现类
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:05
 */
public interface AskfleaveService extends IService<Askfleave> {

    /**
    * 查询 本人（分页）
    *
    * @param request QueryRequest
    * @param askfleave 请假申请审批表实体类
    * @return IPage<Askfleave>
    */
    IPage<Askfleave> findAskfleaves(QueryRequest request, Askfleave askfleave);

    /**
     * 查询待审批 [需要有审批权限]
     *
     * @param request
     * @param askfleave
     * @return {@link IPage<?>}
     */

    IPage<Askfleave> findAskfleaveApprovals(QueryRequest request, Askfleave askfleave) throws FebsException;
    /**
    * 查询 本人（所有）
    *
    * @param askfleave 请假申请审批表实体类
    * @return List<Askfleave>
    */
    List<Askfleave> findAskfleaves(Askfleave askfleave);


    /**
    * 新增
    *
    * @param askfleave 请假申请审批表实体类
    */
    void createAskfleave(Askfleave askfleave);

    /**
    * 修改
    *
    * @param askfleave 请假申请审批表实体类
    */
    void updateAskfleave(Askfleave askfleave,boolean approval) throws FebsException;
    /**
    * 删除
    *
    * @param askfleave 请假申请审批表实体类
    */
    void deleteAskfleave(Askfleave askfleave) throws FebsException;

    /**
     * 未审批
     *
     * @param userId
     * @param projectId
     * @return {@link Integer}
     */
    Integer notChecked(Long userId, Long projectId);
}
