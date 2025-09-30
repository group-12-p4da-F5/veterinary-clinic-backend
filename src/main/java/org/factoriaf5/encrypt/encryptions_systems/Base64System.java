package org.factoriaf5.encrypt.encryptions_systems;

import java.util.Base64;

import org.factoriaf5.encrypt.decrypt.IDecoder;

public class Base64System implements IDecoder {

  @Override
  public String decode(String data) {
    byte[] decodedBytes = Base64.getDecoder().decode(data);
    String dataDecoded = new String(decodedBytes);
    return dataDecoded;
  }

}
