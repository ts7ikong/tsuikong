package cc.mrbird.febs.server.tjdkxm.mapper;

import cc.mrbird.febs.common.core.entity.tjdkxm.Datamanagement;
import cc.mrbird.febs.common.core.entity.tjdkxm.DocumentClass;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/28 18:39
 */
@Mapper
public interface DocumentClassMapper extends BaseMapper<DocumentClass> {
    /**
     * id查询
     *
     * @param id tableid
     * @return {@link com.alibaba.fastjson.JSONObject}
     */
    JSONObject selectInfoById(@Param("id") Long id);
}
