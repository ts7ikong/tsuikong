package cc.mrbird.febs.server.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cc.mrbird.febs.common.core.entity.system.UserRole;

import java.util.Set;

/**
 * @author MrBird
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    @Select("SELECT\n" + "\tGROUP_CONCAT(CONCAT( ur.ROLE_ID,'') ) test \n" + "FROM\n" + "\tt_user u\n"
        + "\tLEFT JOIN t_user_role ur ON u.USER_ID = ur.USER_ID \n" + "WHERE\n" + "\tu.USER_ID = #{userId} \n"
        + "GROUP BY\n" + "\tu.USER_ID")
    String getUserRoleByUserId(Long userId);

    @Select("SELECT p.PROJECT_ID from p_project p where p.IS_DELETE = 0")
    Set<Long> getAllProjectIds();
}