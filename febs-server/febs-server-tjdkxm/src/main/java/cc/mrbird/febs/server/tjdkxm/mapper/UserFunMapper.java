package cc.mrbird.febs.server.tjdkxm.mapper;

import cc.mrbird.febs.common.core.entity.tjdkxm.UserFun;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author ZNKJ-R
 * @version V1.0
 * @date 2022/1/18 20:35
 */
@Mapper
@Repository
public interface UserFunMapper extends MPJBaseMapper<UserFun> {
}
