package online.shashki.rus.shashki;

import online.shashki.rus.shared.model.Move;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.10.15
 * Time: 16:14
 */
public class MoveFactory {

  public static Move createMoveFromStroke(Stroke stroke) {
    return new Move().setFirst(stroke.isFirst())
        .setNumber(stroke.getNumber())
        .setMoveFlags(stroke.getMoveFlags())
        .setStartPos(stroke.getStartSquare().getPos())
        .setEndPos(stroke.getEndSquare().getPos())
        .setTakenPos(stroke.getTakenSquare().getPos());
  }
}
