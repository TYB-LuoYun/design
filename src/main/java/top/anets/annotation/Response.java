package top.anets.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author ftm
 * @date 2023/3/15 0015 9:40
 */
@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface Response {
}
