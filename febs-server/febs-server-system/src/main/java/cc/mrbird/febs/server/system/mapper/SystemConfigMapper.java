package cc.mrbird.febs.server.system.mapper;

import cc.mrbird.febs.common.core.entity.system.SystemConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/6/2 16:38
 */
@Mapper
public interface SystemConfigMapper extends BaseMapper<SystemConfig> {
    /**
     * 查询是否有同名的key
     *
     * @param key key
     * @return {@link Integer}
     */
    Integer selectCountByKey(@Param("key") String key);
}
