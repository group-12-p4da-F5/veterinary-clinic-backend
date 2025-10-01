package org.factoriaf5.facade.encrypt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class EncryptFacadeTest {

  @Autowired
  private IEncryptFacade facade;

  @Test
  @DisplayName("Encode data with bcrypt")
  void testEncrypt() {
    String type = "bcrypt";
    String data = "password";
    PasswordEncoder encoder = new BCryptPasswordEncoder();

    String passwordEncoded = facade.encode(type, data);

    assertThat(passwordEncoded, not(data));
    assertThat(encoder.matches(data, passwordEncoded), not(false));
  }
}
