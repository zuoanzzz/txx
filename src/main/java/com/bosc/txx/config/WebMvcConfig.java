package com.bosc.txx.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;
    
    @Autowired
    private LoggingInterceptor loggingInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 所有接口
                .allowCredentials(false) // 改为false，因为使用JWT而不是cookie
                .allowedOriginPatterns(
                        "http://127.0.0.1:5177",
                        "http://localhost:5177")
                .allowedMethods(new String[]{"GET", "POST", "PUT", "DELETE", "OPTIONS"}) // 添加OPTIONS方法
                .allowedHeaders("*")
                .exposedHeaders("*");
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/account/get-import-template",
                        "/transaction/get-import-template"
                );

        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/user/**",
                        "/error",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/doc.html",
                        "/favicon.ico",
                        "/static/**",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/account/get-import-template",
                        "/transaction/get-import-template"  // 排除文件下载模板接口
                );

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射本地文件系统路径到URL
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:./uploads/")
                .addResourceLocations("classpath:/static/images/");
    }
}


