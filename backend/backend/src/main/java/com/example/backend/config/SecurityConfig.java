package com.example.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.example.backend.filter.JwtAuthenticationFilter;
import com.example.backend.service.CustomUserDetailsService;
import com.example.backend.utils.JwtUtils;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  // 依赖注入
  @Autowired
  private CustomUserDetailsService userDetailsService;

  @Autowired
  private JwtUtils jwtUtils;

  // 安全过滤器链配置
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(c -> c.disable()) // 禁用CSRF（因使用JWT）
        .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 无状态会话
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/users/login", "/api/users/register", "/api/products",
                "/api/products/*", "api/users")
            .permitAll() // 开放登录注册
            .anyRequest().authenticated()) // 其他请求需认证
        .addFilterBefore(new JwtAuthenticationFilter(userDetailsService, jwtUtils),
            UsernamePasswordAuthenticationFilter.class); // 添加JWT过滤器

    return http.build();
  }

  // 认证管理器
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
      throws Exception {
    return authConfig.getAuthenticationManager();
  }

  // 密码编码器
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }
}
