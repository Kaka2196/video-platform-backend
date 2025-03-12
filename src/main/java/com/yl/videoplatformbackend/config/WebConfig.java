package com.yl.videoplatformbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 允许所有路径
                .allowedOrigins("http://localhost:3000")  // 允许的域名
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 允许的请求方式
                .allowedHeaders("*")  // 允许的请求头
                .allowCredentials(true)  // 是否允许携带 cookie
                .maxAge(3600);  // 预检请求的缓存时间（单位秒）
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射 /videos/** 到 C:/Users/Leon/Desktop/server/
        registry.addResourceHandler("/server/**")
                .addResourceLocations("file:C:/Users/Leon/Desktop/server/");
    }

}
