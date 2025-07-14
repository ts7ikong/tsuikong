package cc.mrbird.febs.server.tjdkxm.mapper;

import cc.mrbird.febs.common.core.entity.tjdkxm.Safeplan;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * 安全检查计划(Safeplan)表数据库访问层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:03
 */
@Mapper
@Repository
public interface SafeplanMapper extends BaseMapper<Safeplan> {

    Integer getAcceptanceCount(Set<Long> projectIds, Long userId, @Param(Constants.WRAPPER) Wrapper<?> wrapper);

    Integer notChecked(Long userId, Set<Long> projectIds);

    /**
     * 查询
     *
     * @param page 分页
     * @param wrapper wapper
     * @return {@link IPage< Safeplan>}
     */
    <T> IPage<Safeplan> selectPageInfo(Page<T> page, @Param(Constants.WRAPPER) Wrapper<?> wrapper);
    /**
     * id查询
     *
     * @param id tableid
     * @return {@link com.alibaba.fastjson.JSONObject}
     */
     JSONObject selectInfoById(@Param("id") Long id);

}

