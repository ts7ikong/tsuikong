package cc.mrbird.febs.server.system.mapper;

import cc.mrbird.febs.common.core.entity.system.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author MrBird
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    @Select("SELECT\n" +
            "rm.*,m.MENU_NAME,m.CLASS_TYPE,p.PROJECT_NAME\n" +
            "FROM\n" +
            "t_role_menu rm\n" +
            "LEFT JOIN t_menu m ON m.MENU_ID = rm.MENU_ID\n" +
            "LEFT JOIN p_project p on p.PROJECT_ID = rm.PROJECT_ID\n" +
            "WHERE\n" +
            "rm.ROLE_ID = #{roleId} AND rm.PROJECT_ID <0\n")
    List<RoleMenu> listById(Long roleId);
}