package cc.mrbird.febs.server.system.service;

import cc.mrbird.febs.common.core.entity.router.VueRouter;
import cc.mrbird.febs.common.core.entity.system.Menu;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 获取用户权限
     *
     * @param username 用户名
     * @return 用户权限
     */
    String findUserPermissions(String username);

    /**
     * 获取用户菜单
     *
     * @param username 用户名
     * @return 用户菜单
     */
    List<Menu> findUserMenus(String username);

    Map<String, Object> getUserOldRouters(String username) throws FebsException;

    /**
     * 获取用户菜单
     *
     * @param menu menu
     * @return 用户菜单
     */
    Map<String, Object> findMenus(Menu menu);

    /**
     * 获取用户路由
     *
     * @param username 用户名
     * @return 用户路由
     */
    List<VueRouter<Menu>> getUserRouters(String username);

    /**
     * 获取菜单列表
     *
     * @param menu menu
     * @return 菜单列表
     */
    List<Menu> findMenuList(Menu menu);

    /**
     * 创建菜单
     *
     * @param menu menu
     */
    void createMenu(Menu menu);

    /**
     * 更新菜单
     *
     * @param menu menu
     */
    void updateMenu(Menu menu);

    /**
     * 递归删除菜单/按钮
     *
     * @param menuIds menuIds
     */
    void deleteMeuns(String[] menuIds);

    Map<String, Object> findMenus(Menu menu, HttpServletRequest request);

    /**
     * 优化权限
     *
     * @return {@link java.util.Map<java.lang.String,java.lang.Object>}
     */
    Map<String, Object> getUserRouters() throws FebsException;

    /**
     * 优化菜单
     *
     * @param menu 菜单实体
     * @param request 请求
     * @return {@link Map< String, Object>}
     */
    Map<String, Object> findNewMenus(Menu menu, HttpServletRequest request) throws FebsException;

}
