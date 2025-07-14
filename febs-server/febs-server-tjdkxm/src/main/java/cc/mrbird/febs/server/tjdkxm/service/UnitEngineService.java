package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdkxm.UnitEngine;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/4/6 11:07
 */
public interface UnitEngineService extends IService<UnitEngine> {

    /**
     * 查询（分页）
     *
     * @param request    QueryRequest
     * @param unitEngine 分项表实体类
     * @return IPage<UnitEngine>
     */
    MyPage<UnitEngine> findUnitEngines(QueryRequest request, UnitEngine unitEngine) throws FebsException;

    /**
     * 查询（所有）
     *
     * @param unitEngine 分项表实体类
     * @return List<UnitEngine>
     */
    List<UnitEngine> findUnitEngines(UnitEngine unitEngine) throws FebsException;

    /**
     * 新增
     *
     * @param unitEngine 分项表实体类
     */
    void createUnitEngine(UnitEngine unitEngine) throws FebsException;

    /**
     * 修改
     *
     * @param unitEngine 分项表实体类
     */
    void updateUnitEngine(UnitEngine unitEngine) throws FebsException;

    /**
     * 删除
     *
     * @param unitEngine 分项表实体类
     */
    void deleteUnitEngine(UnitEngine unitEngine) throws Exception;
}
