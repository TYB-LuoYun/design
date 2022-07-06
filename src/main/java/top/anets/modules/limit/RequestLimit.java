package top.anets.modules.limit;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * @author LuoYun
 * @since 2022/7/6 11:15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface RequestLimit {
    /**
     * 允许访问的最大次数
     */
    int maxCount() default Integer.MAX_VALUE;

    /**
     * 时间段，单位为秒，默认值一分钟
     */
    long time() default 6;
}
