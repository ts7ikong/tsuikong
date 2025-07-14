package cc.mrbird.febs.server.system.service;

import cc.mrbird.febs.common.core.entity.constant.FebsConstant;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Async;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/23 16:11
 */
public interface CacheEvictService {
    /**
     * 更新用户
     */
    void upUser(Long userId);

    /**
     * 更新用户的数据权限 角色类型
     *
     * @param userId
     */
    void upUserAuth(Long userId);

    /**
     * 更新用户
     */
    void upUser();

    /**
     * 更新用户
     */
    void upPwd();

    /**
     * 更新用户角色权限 system
     *
     * @param userId 用户id
     */
    void upAuthority(Long userId);

    /**
     * 更新用户角色权限 system 按钮一样操作
     */
    void upAuthority();
}
