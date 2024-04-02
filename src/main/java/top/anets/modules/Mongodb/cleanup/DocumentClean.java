package top.anets.modules.Mongodb.cleanup;

import org.springframework.data.annotation.Persistent;

import java.lang.annotation.*;

/**
 * @author ftm
 * @date 2024-04-01 17:34
 */
@Persistent
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface DocumentClean {
    String timeField() default "";
    long   timeExpire() default 0;
    long maxSize() default 0;
    long maxDocuments() default 0;
}
