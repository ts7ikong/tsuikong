package cc.mrbird.febs.server.system.service;


import cc.mrbird.febs.common.core.entity.system.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;
import org.omg.CORBA.OBJ_ADAPTER;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author MrBird
 */
public interface IUserRoleService extends IService<UserRole> {

    /**
     * 删除角色用户管理关系
     *
     * @param roleIds 角色id数组
     */
    void deleteUserRolesByRoleId(String[] roleIds);

    /**
     * 删除角色用户管理关系
     *
     * @param userIds 用户id数组
     */
    void deleteUserRolesByUserId(String[] userIds);


    void deleteUserRoleByUserId(String userId);


}
