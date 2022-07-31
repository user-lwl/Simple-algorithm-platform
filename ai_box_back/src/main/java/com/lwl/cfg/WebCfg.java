package com.lwl.cfg;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebCfg implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry){
        // 表示对所有路径开放全局跨域访问权限
        CorsRegistration cors = registry.addMapping("/**");
        // 允许所有人跨域
        cors.allowedOriginPatterns("*");
        // 是否允许发送Cookie信息
        cors.allowCredentials(true);
        // 那些HTTP方法允许跨域访问
        cors.allowedMethods("GET", "POST");
    }
}
