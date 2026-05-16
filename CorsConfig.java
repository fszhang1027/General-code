package com.sheng.heritagenexus.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author 张福生
 * @version 1.0
 * @data 2025/12/31 10:40
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        // 1. 创建跨域配置对象
        CorsConfiguration corsConfig = new CorsConfiguration();

        // 放行所有域名（开发环境用*，生产环境建议写具体域名，如http://localhost:8080）
        corsConfig.addAllowedOriginPattern("*");

        // 放行所有请求头（如Content-Type、Token、Authorization等）
        corsConfig.addAllowedHeader("*");

        // 放行所有请求方法（GET/POST/PUT/DELETE/OPTIONS等）
        corsConfig.addAllowedMethod("*");

        // 关键补充：允许跨域携带Cookie/Token（前端带认证信息必备）
        corsConfig.setAllowCredentials(true);

        // 可选：预检请求有效期（单位秒），避免频繁发送OPTIONS预检请求，提升性能
        corsConfig.setMaxAge(3600L);

        // 2. 配置跨域规则生效的接口路径（/** 代表所有接口）
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        // 3. 返回跨域过滤器
        return new CorsFilter(source);
    }
}
