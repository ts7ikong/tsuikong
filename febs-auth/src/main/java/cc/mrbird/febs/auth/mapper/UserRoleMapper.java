package cc.mrbird.febs.auth.mapper;

import cc.mrbird.febs.common.core.entity.system.UserRole;
import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author MrBird
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 获取用户所有角色id
     *
     * @param userId 用户id
     * @return {@link String}
     */
    String getUserRoleByUserId(@Param("userId") Long userId);

    /**
     * 获取所有项目id
     *
     * @return {@link java.util.List<java.lang.Long>}
     */
    Set<Long> getAllProjectIds();

    /**
     * 获取用户拥有的所有项目
     *
     * @param userId 用户id
     * @return {@link Map<String,String>}
     */
    Map<String, String> getAllProject(Long userId);

}