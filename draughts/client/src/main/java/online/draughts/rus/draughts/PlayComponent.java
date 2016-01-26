package online.draughts.rus.draughts;

import online.draughts.rus.shared.dto.MoveDto;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.11.15
 * Time: 20:23
 */
public interface PlayComponent {
  String takeScreenshot();

  void checkWinner();

  void toggleTurn(boolean turn);

  void doPlayerMove(MoveDto move);

  void addNotationStroke(Stroke strokeForNotation);

  void doSaveMove(MoveDto move);
}
