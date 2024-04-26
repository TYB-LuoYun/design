package top.anets.modules.log.requestDbLog.aop;




import top.anets.modules.log.requestDbLog.enums.BusinessEnum;

import java.lang.annotation.*;

/**
 * @author ftm
 * @date 2023-12-07 14:15
 */
@Target({  ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {
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

    /**
     * 用于你自定义业务编码 ，如果你不想用枚举类
     * @return
     */
    String businessCode() default "";


    /**
     * el表达式取对应
     * @return
     */
    String userCode() default "";
    String userName() default "";
    String organCode() default "";

}
