package top.anets.modules.cache.enums;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ftm
 * @date 2023/5/10 0010 10:13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Cache {
    boolean async() default true;
    String name() default "";
    String key() default "";
    long expire() default 60 ;
    long refresh() default 60 ;
}
