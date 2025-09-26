package org.factoriaf5.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class JpaUserDetailsService {

  @Value("${api-endpoint}")
  String endpoint;

  private JpaUserDetailsService jpaUserDetailsService;

}
