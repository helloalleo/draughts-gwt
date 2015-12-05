package online.draughts.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import online.draughts.rus.shared.dto.GameDto;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 03.10.15
 * Time: 13:53
 */
public class GameOverEvent extends GwtEvent<GameOverEventHandler> {
  public static Type<GameOverEventHandler> TYPE = new Type<GameOverEventHandler>();
  private final GameDto game;
  private final GameDto.GameEnds gameEnd;
  private final AsyncCallback<GameDto> asyncCallback;

  public GameOverEvent(GameDto game, GameDto.GameEnds gameEnd, AsyncCallback<GameDto> asyncCallback) {
    this.game = game;
    this.gameEnd = gameEnd;
    this.asyncCallback = asyncCallback;
  }

  public GameDto getGame() {
    return game;
  }

  public GameDto.GameEnds getGameEnd() {
    return gameEnd;
  }

  public AsyncCallback<GameDto> getAsyncCallback() {
    return asyncCallback;
  }

  public Type<GameOverEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(GameOverEventHandler handler) {
    handler.onGameOver(this);
  }
}
