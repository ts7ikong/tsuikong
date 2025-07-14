package cc.mrbird.febs.auth.mapper;

import cc.mrbird.febs.common.core.entity.system.Menu;
import cc.mrbird.febs.common.core.entity.system.MenuUserAuthDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 获取用户权限集
     *
     * @param username 用户名
     * @return 权限集合
     */
    List<Menu> findUserPermissions(String username);

    /**
     * 获取用户菜单权限 和按钮权限 以及对
     *
     * @param userId 用户id
     * @return 权限集合
     */
    List<MenuUserAuthDto.MenuButtonDto> getMenuUserAuth(@Param("userId") Long userId);

    /**
     * 获取用户权限集 type menuIds
     *
     * @return 权限集合
     */
    List<Map<String, String>> getTypeMenuIds();

    /**
     * 用户拥有权限的所有项目
     *
     * @param userId
     * @return {@link String}
     */
    Map<String, String> getAllProject(@Param("userId") Long userId);

}