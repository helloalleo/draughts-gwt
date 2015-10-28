package online.draughts.rus.server;

import java.math.BigInteger;
import java.security.SecureRandom;

public class BaseTest {

  private SecureRandom random = new SecureRandom();

  public String randomString() {
    return new BigInteger(130, random).toString(32);
  }
}
