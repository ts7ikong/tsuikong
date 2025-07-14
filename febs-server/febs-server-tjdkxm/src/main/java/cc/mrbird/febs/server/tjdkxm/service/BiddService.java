package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Bidd;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/4/7 11:54
 */
public interface BiddService extends IService<Bidd> {

    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param bidd     实体类
     * @return IPage<Bidd>
     */
    IPage<Bidd> findBidds(QueryRequest request, Bidd.Params bidd);

    /**
     * 查询（所有）
     *
     * @param bidd  实体类
     * @return List<Bidd>
     */
    List<Bidd> findBidds(Bidd bidd);

    /**
     * 新增
     *
     * @param bidd  实体类
     */
    void createBidd(Bidd bidd);

    /**
     * 修改
     *
     * @param bidd  实体类
     */
    void updateBidd(Bidd bidd) throws FebsException;

    /**
     * 删除
     *
     * @param bidd  实体类
     */
    void deleteBidd(Bidd bidd) throws FebsException;
}