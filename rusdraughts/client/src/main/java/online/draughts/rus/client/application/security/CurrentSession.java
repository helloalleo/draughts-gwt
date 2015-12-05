package online.draughts.rus.client.application.security;

import com.google.inject.Singleton;
import online.draughts.rus.shared.dto.PlayerDto;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 21.09.15
 * Time: 12:01
 */
@Singleton
public class CurrentSession {
  private PlayerDto player;

  public Boolean isLoggedIn() {
    return player != null && player.isLoggedIn();
  }

  public void setPlayer(PlayerDto player) {
    this.player = player;
  }

  public PlayerDto getPlayer() {
    return player;
  }
}
