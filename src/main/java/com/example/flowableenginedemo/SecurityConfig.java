package com.example.flowableenginedemo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebSecurityCustomizer {

/*
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    return http
        .authorizeExchange()
        .pathMatchers("/deploy/holiday").permitAll()
        .pathMatchers("/tasks").permitAll()
        .pathMatchers("/tasks/assignee/{assignee}").permitAll()
        .pathMatchers("/tasks/{taskIndex}/assignee/{assignee}").permitAll()
        .pathMatchers("/tasks/{taskIndex}").permitAll()
        .pathMatchers("/tasks/{taskIndex}/variables").permitAll()
        .pathMatchers("/process/{processId}").permitAll()
        .anyExchange().authenticated()
        .and()
        .build();
  }
*/

  // userDetailService
  @Bean
  public CustomUserDetailsService customUserDetailsService() {
    return new CustomUserDetailsService();
  }

  @Bean
  public CustomReactiveAuthenticationManager customReactiveAuthenticationManager() {
    return new CustomReactiveAuthenticationManager(customUserDetailsService());
  }

  @Override
  public void customize(WebSecurity web) {
    web.ignoring().antMatchers("/deploy/holiday")
        .antMatchers("/tasks")
        .antMatchers("/tasks/assignee/{assignee}")
        .antMatchers("/tasks/{taskIndex}/assignee/{assignee}")
        .antMatchers("/tasks/{taskIndex}")
        .antMatchers("/tasks/{taskIndex}/variables")
        .antMatchers("/process/{processId}")
    ;

  }
}
