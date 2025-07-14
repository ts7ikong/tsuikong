package cc.mrbird.febs.server.system.mapper;

import cc.mrbird.febs.common.core.entity.system.Version;
import cc.mrbird.febs.server.system.dto.VersionDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/4/28 16:53
 */
@Mapper
public interface VersionMapper extends BaseMapper<Version> {
    /**
     * 查询是否有新版本
     *
     * @param version 版本号
     * @return {@link cc.mrbird.febs.common.core.entity.system.Version}
     */
    VersionDto selectByVersion(String version);
}
