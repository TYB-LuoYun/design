package top.anets.modules.log.appender;

/**
 * @author ftm
 * @date 2024-04-08 17:39
 */

import top.anets.modules.log.enums.BusinessEnum;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface LogDb {
    /**
     * 业务名
     * @return
     */
    String name()  default  "";

    /**
     * 业务枚举类
     * @return
     */
    BusinessEnum business() default BusinessEnum.Custom;
}
