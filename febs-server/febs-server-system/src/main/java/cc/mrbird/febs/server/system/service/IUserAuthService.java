package cc.mrbird.febs.server.system.service;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/4/8 19:26
 */

import cc.mrbird.febs.common.core.entity.system.SystemUser;
import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;
import java.util.Set;

/**
 * @author MrBird
 */
public interface IUserAuthService extends IService<SystemUser> {

    /**
     * 获取用户数据权限
     * <p>
     * 1 超级管理员 2 projectIds 项目负责人 3 projectIds 员工 4 projectIds 临时 5 无角色无权限
     * </p>
     *
     * @return {@link Map < String, Set < Long>>}
     */
    AuthUserModel userBackendPermissions(Long userId);

    /**
     * 获取用户数据权限
     * <p>
     * 1 超级管理员 2 projectIds 项目负责人 3 projectIds 员工 4 projectIds 临时 5 无角色无权限
     * </p>
     *
     * @return {@link Map < String, Set < Long>>}
     */
    AuthUserModel userFrontEndPermission(Long userId);

}