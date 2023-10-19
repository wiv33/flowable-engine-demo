package com.example.flowableenginedemo;

public class SecurityConfig {

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
/*
  @Bean
  public CustomUserDetailsService customUserDetailsService() {
    return new CustomUserDetailsService();
  }

  @Bean
  public CustomReactiveAuthenticationManager customReactiveAuthenticationManager() {
    return new CustomReactiveAuthenticationManager();
  }
*/


/*
  @Bean
  public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
    return http.authorizeExchange(authorizeExchangeSpec -> {

          authorizeExchangeSpec.pathMatchers("/deploy/holiday").permitAll()
              .pathMatchers("/**").permitAll()
              .anyExchange().permitAll()
          ;
        })
        .csrf().disable()
        .authenticationManager(customReactiveAuthenticationManager())
        .build();
  }

*/
}
