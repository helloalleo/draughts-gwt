package online.shashki.rus.shashki;

import online.shashki.rus.shared.model.Move;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.10.15
 * Time: 16:14
 */
public class MoveFactory {

  /**
   * Создание "перемещения" для сервера из "хода" для клиента
   * @param stroke ход на клиенте
   * @return перемещение для сервера
   */
  public static Move createMoveFromStroke(Stroke stroke) {
    Move move = new Move().setFirst(stroke.isFirst())
        .setNumber(stroke.getNumber())
        .setMoveFlags(stroke.getMoveFlags())
        .setStartPos(stroke.getStartSquare().getPos())
        .setEndPos(stroke.getEndSquare().getPos());

    // При преобразовании не всегда есть захваченная шашка, поэтому делаем проверка
    if (stroke.getTakenSquare() != null) {
      move.setTakenPos(stroke.getTakenSquare().getPos());
    }
    return move;
  }
}
