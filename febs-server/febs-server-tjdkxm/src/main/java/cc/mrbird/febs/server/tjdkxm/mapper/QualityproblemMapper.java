package cc.mrbird.febs.server.tjdkxm.mapper;

import cc.mrbird.febs.common.core.entity.tjdkxm.Qualityproblem;
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
 * 质量问题清单(Qualityproblem)表数据库访问层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:04
 */
@Mapper
@Repository
public interface QualityproblemMapper extends MPJBaseMapper<Qualityproblem> {
    /**
     * 统计数量
     *
     * @param userId
     * @param projectId
     * @return {@link java.lang.Integer}
     */
    Integer getCountByUserIdAndProjectId(Long userId, Set<Long> projectIds);

    /**
     * 查询用
     *
     * @param wrapper wrapper
     * @return {@link Page< Qualityproblem>}
     */
    <T> IPage<Qualityproblem> selectPageInfo(Page<T> page, @Param(Constants.WRAPPER) Wrapper<?> wrapper);
    /**
     * id查询
     *
     * @param id tableid
     * @return {@link com.alibaba.fastjson.JSONObject}
     */
    JSONObject selectInfoById(@Param("id") Long id);
}
