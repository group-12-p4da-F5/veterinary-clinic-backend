package org.factoriaf5.encrypt.decrypt;

public class DecryptFacade implements IDecryptFacade {

  private final IDecoder decoder;

  public DecryptFacade(IDecoder decoder) {
    this.decoder = decoder;
  }

  @Override
  public String decode(String type, String data) {
    String dataDecoded = "";

    if (type == "base64")
      dataDecoded = decoder.decode(data);

    return dataDecoded;
  }

}
