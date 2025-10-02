package com.example.springboots.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 添加配置注解
public class InterceptorConfig implements WebMvcConfigurer {
    // 注册拦截器为Bean
    @Bean
    public com.example.springboots.common.JwtInterceptor jwtInterceptor() {
        return new com.example.springboots.common.JwtInterceptor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 使用上面定义的Bean
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns("/login", "/register"); // 排除登录注册接口
    }
}
