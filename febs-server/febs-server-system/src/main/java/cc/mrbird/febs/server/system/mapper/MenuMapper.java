package cc.mrbird.febs.server.system.mapper;

import cc.mrbird.febs.common.core.entity.system.ButtonDto;
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
     * @param projectId
     * @return 用户权限集
     */
    List<Menu> findUserPermissions(String username);

    /**
     * 获取用户权限集
     *
     * @param username 用户名
     * @param projectId
     * @return 用户权限集
     */
    String findUserPermissionNews(@Param("userId") Long userId);

    /**
     * 获取用户菜单
     *
     * @param username 用户名
     * @return 用户菜单
     */
    List<Menu> findUserMenus(String username);

    List<Menu> findNewUserMenus(@Param("userId") Long userId);

    List<Menu> getNewMenus();

    List<Map<String, Object>> selectProjectMaps();

    /**
     * 前端 基于button校验
     *
     * @param userId 用户id
     * @return {@link List<ButtonDto>}
     */
    List<ButtonDto> getButtonUserAuth(@Param("userId") Long userId);

}