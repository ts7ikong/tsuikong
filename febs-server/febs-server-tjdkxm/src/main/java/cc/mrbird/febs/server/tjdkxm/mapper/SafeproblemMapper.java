package cc.mrbird.febs.server.tjdkxm.mapper;

import cc.mrbird.febs.common.core.entity.tjdkxm.Qualityproblem;
import cc.mrbird.febs.common.core.entity.tjdkxm.Safeproblem;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * 安全隐患清单(Safeproblem)表数据库访问层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:04
 */
@Mapper
@Repository
public interface SafeproblemMapper extends MPJBaseMapper<Safeproblem> {
    /**
     * 统计数量
     *
     * @param userId
     * @return {@link java.lang.Integer}
     */
    Integer getCountByUserIdAndProjectId(Long userId, Set<Long> projectIds);
    /**
     * 查询
     *
     * @param tPage page
     * @param wrapper wrapper
     * @return {@link java.lang.Integer}
     */
    <T> Page<Safeproblem> selectPageInfo(Page<T> tPage, @Param(Constants.WRAPPER) Wrapper<?> wrapper);
    /**
     * id查询
     *
     * @param id tableid
     * @return {@link com.alibaba.fastjson.JSONObject}
     */
    JSONObject selectInfoById(@Param("id") Long id);

}
