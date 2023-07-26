package top.anets.support.swagger;//package com.dicomclub.cdr.support.swagger;
//
//import io.swagger.models.parameters.Parameter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.*;
//import springfox.documentation.oas.annotations.EnableOpenApi;
//import springfox.documentation.schema.ModelRef;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.service.ResponseMessage;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger.web.UiConfiguration;
//import springfox.documentation.swagger.web.UiConfigurationBuilder;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author ftm
// * @date 2023/3/21 0021 17:35
// */
// //请求地址：/swagger-ui/
//@Configuration
//@EnableOpenApi
//public class SwaggerConfig {
//
//    @Bean
//    public Docket createRestApi() {
//        return new Docket(DocumentationType.OAS_30) // v2 不同
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.dicomclub.cdr.module")) // 设置扫描路径
//                .build().apiInfo(apiInfo());
//    }
//
//
//
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("Demo API")
//                .description("API 接口文档")
//                .version("1.0.0")
//                .build();
//    }
//}