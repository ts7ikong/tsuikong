package cc.mrbird.febs.server.tjdk.mapper;

import cc.mrbird.febs.common.core.entity.tjdk.Banner;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
/**
 * banner管理(Banner)表数据库访问层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:02
 */
@Mapper
@Repository 
public interface BannerMapper extends BaseMapper<Banner> {
    /**
     * id查询
     *
     * @param id tableid
     * @return {@link com.alibaba.fastjson.JSONObject}
     */
    JSONObject selectInfoById(@Param("id") Long id);

}
