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
            // 关闭CSRF防护
            .csrf(csrf -> csrf.disable())
            // 关闭默认的登录验证
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
