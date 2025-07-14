package cc.mrbird.febs.common.core.annotation;

import cc.mrbird.febs.common.core.entity.Operate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/30 20:11
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OperateLog {
    Class<? extends BaseMapper> mapper();

    Class<? extends Serializable> className();

    /**
     * {@link Operate}
     */
    String type() default Operate.TYPE_ADD;
}
