package cc.mrbird.febs.server.tjdk.service;

import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
import cc.mrbird.febs.common.core.entity.tjdkxm.UserFun;
import cc.mrbird.febs.common.core.entity.tjdkxm.UserProject;

import java.util.List;
import java.util.Map;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/8 17:05
 */
public interface CacheableService {
    /**
     * 获取用户在项目中的系统通知
     *
     * @param projectId 项目id
     * @return {@link List< Map< String, Object>>}
     */
    Map<String, Object> getUserSysnotifyNotRead(Long userId,Long projectId);

    AuthUserModel getUserAuth(Long userId);
}
