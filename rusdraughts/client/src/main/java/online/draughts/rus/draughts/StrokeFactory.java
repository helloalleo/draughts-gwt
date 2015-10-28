package online.draughts.rus.draughts;

import online.draughts.rus.shared.model.Move;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.10.15
 * Time: 16:49
 */
public class StrokeFactory {

  public static Stroke createStrokeFromMove(Move move) {
    return new Stroke().setFirst(move.isFirst())
        .setNumber(move.getNumber())
        .setMoveFlags(move.getMoveFlags())
        .setStartSquare(Square.fromString(move.getStartPos()))
        .setEndSquare(Square.fromString(move.getEndPos()))
        .setTakenSquare(Square.fromString(move.getTakenPos()));
  }
}
