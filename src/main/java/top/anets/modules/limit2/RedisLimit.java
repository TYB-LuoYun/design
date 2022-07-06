package top.anets.modules.limit2;

import java.lang.annotation.*;

/**
 * @author LuoYun
 * @since 2022/7/6 14:03
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RedisLimit {

    // 资源名称
    String name() default "";

    // 资源key
    String key() default "";

    // 前缀
    String prefix() default "";

    // 时间
    int period();

    // 最多访问次数
    int count();

    // 类型
    LimitType limitType() default LimitType.CUSTOMER;

    // 提示信息
    String msg() default "系统繁忙,请稍后再试";

}