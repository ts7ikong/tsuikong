package cc.mrbird.febs.server.system.aspect;

import cc.mrbird.febs.common.core.annotation.AuthMenu;
import cc.mrbird.febs.common.core.annotation.AuthMenus;
import cc.mrbird.febs.common.core.entity.constant.RedisKey;
import cc.mrbird.febs.common.core.entity.system.MenuUserAuthDto;
import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.common.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/4/21 22:44
 */
@Aspect
@Component
@Order(1)
@Slf4j
public class AuthAspect extends BaseAspectSupport {
    @Autowired
    private RedisService redisService;

    @Pointcut("@annotation(cc.mrbird.febs.common.core.annotation.AuthMenu)"
        + "||@annotation(cc.mrbird.febs.common.core.annotation.AuthMenus)")
    public void pointcut() {}

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws FebsException {
        Method targetMethod = resolveMethod(point);
        checkAuth(targetMethod);
        try {
            return point.proceed();
        } catch (Throwable throwable) {
            String message = throwable.getMessage();
            throw new FebsException(message);
        }
    }

    /**
     * 权限校验
     */
    private void checkAuth(Method targetMethod) {
        AuthMenu authMenu = targetMethod.getAnnotation(AuthMenu.class);
        AuthMenus authMenus = targetMethod.getAnnotation(AuthMenus.class);
        if (authMenu != null || authMenus != null) {
            Long userId = FebsUtil.getCurrentUserId();
            String key = RedisKey.DATA_AUTH_PREFIX + userId;
            boolean flag = false;
            try {
                AuthUserModel userAuth = (AuthUserModel)redisService.get(key);
                Assert.notNull(userAuth, "");
                flag = AuthUserModel.KEY_ADMIN.equals(userAuth.getKey());
                List<MenuUserAuthDto.MenuButtonDto> auths = userAuth.getAuths();
                Assert.notNull(auths, "");
                if (authMenu != null) {
                    flag = flag || isFlag(authMenu, auths);
                }
                if (authMenus != null) {
                    AuthMenu[] value = authMenus.value();
                    for (AuthMenu authMenu1 : value) {
                        flag = flag || isFlag(authMenu1, auths);
                    }
                }
            } catch (Exception e) {
                flag = false;
            }
            if (!flag) {
                throw new IllegalArgumentException("权限不足");
            }
        }
    }

    /**
     * 内部校验方法
     *
     * @param authMenu 权限按钮
     * @param auths 用户拥有权限
     * @return {@link boolean}
     */
    private boolean isFlag(AuthMenu authMenu, List<MenuUserAuthDto.MenuButtonDto> auths) {
        boolean flag;
        String menuId = authMenu.value() + "";
        String buttonId = authMenu.buttonId() + "";
        boolean notVerifyMenu = "0".equals(menuId);
        boolean notVerifyButton = "-1".equals(buttonId);
        if (notVerifyMenu && notVerifyButton) {
            flag = true;
        } else {
            MenuUserAuthDto.MenuButtonDto menuButtonDto = auths.stream().filter(auth -> menuId.equals(auth.getMenuId()))
                .findFirst().orElse(new MenuUserAuthDto.MenuButtonDto());
            boolean hasMenuAuth = menuButtonDto.getMenuId() != null;
            flag = notVerifyMenu || hasMenuAuth;
            boolean hasButtonAuth =
                menuButtonDto.getButtons().parallelStream().anyMatch(button -> button.getButtonId().equals(buttonId));
            flag = flag && (notVerifyButton || hasButtonAuth);
        }
        return flag;
    }
}
