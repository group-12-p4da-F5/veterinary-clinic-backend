package org.factoriaf5.facade.decrypt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.Base64;
import java.util.Base64.Encoder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DecryptFacadeTest {

  @Autowired
  private DecryptFacade facade;

  @Test
  @DisplayName("Decode data with Base 64")
  void testDecode() {
    String type = "base64";
    String data = "password";

    Encoder encoder = Base64.getEncoder();
    byte[] bytes = encoder.encode(data.getBytes());
    String dataEncoded = new String(bytes);

    String decodedPassword = facade.decode(type, dataEncoded);

    assertThat(decodedPassword, is(equalTo(data)));
  }
}
