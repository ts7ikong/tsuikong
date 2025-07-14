package cc.mrbird.febs.common.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.List;

/**
 * 判断有无菜单权限 在判断有无按钮权限
 * <p>
 * value 默认为0[不校验菜单] 菜单id buttonId 默认为 -1[不校验按钮] 按钮id
 * </p>
 *
 * @author 14059
 * @version V1.0
 * @date 2022/3/30 20:11
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthMenu {
    /**
     * value 菜单id
     */
    int value() default 0;

    /**
     * buttonId 按钮id
     */
    int buttonId() default -1;
}
