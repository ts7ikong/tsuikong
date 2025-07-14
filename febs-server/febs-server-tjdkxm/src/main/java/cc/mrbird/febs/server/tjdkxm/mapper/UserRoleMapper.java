package cc.mrbird.febs.server.tjdkxm.mapper;

import cc.mrbird.febs.common.core.entity.system.UserRole;
import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author MrBird
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {}