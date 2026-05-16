@Configuration
public class CorsConfig {
    
    @Bean
    public CorsFilter corsFilter() {
        // 创建跨域配置对象
        CorsConfiguration corsConfig = new CorsConfiguration();
        // 放行所有域名（开发环境用*，生产环境建议写具体域名，如http://localhost:8080）
        corsConfig.addAllowedOriginPattern("*");
        // 放行所有请求头（如Content-Type、Token、Authorization等）
        corsConfig.addAllowedHeader("*");
        // 放行所有请求方法（GET/POST/PUT/DELETE/OPTIONS等）
        corsConfig.addAllowedMethod("*");
        // 允许跨域携带Cookie/Token
        corsConfig.setAllowCredentials(true);
        // 预检请求有效期（单位秒），避免频繁发送OPTIONS预检请求，提升性能
        corsConfig.setMaxAge(3600L);
        // 配置跨域规则生效的接口路径（/** 代表所有接口）
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        // 返回跨域过滤器
        return new CorsFilter(source);
    }
}
