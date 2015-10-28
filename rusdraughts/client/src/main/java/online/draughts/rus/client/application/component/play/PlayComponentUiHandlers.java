package online.draughts.rus.client.application.component.play;

import com.gwtplatform.mvp.client.UiHandlers;
import online.draughts.rus.shared.model.Friend;
import online.draughts.rus.shared.model.Player;
import online.draughts.rus.draughts.Stroke;

interface PlayComponentUiHandlers extends UiHandlers {

  void startPlayWith(Player opponent);

  void refreshConnectionToServer();

  boolean isMyTurn();

  com.google.web.bindery.event.shared.EventBus getPlayEventBus();

  void proposeDraw();

  void playerSurrendered();

  void proposeCancelMove(Stroke lastMove);

  void saveFriend(Friend friend);

  boolean isPlaying();
}
