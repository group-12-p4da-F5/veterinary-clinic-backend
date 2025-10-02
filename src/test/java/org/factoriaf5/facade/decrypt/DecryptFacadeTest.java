package org.factoriaf5.facade.decrypt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.util.Base64;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DecryptFacadeTest {

  @Mock
  private IDecoder decoder;

  @InjectMocks
  private DecryptFacade facade;

  @Test
  @DisplayName("Decode data with Base 64")
  void testDecode() {
    String data = "password";
    String encoded = Base64.getEncoder().encodeToString(data.getBytes());

    when(decoder.decode(encoded)).thenReturn(data);

    String decoded = facade.decode("base64", encoded);

    assertThat(decoded, is(equalTo(data)));
  }
}
