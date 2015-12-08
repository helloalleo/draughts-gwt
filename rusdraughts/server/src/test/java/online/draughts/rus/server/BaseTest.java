package online.draughts.rus.server;

import online.draughts.rus.server.domain.Player;

import java.math.BigInteger;
import java.security.SecureRandom;

public class BaseTest {

  private SecureRandom random = new SecureRandom();

  public String randomString() {
    return new BigInteger(130, random).toString(32);
  }

  protected Player createPlayer() {
    Player player = new Player();
    player.setActive(false);
    player.setPlayerName(randomString());
    return player;
  }
}
