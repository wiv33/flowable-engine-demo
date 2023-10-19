package com.example.flowableenginedemo;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

public class CustomReactiveAuthenticationManager implements AuthenticationManager {

  private final CustomUserDetailsService customUserDetailsService;
  public CustomReactiveAuthenticationManager(CustomUserDetailsService customUserDetailsService) {
    this.customUserDetailsService = customUserDetailsService;
  }

  @Override
  public Authentication authenticate(Authentication authentication) {
    String username = authentication.getName();
    CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(username);
    return (Authentication) customUserDetails;
  }
}
