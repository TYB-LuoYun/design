package top.anets.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.anets.modules.verify.VerifyInterceptor;


@Configuration
public class WebConfig implements WebMvcConfigurer {


//    @Bean
//    public VerifyInterceptor authenticationInterceptor(){
//        return new VerifyInterceptor();
//    }




    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(authenticationInterceptor()).addPathPatterns("/**");
    }


}
