package cc.mrbird.febs.server.tjdkxm.mapper;

import cc.mrbird.febs.common.core.entity.tjdkxm.Danger;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
/**
 * 重大危险源记录表(Danger)表数据库访问层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:02
 */
@Mapper
@Repository 
public interface DangerMapper extends MPJBaseMapper<Danger> {

    <T> IPage<Danger> selectPageInfo(Page<T> page, @Param(Constants.WRAPPER) Wrapper<?> wrapper);
    /**
     * id查询
     *
     * @param id tableid
     * @return {@link com.alibaba.fastjson.JSONObject}
     */
    JSONObject selectInfoById(@Param("id") Long id);
}
