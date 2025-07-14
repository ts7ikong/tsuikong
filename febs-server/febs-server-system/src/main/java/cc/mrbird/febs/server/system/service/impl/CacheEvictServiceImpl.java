package cc.mrbird.febs.server.system.service.impl;

import cc.mrbird.febs.common.core.entity.constant.FebsConstant;
import cc.mrbird.febs.common.core.entity.constant.RedisKey;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.common.redis.service.RedisService;
import cc.mrbird.febs.server.system.service.CacheEvictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/14 10:47
 */

@Service
public class CacheEvictServiceImpl implements CacheEvictService {
    @Autowired
    private RedisService redisService;

    /**
     * 更新用户
     *
     * @param projectId 项目id
     */
    @Override
    public void upUser(Long projectId) {
        redisService.del(RedisKey.USER_ALL);
        Map<String, Object> map = new HashMap<>();
        map.put("-1", Collections.emptyMap());
        redisService.hmset(RedisKey.PROJECT_USER_ALL, map, 1L);
    }

    /**
     * 更新用户的数据权限 角色类型
     *
     * @param userId
     */
    @Override
    public void upUserAuth(Long userId) {
        // redisService.del(RedisKey.DATA_AUTH_PREFIX + userId);
    }

    /**
     * 更新用户
     */
    @Override
    public void upUser() {
        Map<String, Object> map = new HashMap<>();
        map.put("-1", Collections.emptyMap());
        redisService.hmset(RedisKey.PROJECT_USER_ALL, map, 1L);
        redisService.del(RedisKey.USER_ALL);
    }

    /**
     * 更新用户
     */
    @Override
    public void upPwd() {
        String token = FebsUtil.getCurrentTokenValue();
        redisService.delKeys("access:" + token + "*");
    }

    /**
     * 角色更新
     *
     * @param userId 用户id
     */
    @Override
    public void upAuthority(Long userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("-1", Collections.emptyMap());
        // redisService.del(RedisKey.DATA_AUTH_PREFIX + userId);
    }

    /**
     * 更新所有用户 权限
     */
    @Override
    public void upAuthority() {
        // Map<String, Object> map = new HashMap<>();
        // map.put("-1", Collections.emptyMap());
        // redisService.delKeys(RedisKey.DATA_AUTH_PREFIX + "*");
    }
}
