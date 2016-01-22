package online.draughts.rus.client.application.common;

import com.gwtplatform.mvp.client.UiHandlers;
import online.draughts.rus.draughts.Stroke;
import online.draughts.rus.shared.dto.FriendDto;
import online.draughts.rus.shared.dto.MoveDto;
import online.draughts.rus.shared.dto.PlayerDto;

import java.util.List;
import java.util.Set;

interface PlayComponentUiHandlers extends UiHandlers {

  void startPlayWith(PlayerDto opponent);

  void refreshConnectionToServer();

  boolean isMyTurn();

  void proposeDraw();

  void playerSurrendered();

  void proposeCancelMove(Stroke lastMove);

  void saveFriend(FriendDto friend);

  void checkWinner();

  void toggleTurn(boolean turn);

  void doPlayerMove(MoveDto move);

  void writeToFriend(PlayerDto friend);

  List<PlayerDto> getSortedPlayerList(Set<Long> playerIds, List<PlayerDto> playerList);

  List<FriendDto> getSortedFriendList(Set<Long> playerIds, List<FriendDto> playerList);

  void didNotResponse();

  void stopTimers();

  String getTimeOnPlayStringMinutes();

  String getFisherTimeStringSecond();
}
