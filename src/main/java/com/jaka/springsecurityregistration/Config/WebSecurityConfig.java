package com.jaka.springsecurityregistration.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
  private static final String[] WHITE_LIST_URLS = {
    "/register",
    "/hello",
    "/v3/api-docs/**",
    "/swagger-ui/**",
    "/swagger-ui.html",
    "/verifyRegistration*",
    "/resendVerifyToken*",
    "/savePassword*",
    "/resetPassword*",
    "/changePassword*"
  };
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(11);
  }
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf((csrf) -> csrf.disable())// Disable CSRF for non-browser clients
      .authorizeHttpRequests(request -> request
        .requestMatchers(WHITE_LIST_URLS).permitAll()
        .anyRequest().authenticated());
    return http.build();
  }
}
