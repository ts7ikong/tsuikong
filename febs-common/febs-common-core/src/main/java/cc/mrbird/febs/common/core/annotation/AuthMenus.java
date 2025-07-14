package cc.mrbird.febs.common.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 判断有无菜单权限 满足一个即可
 *
 * @author 14059
 * @version V1.0
 * @date 2022/3/30 20:11
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthMenus {
    AuthMenu[] value();
}
