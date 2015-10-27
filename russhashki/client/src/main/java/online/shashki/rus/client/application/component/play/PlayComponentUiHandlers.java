package online.shashki.rus.client.application.component.play;

import com.gwtplatform.mvp.client.UiHandlers;
import online.shashki.rus.shared.model.Player;
import online.shashki.rus.shashki.Stroke;

interface PlayComponentUiHandlers extends UiHandlers {

  void startPlayWith(Player opponent);

  void refreshConnectionToServer();

  boolean isMyTurn();

  com.google.web.bindery.event.shared.EventBus getPlayEventBus();

  void proposeDraw();

  void playerSurrendered();

  void proposeCancelMove(Stroke lastMove);
}
