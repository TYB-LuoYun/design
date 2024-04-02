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
public @interface CappedCollection {
    long maxSize() default 304857600; //300MB
    long maxDocs() default -1; //-1代表不生效
    MongoDB mongoDB() default MongoDB.LOGDB;
}
