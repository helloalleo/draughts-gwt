package online.draughts.rus.client.application.security;

import com.google.inject.Singleton;
import online.draughts.rus.shared.model.Player;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 21.09.15
 * Time: 12:01
 */
@Singleton
public class CurrentSession {
  private Player player;

  public Boolean isLoggedIn() {
    return player != null && player.isLoggedIn();
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  public Player getPlayer() {
    return player;
  }
}
