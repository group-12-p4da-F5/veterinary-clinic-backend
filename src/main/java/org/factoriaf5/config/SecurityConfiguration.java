package org.factoriaf5.config;

import org.factoriaf5.security.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  @Value("${api-endpoint}")
  String endpoint;

  private JpaUserDetailsService jpaUserDetailsService;

  public SecurityConfiguration(JpaUserDetailsService jpaUserDetailsService) {
    this.jpaUserDetailsService = jpaUserDetailsService;
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(withDefaults())
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/h2-console/**")
            .disable())
        .headers(header -> header
            .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
        .formLogin(form -> form
            .loginProcessingUrl(endpoint + "/login")
            .successHandler((request, response, authentication) -> {
              response.setStatus(200);
            }))
        .logout(out -> out
            .logoutUrl(endpoint + "/logout")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID"))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("h2-console/**").permitAll()
            .requestMatchers("/public").permitAll()
            .requestMatchers(HttpMethod.POST, endpoint + "/register").permitAll()
            .requestMatchers(endpoint + "/login").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.GET, endpoint + "/private").hasRole("ADMIN")
            .anyRequest().authenticated())
        .userDetailsService(jpaUserDetailsService)
        .httpBasic(withDefaults())
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

    return http.build();
  }
}
