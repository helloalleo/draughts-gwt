package online.draughts.rus.client.channel;

import com.google.inject.Singleton;
import online.draughts.rus.shared.dto.GameDto;
import online.draughts.rus.shared.dto.PlayerDto;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 23.09.15
 * Time: 20:22
 */
@Singleton
public class PlaySession {
  private PlayerDto player;
  private PlayerDto opponent;
  private GameDto game;
  private boolean connected;

  public PlayerDto getPlayer() {
    return player;
  }

  public PlaySession setPlayer(PlayerDto player) {
    this.player = player;
    return this;
  }

  public PlayerDto getOpponent() {
    return opponent;
  }

  public PlaySession setOpponent(PlayerDto opponent) {
    this.opponent = opponent;
    return this;
  }

  public GameDto getGame() {
    return game;
  }

  public PlaySession setGame(GameDto game) {
    this.game = game;
    return this;
  }

  public void setConnected(boolean connected) {
    this.connected = connected;
  }

  public boolean isConnected() {
    return connected;
  }

  public boolean isPlayerHasWhiteColor() {
    return connected
        && player != null
        && game != null
        && game.getPlayerWhite() != null
        && player.getId().equals(game.getPlayerWhite().getId());
  }
}
