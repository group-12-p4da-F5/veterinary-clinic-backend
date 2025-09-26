package org.factoriaf5.facade.encryptions_systems;

import org.factoriaf5.facade.decrypt.IDecoder;

public class Base64System implements IDecoder {

  @Override
  public String decode(String data) {
    byte[] decodedBytes = Base64.getDecoder().decode(data);
    String dataDecoded = new String(decodedBytes);
    return dataDecoded;
  }

}
