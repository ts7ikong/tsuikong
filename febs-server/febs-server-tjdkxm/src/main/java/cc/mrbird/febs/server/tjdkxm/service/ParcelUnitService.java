package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Parcel;
import cc.mrbird.febs.common.core.entity.tjdkxm.ParcelUnit;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/8 17:13
 */
public interface ParcelUnitService extends IService<ParcelUnit> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param parcel 分部表实体类
     * @return IPage<ParcelUnit>
     */
    IPage<ParcelUnit> findParcelUnits(QueryRequest request, ParcelUnit parcel);

    /**
     * 查询（所有）
     *
     * @param parcel 分部表实体类
     * @return List<ParcelUnit>
     */
    List<ParcelUnit> findParcelUnits(ParcelUnit parcel);

    List<Map<String, Object>> findParcelUnits();

    /**
     * 新增
     *
     * @param parcel 分部表实体类
     */
    void createParcelUnit(ParcelUnit parcel);

    /**
     * 修改
     *
     * @param parcel 分部表实体类
     */
    void updateParcelUnit(ParcelUnit parcel) throws FebsException;

    /**
     * 删除
     *
     * @param parcel 分部表实体类
     */
    void deleteParcelUnit(ParcelUnit parcel) throws FebsException;

}
