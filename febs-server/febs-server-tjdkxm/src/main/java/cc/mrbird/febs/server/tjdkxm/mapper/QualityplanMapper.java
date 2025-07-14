package cc.mrbird.febs.server.tjdkxm.mapper;

import cc.mrbird.febs.common.core.entity.tjdkxm.Qualityplan;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * 质量检查计划(Qualityplan)表数据库访问层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:02
 */
@Mapper
@Repository 
public interface QualityplanMapper extends MPJBaseMapper<Qualityplan> {
    Integer getAcceptanceCount(Set<Long> projectIds, Long userId, @Param(Constants.WRAPPER) Wrapper<?> wrapper);

    Integer notChecked(Long userId, Set<Long> projectIds);

    <T> IPage<Qualityplan> selectPageInfo(Page<T> page, @Param(Constants.WRAPPER) Wrapper<?> wrapper);
    /**
     * id查询
     *
     * @param id tableid
     * @return {@link com.alibaba.fastjson.JSONObject}
     */
    JSONObject selectInfoById(@Param("id") Long id);

}
