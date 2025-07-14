package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Danger;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 重大危险源记录表(Danger)表服务实现类
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:02
 */
public interface DangerService extends IService<Danger> {

    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param danger  重大危险源记录表实体类
     * @return IPage<Danger>
     */
    MyPage<Danger> findDangers(QueryRequest request, Danger.Params danger);


    /**
     * 新增
     *
     * @param danger 重大危险源记录表实体类
     */
    void createDanger(Danger danger) throws FebsException;

    /**
     * 修改
     *
     * @param danger 重大危险源记录表实体类
     */
    void updateDanger(Danger danger) throws FebsException;

    /**
     * 删除
     *
     * @param danger 重大危险源记录表实体类
     */
    void deleteDanger(Danger danger) throws FebsException;
}
