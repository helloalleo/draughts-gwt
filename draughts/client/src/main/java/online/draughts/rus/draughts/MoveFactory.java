package online.draughts.rus.draughts;

import online.draughts.rus.shared.dto.DraughtDto;
import online.draughts.rus.shared.dto.MoveDto;

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
  public static MoveDto createMoveFromStroke(Stroke stroke) {
    MoveDto move = new MoveDto().setFirst(stroke.isFirst())
        .setNumber(stroke.getNumber())
        .setMoveFlags(stroke.getMoveFlags())
        .setMovingDraught(new DraughtDto(stroke.getStartSquare().getRow(), stroke.getStartSquare().getCol(),
            stroke.isWhite(), stroke.isQueen()))
        .setMovedDraught(new DraughtDto(stroke.getEndSquare().getRow(), stroke.getEndSquare().getCol(),
            stroke.isWhite(), stroke.isQueen()));

    // При преобразовании не всегда есть захваченная шашка, поэтому делаем проверка
    if (stroke.getTakenSquare() != null) {
      move.setTakenDraught(new DraughtDto(stroke.getTakenSquare().getRow(), stroke.getTakenSquare().getCol(),
          stroke.isWhite(), stroke.isQueen()));
    }
    return move;
  }
}
