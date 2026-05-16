package com.sheng.heritagenexus.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author 张福生
 * @version 1.0
 * @data 2026/3/14 14:16
 * 密码加密器配置类，确保能注入PasswordEncoder
 */
@Configuration
public class SecurityConfig {

    /**
     * 配置BCrypt密码加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    /**
     * 关闭Spring Security的默认拦截规则
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 关闭CSRF防护（前后端分离场景常用）
            .csrf(csrf -> csrf.disable())
            // 关闭默认的登录验证（允许匿名访问所有接口）
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // 允许所有请求无需认证
            )
            // 关闭默认的表单登录
            .formLogin(form -> form.disable())
            // 关闭默认的HTTP Basic认证
            .httpBasic(basic -> basic.disable());
        return http.build();
    }
}