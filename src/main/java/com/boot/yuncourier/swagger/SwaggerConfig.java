package com.boot.yuncourier.swagger;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.WebMvcRequestHandler;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: skwen
 * @Description: SwaggerConfig-swagger配置
 * @Date: 2020-02-01
 */

@EnableSwagger2
@Configuration                 // 声明当前配置类
public class SwaggerConfig extends WebMvcConfigurerAdapter {
    @Value("${swagger.disable}")
    private boolean disable;
    @Value("${swagger.basePackage}")
    private String basePackage;// controller接口所在的包
    @Value("${swagger.title}")
    private String title;   // 当前文档的标题
    @Value("${swagger.description}")
    private String description;     // 当前文档的详细描述
    @Value("${swagger.version}")
    private String version; // 当前文档的版本
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .version(version)
                .build();
    }
}

