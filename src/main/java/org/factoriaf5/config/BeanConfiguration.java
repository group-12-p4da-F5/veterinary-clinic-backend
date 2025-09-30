package org.factoriaf5.config;

import org.factoriaf5.encrypt.decrypt.DecryptFacade;
import org.factoriaf5.encrypt.decrypt.IDecryptFacade;
import org.factoriaf5.encrypt.encrypt.EncryptFacade;
import org.factoriaf5.encrypt.encrypt.IEncryptFacade;
import org.factoriaf5.encrypt.encryptions_systems.Base64System;
import org.factoriaf5.encrypt.encryptions_systems.BcryptSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfiguration {

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public Base64System base64System() {
    return new Base64System();
  }

  @Bean
  public IEncryptFacade bcryptSystem() {
    return new EncryptFacade(new BcryptSystem(passwordEncoder()));
  }

  @Bean
  public IDecryptFacade encryptFacade() {
    return new DecryptFacade(base64System());
  }
}
