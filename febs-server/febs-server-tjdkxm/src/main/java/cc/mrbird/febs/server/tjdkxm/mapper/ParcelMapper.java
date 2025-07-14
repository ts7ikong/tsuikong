package cc.mrbird.febs.server.tjdkxm.mapper;

import cc.mrbird.febs.common.core.entity.tjdkxm.Jsdwaqrz;
import cc.mrbird.febs.common.core.entity.tjdkxm.Parcel;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * 分部表(Parcel)表数据库访问层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:04
 */
@Mapper
@Repository
public interface ParcelMapper extends MPJBaseMapper<Parcel> {
    /**
     * 级联查询
     *
     * @param page
     * @param wrapper
     * @return {@link java.util.List<cc.mrbird.febs.common.core.entity.tjdkxm.Parcel>}
     */
    @Select("SELECT * from p_parcel  t ${ew.customSqlSegment}")
    @Results(id = "column", value = {
            @Result(property = "parcelId", column = "parcel_id"),
            @Result(column = "parcel_id", property = "subitemList",
                    many = @Many(select = "cc.mrbird.febs.server.tjdkxm.mapper" +
                            ".SubitemMapper.selectByParcelId", fetchType = FetchType.LAZY
                    )
            )
    })
    public List<Parcel> selectParcelList(IPage<Parcel> page, @Param(Constants.WRAPPER) Wrapper<Parcel> wrapper);

    @Select("SELECT * from p_parcel t left join p_project p on t.PARCEL_PROJECTID=p.PROJECT_ID" +
            " ${ew.customSqlSegment}")
    @ResultMap(value = "column")
    public IPage<Parcel> selectParcelPage(IPage<Parcel> page, @Param(Constants.WRAPPER) Wrapper<Parcel> wrapper);

    /**
     * 根据项目id查询
     *
     * @param parcelProjectid
     * @return {@link List< Parcel>}
     */
    @Select("SELECT PARCEL_ID,PARCEL_PROJECTID,PARCEL_NAME,PARCEL_CONTENT,PARCEL_UNIT,PARCEL_PERSON,PARCEL_LINK FROM " +
            "p_parcel WHERE (PARCEL_PROJECTID = #{parcelProjectid} and IS_DELETE = 0)")
    @ResultMap(value = "column")
    public List<Parcel> selectParcelByProjectId(Long parcelProjectid);

    /**
     * 根据id查询Parcel信息
     *
     * @param parcelId id
     * @return {@link cc.mrbird.febs.common.core.entity.tjdkxm.Parcel}
     */
    @Select("SELECT PARCEL_ID,PARCEL_PROJECTID,PARCEL_NAME,PARCEL_CONTENT,PARCEL_UNIT,PARCEL_PERSON,PARCEL_LINK FROM " +
            "p_parcel WHERE PARCEL_ID=#{parcelId} and IS_DELETE = 0"
    )
    Parcel selectParcelById(Long parcelId);

    Set<Long> getParcelIds(Set<Long> projectIds, String name);
    <T> IPage<Parcel> selectPageInfo(Page<T> page, @Param(Constants.WRAPPER) Wrapper<?> wrapper);
    /**
     * id查询
     *
     * @param id tableid
     * @return {@link com.alibaba.fastjson.JSONObject}
     */
    JSONObject selectInfoById(@Param("id") Long id);

}
