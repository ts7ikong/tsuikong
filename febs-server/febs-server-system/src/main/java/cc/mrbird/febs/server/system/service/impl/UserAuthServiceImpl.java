package cc.mrbird.febs.server.system.service.impl;

import cc.mrbird.febs.common.core.entity.constant.FebsConstant;
import cc.mrbird.febs.common.core.entity.constant.RedisKey;
import cc.mrbird.febs.common.core.entity.system.ButtonDto;
import cc.mrbird.febs.common.core.entity.system.MenuUserAuthDto;
import cc.mrbird.febs.common.core.entity.system.SystemUser;
import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.common.redis.service.RedisService;
import cc.mrbird.febs.server.system.mapper.MenuMapper;
import cc.mrbird.febs.server.system.mapper.UserMapper;
import cc.mrbird.febs.server.system.mapper.UserRoleMapper;
import cc.mrbird.febs.server.system.service.IUserAuthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author MrBird
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserAuthServiceImpl extends ServiceImpl<UserMapper, SystemUser> implements IUserAuthService {
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RedisService redisService;


    /**
     * 获取用户数据权限
     * <p>
     * 1 超级管理员 2 projectIds 项目负责人 3 projectIds 员工 4 projectIds 临时 5 无角色无权限
     * </p>
     *
     * @return {@link Map < String, Set< Long>>}
     */
    @Override
    public AuthUserModel userBackendPermissions(Long userId) {
        return (AuthUserModel)redisService.get(RedisKey.DATA_AUTH_PREFIX + userId);
    }

    /**
     * 获取用户数据权限
     * <p>
     * 1 超级管理员 2 projectIds 项目负责人 3 projectIds 员工 4 projectIds 临时 5 无角色无权限
     * </p>
     *
     * @return {@link Map < String, Set < Long>>}
     */
    @Override
    public AuthUserModel userFrontEndPermission(Long userId) {
        userId = userId == null ? FebsUtil.getCurrentUserId() : userId;
        String key = RedisKey.DATA_FRONT_AUTH_PREFIX + userId;
        if (redisService.hasKey(key)) {
            return (AuthUserModel)redisService.get(key);
        }
        return getUserFrontEndPermission(userId, key);
    }

    private AuthUserModel getUserFrontEndPermission(Long userId, String key) {
        AuthUserModel authUserModel = new AuthUserModel().setUserId(userId);
        try {
            Set<Long> projectIds = userRoleMapper.getAllProjectIds();
            String roleIds = userRoleMapper.getUserRoleByUserId(userId);
            List<ButtonDto> auths = menuMapper.getButtonUserAuth(userId);
            String projectIdString = Strings.join(projectIds, ',');
            auths.parallelStream().forEach(auth -> {
                if ("2".equals(auth.getRoleType()) && "2".equals(auth.getType())) {
                    auth.setProjectIds(projectIdString);
                }
            });
            if (StringUtils.isNotBlank(roleIds)) {
                String[] split = roleIds.split(",");
                Set<String> collect = Arrays.stream(split).collect(Collectors.toSet());
                if (collect.contains(FebsConstant.ADMIN_USER)) {
                    authUserModel.setKey(AuthUserModel.KEY_ADMIN);
                    authUserModel.setProjectIds(projectIds);
                } else {
                    if (collect.contains(FebsConstant.TEMP_USER)) {
                        authUserModel.setKey(AuthUserModel.KEY_TEMP);
                    } else if (collect.contains(FebsConstant.PROJECT_USER)) {
                        authUserModel.setKey(AuthUserModel.KEY_PROJECT_ADMIN);
                    } else {
                        authUserModel.setKey(AuthUserModel.KEY_EMPLOYEE);
                    }
                    authUserModel.setButtonPermissions(auths);
                }
            } else {
                authUserModel.setAuths(new ArrayList<>());
                authUserModel.setKey(AuthUserModel.KEY_NONE);
            }
            redisService.set(key, authUserModel);
        } catch (Exception e) {
        }
        return authUserModel;
    }
}
