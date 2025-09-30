package org.factoriaf5.encrypt.encryptions_systems;

import org.factoriaf5.encrypt.encrypt.IEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BcryptSystem implements IEncoder {

  PasswordEncoder encoder;

  public BcryptSystem(PasswordEncoder encoder) {
    this.encoder = encoder;
  }

  @Override
  public String encode(String data) {
    String dataEncoded = encoder.encode(data);
    return dataEncoded;
  }

}
