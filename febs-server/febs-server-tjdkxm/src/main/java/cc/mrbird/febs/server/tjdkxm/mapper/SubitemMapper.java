package cc.mrbird.febs.server.tjdkxm.mapper;

import cc.mrbird.febs.common.core.entity.tjdkxm.Safeproblem;
import cc.mrbird.febs.common.core.entity.tjdkxm.Subitem;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * 分项表(Subitem)表数据库访问层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:07
 */
@Mapper
@Repository
public interface SubitemMapper extends MPJBaseMapper<Subitem> {
    <T> Page<Subitem> selectPageInfo(Page<T> tPage, @Param(Constants.WRAPPER) Wrapper<?> wrapper);
    /**
     * id查询
     *
     * @param id tableid
     * @return {@link com.alibaba.fastjson.JSONObject}
     */
    JSONObject selectInfoById(@Param("id") Long id);

    /**
     * by 分部id
     *
     * @param parcelId
     * @return {@link java.util.List<cc.mrbird.febs.common.core.entity.tjdkxm.Subitem>}
     */
    @Select("SELECT * from p_subitem t where t.SUBITEM_PARCELID=#{parcelId} and t.IS_DELETE = 0 ")
    public List<Subitem> selectByParcelIdAndProjectId(Long parcelId);

    /**
     * by subitemid
     *
     * @param subitemId
     * @return {@link Subitem}
     */
    @Select("SELECT * FROM p_subitem WHERE SUBITEM_ID=#{subitemId} and IS_DELETE = 0")
    public Subitem selectSubitemById(Long subitemId);

    Set<Long> getSubitemIds(Set<Long> projectIds, String name);

}
