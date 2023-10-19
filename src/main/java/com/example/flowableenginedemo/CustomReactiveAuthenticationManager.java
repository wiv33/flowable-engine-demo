/*
package com.example.flowableenginedemo;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

public class CustomReactiveAuthenticationManager implements ReactiveAuthenticationManager {

  @Override
  public Mono<Authentication> authenticate(Authentication authentication) {
    if ("user".equals(authentication.getName()) && "1234".equals(
        authentication.getCredentials())) {
      return Mono.just(new UsernamePasswordAuthenticationToken(authentication.getName(),
          authentication.getCredentials()));
    }
    return Mono.empty();
  }
}
*/
