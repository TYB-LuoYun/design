package top.anets.support.swagger;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author ftm
 * @date 2023/3/21 0021 17:05
 */

@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class Swagger2 {
    private String controller ="com.dicomclub.cdr.module.datacenter.controller";




    @Bean(value = "defaultApi1")
    public Docket defaultApi1() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //分组名称
                .groupName("开放API")
                .select()
                //这里指定Controller扫描包路径
                .apis(
                        RequestHandlerSelectors.basePackage(this.controller)
//                        RequestHandlerSelectors.withMethodAnnotation(PassToken.class)
                )
                .paths(PathSelectors.any())
                .build() ;
        return docket;
    }

//    @Bean(value = "defaultApi2")
//    public Docket defaultApi2() {
//        Docket docket = new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                //分组名称
//                .groupName("2.其他")
//                .select()
//                //这里指定Controller扫描包路径
//                .apis(
////                        RequestHandlerSelectors.basePackage(this.controller)
//                        RequestHandlerSelectors.withMethodAnnotation(PassToken.class)
//                )
//                .paths(PathSelectors.any())
//                .build() ;
//        return docket;
//    }



    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //.title("swagger-bootstrap-ui-demo RESTful APIs")
                .description("cdr open api")
                .termsOfServiceUrl("/doc.html")
//                        .contact(new Contact("爪洼笔记 ", "http://blog.52itstyle.vip", "345849402@qq.com"))
                .version("1.0")
                .title("影像中心")
                .build();
    }
}

