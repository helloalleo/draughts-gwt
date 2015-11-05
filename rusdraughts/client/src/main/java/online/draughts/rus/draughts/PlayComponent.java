package online.draughts.rus.draughts;

import online.draughts.rus.shared.model.Move;

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

  void doPlayerMove(Move move);
}
