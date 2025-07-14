package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Parcel;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分部表(Parcel)表服务实现类
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:04
 */
public interface ParcelService extends IService<Parcel> {

    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param parcel 分部表实体类
     * @return IPage<Parcel>
     */
    MyPage<Parcel> findParcels(QueryRequest request, Parcel parcel) throws FebsException;

    /**
     * 查询（所有）
     *
     * @param parcel 分部表实体类
     * @return List<Parcel>
     */
    List<Parcel> findParcels(Parcel parcel) throws FebsException;

    /**
     * 新增
     *
     * @param parcel 分部表实体类
     */
    void createParcel(Parcel parcel) throws FebsException;

    /**
     * 修改
     *
     * @param parcel 分部表实体类
     */
    void updateParcel(Parcel parcel) throws FebsException;

    /**
     * 删除
     *
     * @param parcel 分部表实体类
     */
    void deleteParcel(Parcel parcel) throws Exception;
}
