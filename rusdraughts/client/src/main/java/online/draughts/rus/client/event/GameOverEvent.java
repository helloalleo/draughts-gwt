package online.draughts.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import online.draughts.rus.shared.model.Game;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 03.10.15
 * Time: 13:53
 */
public class GameOverEvent extends GwtEvent<GameOverEventHandler> {
  public static Type<GameOverEventHandler> TYPE = new Type<GameOverEventHandler>();
  private final Game game;
  private final Game.GameEnds gameEnd;
  private final AsyncCallback<Game> asyncCallback;

  public GameOverEvent(Game game, Game.GameEnds gameEnd, AsyncCallback<Game> asyncCallback) {
    this.game = game;
    this.gameEnd = gameEnd;
    this.asyncCallback = asyncCallback;
  }

  public Game getGame() {
    return game;
  }

  public Game.GameEnds getGameEnd() {
    return gameEnd;
  }

  public AsyncCallback<Game> getAsyncCallback() {
    return asyncCallback;
  }

  public Type<GameOverEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(GameOverEventHandler handler) {
    handler.onGameOver(this);
  }
}
