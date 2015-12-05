package online.draughts.rus.client.application.play;

import com.gwtplatform.mvp.client.UiHandlers;
import online.draughts.rus.draughts.Stroke;
import online.draughts.rus.shared.dto.FriendDto;
import online.draughts.rus.shared.dto.MoveDto;
import online.draughts.rus.shared.dto.PlayerDto;

interface PlayComponentUiHandlers extends UiHandlers {

  void startPlayWith(PlayerDto opponent);

  void refreshConnectionToServer();

  boolean isMyTurn();

  void proposeDraw();

  void playerSurrendered();

  void proposeCancelMove(Stroke lastMove);

  void saveFriend(FriendDto friend);

  void checkWinner();

  void addNotationStroke(Stroke strokeForNotation);

  void toggleTurn(boolean turn);

  void doPlayerMove(MoveDto move);

  void writeToFriend(PlayerDto friend);
}
