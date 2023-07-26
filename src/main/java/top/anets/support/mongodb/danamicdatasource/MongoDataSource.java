package top.anets.support.mongodb.danamicdatasource;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author ftm
 * @date 2023/3/9 0009 12:38
 */
@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface MongoDataSource {

    MongoDB value() default MongoDB.DATACENTER;
}