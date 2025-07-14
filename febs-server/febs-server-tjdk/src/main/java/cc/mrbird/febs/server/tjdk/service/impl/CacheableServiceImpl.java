package cc.mrbird.febs.server.tjdk.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import cc.mrbird.febs.common.core.entity.constant.RedisKey;
import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
import cc.mrbird.febs.common.core.entity.tjdkxm.UserSysnotify;
import cc.mrbird.febs.common.redis.service.RedisService;
import cc.mrbird.febs.server.tjdk.mapper.UserRoleMapper;
import cc.mrbird.febs.server.tjdk.mapper.UserSysnotifyMapper;
import cc.mrbird.febs.server.tjdk.service.CacheableService;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/26 21:01
 */
@Service
public class CacheableServiceImpl implements CacheableService {
    @Autowired
    private UserSysnotifyMapper userSysnotifyMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserRoleMapper userRoleMapper;

    /**
     * 获取用户在项目中的系统通知
     *
     * @param projectId 项目id
     */
    @Override
    public Map<String, Object> getUserSysnotifyNotRead(Long userId, Long projectId) {
        Map<String, Object> hashMap = new HashMap<>(1);
        Integer count = userSysnotifyMapper.selectCount(new LambdaQueryWrapper<UserSysnotify>()
            .select(UserSysnotify::getUserId).eq(UserSysnotify::getUserId, userId).eq(UserSysnotify::getIsDelete, 0)
            .eq(UserSysnotify::getIsRead, 0));
        hashMap.put("notification", count);
        return hashMap;
    }

    /**
     * 获取用户的数据权限 角色类型
     */
    @Override
    public AuthUserModel getUserAuth(Long userId) {
        return (AuthUserModel)redisService.get(RedisKey.DATA_AUTH_PREFIX + userId);
    }
}
