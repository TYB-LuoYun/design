package top.anets.modules.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ftm
 * @date 2023-11-17 17:54
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface CacheUpdate {
    String name() default "";
    String key() default "";
    boolean delete() default false;
}
