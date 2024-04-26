package top.anets.modules.log.appender;

/**
 * @author ftm
 * @date 2024-04-08 17:39
 */


import top.anets.modules.log.requestDbLog.enums.BusinessEnum;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface LogDb {
    /**
     * 日志所属分类
     * @return
     */
    String classify()  default  "Default";

    String business() default "";

    /**
     * 业务枚举类
     * @return
     */
    BusinessEnum businessEnum() default BusinessEnum.Custom;
}
