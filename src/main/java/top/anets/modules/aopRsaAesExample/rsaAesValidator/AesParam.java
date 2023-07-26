package top.anets.modules.aopRsaAesExample.rsaAesValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author LuoYun
 * @since 2022/6/21 9:44
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface AesParam {
    public String value() default "aop";
}
