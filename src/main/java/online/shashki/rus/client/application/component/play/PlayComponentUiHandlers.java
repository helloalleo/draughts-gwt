package online.shashki.rus.client.application.component.play;

import com.gwtplatform.mvp.client.UiHandlers;
import online.shashki.rus.shared.model.Move;
import online.shashki.rus.shared.model.Shashist;

interface PlayComponentUiHandlers extends UiHandlers {
  void startPlayWith(Shashist opponent);

  void refreshConnectionToServer();

  boolean isMyTurn();

  com.google.web.bindery.event.shared.EventBus getPlayEventBus();

  void proposeDraw();

  void playerSurrendered();

  void proposeCancelMove(Move lastMove);
}
