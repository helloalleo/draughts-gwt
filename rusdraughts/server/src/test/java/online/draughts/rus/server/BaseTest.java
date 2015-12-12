package online.draughts.rus.server;

import online.draughts.rus.server.domain.Player;
import online.draughts.rus.server.objectify.EmbeddedDataStore;
import org.junit.Rule;

import java.math.BigInteger;
import java.security.SecureRandom;

public class BaseTest {

  @Rule
  public EmbeddedDataStore store = new EmbeddedDataStore();

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
