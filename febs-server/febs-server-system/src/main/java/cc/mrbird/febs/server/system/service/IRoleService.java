package cc.mrbird.febs.server.system.service;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.system.Role;
import cc.mrbird.febs.common.core.entity.system.RoleDto;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IRoleService extends IService<Role> {

    IPage<RoleDto> findNewRoles(Role role, QueryRequest request);

    Role findNewRoles3(Long roleId);

    /**
     * 查找角色分页数据
     *
     * @param role role
     * @param request request
     * @param isProject 是否项目端
     * @return 角色分页数据
     */
    IPage<Role> findRoles(Role role, QueryRequest request);

    /**
     * 获取用户角色
     *
     * @param username 用户名
     * @return 角色集
     */
    List<Role> findUserRole(String username);

    /**
     * 获取所有角色
     *
     * @return 角色列表
     */
    List<Role> findAllRoles(Role role);

    /**
     * 通过名称获取角色
     *
     * @param roleName 角色名称
     * @param project
     * @return 角色
     */
    Role findByName(String roleName);

    /**
     * 创建角色
     *
     * @param role role
     * @param project
     */
    void createRole(Role role) throws FebsException;

    /**
     * 创建角色
     *
     * @param role role
     * @param project
     */
    void createNewRole(RoleDto.Params role) throws FebsException;

    /**
     * 删除角色
     *
     * @param roleIds 角色id数组
     */
    void deleteRoles(String[] roleIds) throws FebsException;

    /**
     * 更新角色
     *
     * @param role role
     */
    void updateRole(Role role) throws FebsException;

    /**
     * 更新角色
     *
     * @param role role
     */
    void updateNewRole(RoleDto.Params role) throws FebsException;

    void deleteRole(String roleId) throws FebsException;

    List<Role> findAllNewRoles(Role role);
}
