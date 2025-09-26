package org.factoriaf5.facade.encrypt;

public class EncryptFacade implements IEncryptFacade {

  private final IEncoder encoder;

  public EncryptFacade(IEncoder encoder) {
    this.encoder = encoder;
  }

  @Override
  public String encode(String type, String data) {
    String dataEncoded = "";

    if (type.equals("bcrypt"))
      dataEncoded = encoder.encode(data);

    return dataEncoded;
  }

}
