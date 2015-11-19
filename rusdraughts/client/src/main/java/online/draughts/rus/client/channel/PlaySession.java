package online.draughts.rus.client.channel;

import com.google.inject.Singleton;
import online.draughts.rus.shared.model.Game;
import online.draughts.rus.shared.model.Player;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 23.09.15
 * Time: 20:22
 */
@Singleton
public class PlaySession {
  private Player player;
  private Player opponent;
  private Game game;
  private boolean connected;

  public Player getPlayer() {
    return player;
  }

  public PlaySession setPlayer(Player player) {
    this.player = player;
    return this;
  }

  public Player getOpponent() {
    return opponent;
  }

  public PlaySession setOpponent(Player opponent) {
    this.opponent = opponent;
    return this;
  }

  public Game getGame() {
    return game;
  }

  public PlaySession setGame(Game game) {
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
