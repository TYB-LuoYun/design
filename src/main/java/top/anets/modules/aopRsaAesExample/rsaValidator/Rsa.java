package top.anets.modules.aopRsaAesExample.rsaValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author LuoYun
 * @since 2022/6/21 9:40
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface Rsa {
    public String value() default "aop";
}
