package online.draughts.rus.client.application.play;

import com.gwtplatform.mvp.client.UiHandlers;
import online.draughts.rus.shared.model.Friend;
import online.draughts.rus.shared.model.Move;
import online.draughts.rus.shared.model.Player;
import online.draughts.rus.draughts.Stroke;

interface PlayComponentUiHandlers extends UiHandlers {

  void startPlayWith(Player opponent);

  void refreshConnectionToServer();

  boolean isMyTurn();

  void proposeDraw();

  void playerSurrendered();

  void proposeCancelMove(Stroke lastMove);

  void saveFriend(Friend friend);

  void checkWinner();

  void addNotationStroke(Stroke strokeForNotation);

  void toggleTurn(boolean turn);

  void doPlayerMove(Move move);
}
