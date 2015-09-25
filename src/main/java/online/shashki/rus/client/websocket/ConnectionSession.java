package online.shashki.rus.client.websocket;

import com.google.inject.Singleton;
import online.shashki.rus.shared.model.Game;
import online.shashki.rus.shared.model.Shashist;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 23.09.15
 * Time: 20:22
 */
@Singleton
public class ConnectionSession {
  private Shashist player;
  private Shashist opponent;
  private Game game;
  private boolean connected;

  public Shashist getPlayer() {
    return player;
  }

  public ConnectionSession setPlayer(Shashist player) {
    this.player = player;
    return this;
  }

  public Shashist getOpponent() {
    return opponent;
  }

  public ConnectionSession setOpponent(Shashist opponent) {
    this.opponent = opponent;
    return this;
  }

  public Game getGame() {
    return game;
  }

  public ConnectionSession setGame(Game game) {
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
