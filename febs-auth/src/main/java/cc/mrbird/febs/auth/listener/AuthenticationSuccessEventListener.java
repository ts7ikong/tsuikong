package cc.mrbird.febs.auth.listener;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import cc.mrbird.febs.auth.mapper.MenuMapper;
import cc.mrbird.febs.auth.mapper.UserMapper;
import cc.mrbird.febs.auth.mapper.UserRoleMapper;
import cc.mrbird.febs.common.core.entity.FebsAuthUser;
import cc.mrbird.febs.common.core.entity.constant.FebsConstant;
import cc.mrbird.febs.common.core.entity.constant.RedisKey;
import cc.mrbird.febs.common.core.entity.system.MenuUserAuthDto;
import cc.mrbird.febs.common.core.entity.system.SystemUser;
import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
import cc.mrbird.febs.common.core.utils.DateUtil;
import cc.mrbird.febs.common.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;

/**
 * spring-security-oauth2 登录或者认证成功监听
 *
 * @author ZNKJ-R
 * @version V1.0
 * @date 2022/1/19 11:33
 */
@Slf4j
@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;
    private static final String CLASS_NAME =
            "org.springframework.security.authentication" + ".UsernamePasswordAuthenticationToken";

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        // 这里的事件源除了登录事件（UsernamePasswordAuthenticationToken）还有可能是token验证事件源（OAuth2Authentication）
        if (!event.getSource().getClass().getName().equals(CLASS_NAME)) {
            return;
        }
        Authentication authentication = event.getAuthentication();
        // 这里还有oAuth2的客户端认证的事件，需要做一个判断
        if (authentication.getDetails() != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof FebsAuthUser) {
                FebsAuthUser user = (FebsAuthUser) principal;
                userAuthRelatedInfo(user.getUserId());
            }
        }
    }

    private synchronized void userAuthRelatedInfo(Long userId) {
        try {
            // 更新用户登录
            // 删除key
            redisService.del(RedisKey.DATA_AUTH_PREFIX + userId, RedisKey.DATA_FRONT_AUTH_PREFIX + userId,
                    RedisKey.DATA_PROJECT + userId);
            SystemUser user = new SystemUser();
            user.setLastLoginTime(DateUtil.getNowdateTimeToString());
            userMapper.update(user, new LambdaQueryWrapper<SystemUser>().eq(SystemUser::getUserId, userId));
            // 菜单权限
            userAuth(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取用户的所有项目
     *
     * @param userId 用户id
     * @return {@link Set<Long>}
     */
    private Set<Long> getHasProject(Long userId) {
        Map<String, String> allProject = userRoleMapper.getAllProject(userId);
        if (allProject == null || allProject.isEmpty()) {
            return Collections.emptySet();
        }
        Set<Long> longs = new HashSet<>(8);
        longs.add(-1L);
        Set<Map.Entry<String, String>> entries = allProject.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            longs.addAll(Arrays.stream(entry.getValue().split(",")).map(Long::valueOf).collect(Collectors.toSet()));
        }
        redisService.set(RedisKey.DATA_PROJECT + userId, longs);
        return longs;
    }


    public static void main(String[] args) {
        String aa = "1";
        String bb = "3,4,5";
        Set<String> a = Arrays.stream(aa.split(",")).collect(Collectors.toSet());
        a.addAll(Arrays.stream(bb.split(",")).collect(Collectors.toSet()));
        System.out.println(a);
    }

    /**
     * 用户菜单 和按钮权限
     *
     * @param userId 用户id
     */
    private void userAuth(Long userId) {
        String key = RedisKey.DATA_AUTH_PREFIX + userId;
        Set<Long> projectIds = userRoleMapper.getAllProjectIds();
        Set<Long> longs = getHasProject(userId);
        // 用户拥有的roleid
        String roleIds = userRoleMapper.getUserRoleByUserId(userId);
        // 用户菜单、按钮权限
        List<MenuUserAuthDto.MenuButtonDto> auths = new ArrayList<>(8);
        // 获取用户菜单权限 和按钮权限
        List<MenuUserAuthDto.MenuButtonDto> menuUserAuth = menuMapper.getMenuUserAuth(userId);
        AtomicBoolean flag = new AtomicBoolean(true);
        // 拥有的项目
        String projectIdString = Strings.join(projectIds, ',');
        // 是否有特殊权限
        boolean hasSpecial = false;
        // 组合数据
        for (MenuUserAuthDto.MenuButtonDto userAuth : menuUserAuth) {
            flag.set(true);
            auths.stream().filter(auth -> userAuth.getMenuId().equals(auth.getMenuId())).forEach(auth -> {
                flag.set(false);
                // 菜单的项目id
                Set<String> menuButtonDtoProjectIds = Arrays.stream(auth.getProjectIds().split(",")).collect(Collectors.toSet());
                menuButtonDtoProjectIds.addAll(Arrays.stream(userAuth.getProjectIds().split(",")).collect(Collectors.toSet()));
                if ("2".equals(userAuth.getRoleType())) {
                    Set<String> collect = Arrays.stream(projectIdString.split(",")).collect(Collectors.toSet());
                    menuButtonDtoProjectIds.addAll(collect);
                }
                auth.setProjectIds(Strings.join(menuButtonDtoProjectIds, ','));
                // 按钮
                List<MenuUserAuthDto.ButtonDto> buttons = auth.getButtons();
                buttons.addAll(userAuth.getButtons());
                if ("2".equals(userAuth.getRoleType())) {
                    buttons.parallelStream().forEach(buttonDto -> buttonDto.setProjectIds(projectIdString));
                }
                auth.setButtons(buttons);
            });
            if (flag.get()) {
                if ("2".equals(userAuth.getRoleType())) {
                    hasSpecial = true;
                    userAuth.setProjectIds(projectIdString);
                    List<MenuUserAuthDto.ButtonDto> buttons = userAuth.getButtons();
                    buttons.parallelStream().forEach(buttonDto -> buttonDto.setProjectIds(projectIdString));
                    userAuth.setButtons(buttons);
                }
                auths.add(userAuth);
            }
        }


        AuthUserModel authUserModel = new AuthUserModel().setUserId(userId);
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
                authUserModel.setAuths(auths);
                if (hasSpecial) {
                    authUserModel.setProjectIds(projectIds);
                } else {
                    authUserModel.setProjectIds(longs);
                }
            }
        } else {
            authUserModel.setAuths(new ArrayList<>());
            authUserModel.setKey(AuthUserModel.KEY_NONE);
            Set<Long> ids = new HashSet<>();
            ids.add(-1L);
            authUserModel.setProjectIds(ids);
        }
        redisService.set(key, authUserModel);
    }
}