package com.boot.yuncourier.crossOrigin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Author: skwen
 * @Description: MyConfiguration-跨域訪問配置
 * @Date: 2020-01-31
 */
@Configuration
public class MyConfiguration {
    @Value("${config.crossOrigin}")
    private String crossOrigin;
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new  WebMvcConfigurer() {

            @Override

            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping(crossOrigin)
                        .allowCredentials(true)
                        .maxAge(3600)
                        .allowedMethods("POST");
            }

        };

    }
}
