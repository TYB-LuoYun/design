package top.anets.support.mongodb.collection;

import org.springframework.data.annotation.Persistent;
import top.anets.support.mongodb.danamicdatasource.MongoDB;

import java.lang.annotation.*;

/**
 * @author ftm
 * @date 2024-04-02 14:11
 */
@Persistent
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface CleanUpCollection {
//  注意，如果实体类有索引注解，那么以下将不会生效
    String ttlField() default "";
    long   ttlSeconds() default -1; //-1代表不生效
    long cappedMaxSize() default 304857600; //300MB  capped是创建有限的集合
    long cappedMaxDocs() default -1; //-1代表不生效
    MongoDB mongoDB() default MongoDB.LOGDB;
}
