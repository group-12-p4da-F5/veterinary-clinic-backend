package org.factoriaf5.facade.encrypt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class EncryptFacadeTest {

  // Implementación de prueba de IEncoder usando BCrypt
  private final IEncoder encoder = new IEncoder() {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String encode(String data) {
      return passwordEncoder.encode(data);
    }
  };

  private final EncryptFacade facade = new EncryptFacade(encoder);

  @Test
  @DisplayName("Encode data with bcrypt")
  void testEncrypt() {
    String data = "password";
    String passwordEncoded = facade.encode("bcrypt", data);

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    assertThat(passwordEncoded, not(data));

    assertThat(passwordEncoder.matches(data, passwordEncoded), is(true));
  }
}