package org.factoriaf5.config;

import org.factoriaf5.security.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  @Value("${api-endpoint}")
  String endpoint;

  private JpaUserDetailsService jpaUserDetailsService;
}
