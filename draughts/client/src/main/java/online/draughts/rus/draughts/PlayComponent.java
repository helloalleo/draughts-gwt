package online.draughts.rus.draughts;

import online.draughts.rus.shared.dto.MoveDto;
import online.draughts.rus.shared.dto.PlayerDto;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.11.15
 * Time: 20:23
 */
public interface PlayComponent {
  void checkWinner();

  void addNotationStroke(Stroke strokeForNotation);

  void toggleTurn(boolean turn);

  void doPlayerMove(MoveDto move);

  PlayerDto getPlayer();

  PlayerDto getOpponent();

  String takeScreenshot();
}
