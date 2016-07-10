package online.draughts.rus.draughts;

import com.google.gwt.user.client.Element;
import online.draughts.rus.client.application.widget.NotationPanel;
import online.draughts.rus.shared.dto.MoveDto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.10.15
 * Time: 16:49
 */
public class StrokeFactory {

  public static Stroke createStrokeFromMove(MoveDto move) {
    return createStroke(move.isFirst(), move.getNumber(),move.getMoveOrder(),
        Square.fromPosition(move.getMovingDraught().getRow(), move.getMovingDraught().getCol()),
        Square.fromPosition(move.getMovedDraught().getRow(), move.getMovedDraught().getCol()),
        move.getTakenDraught() != null ?
            Square.fromPosition(move.getTakenDraught().getRow(), move.getTakenDraught().getCol()) : null,
        move.getMoveFlags(),
        move.getTitle(), move.getComment(),
        move.getMovedDraught().isQueen(),
        move.getMovedDraught().isWhite(),
        null, null);
  }

  private static Stroke createStroke(boolean first, int number, int order,
                                     Square startSquare, Square endSquare, Square captured,
                                     Set<MoveDto.MoveFlag> moveFlags,
                                     String title, String comment,
                                     boolean queen, boolean white, Stroke prevStroke, Stroke nextStroke) {
    Stroke stroke = new Stroke().setFirst(first)
        .setNumber(number)
        .setOrder(order)
        .setStartSquare(startSquare)
        .setEndSquare(endSquare)
        .setTakenSquare(captured)
        .setMoveFlags(moveFlags)
        .setTitle(title)
        .setComment(comment)
        .setPrevStroke(prevStroke)
        .setNextStroke(nextStroke);
    return stroke;
  }

  /**
   * Создаёт ход из переданного html элемента хода нотации
   *
   * @param outerNotation html нотации
   * @param prevSteps     предыдущий ход
   * @param back          ход назад?
   * @return объект хода
   */
  @SuppressWarnings("deprecation")
  public static Stroke createStrokeFromNotationHtml(Element outerNotation, List<String> prevSteps, boolean back) {
    Stroke stroke = new Stroke();
    final Boolean first = Boolean.valueOf(outerNotation.getAttribute(NotationPanel.DATA_FIRST_ATTR));
    final Integer number = Integer.valueOf(outerNotation.getAttribute(NotationPanel.DATA_NUMBER_ATTR));
    final Integer order = Integer.valueOf(outerNotation.getAttribute(NotationPanel.DATA_ORDER_ATTR));
    final Boolean simpleMove = Boolean.valueOf(outerNotation.getAttribute(NotationPanel.DATA_SIMPLE_ATTR));
    final Boolean startBeat = Boolean.valueOf(outerNotation.getAttribute(NotationPanel.DATA_START_BEAT_ATTR));
    final Boolean continueBeat = Boolean.valueOf(outerNotation.getAttribute(NotationPanel.DATA_CONTINUE_BEAT_ATTR));
    final Boolean stopBeat = Boolean.valueOf(outerNotation.getAttribute(NotationPanel.DATA_STOP_BEAT_ATTR));
    final String title = outerNotation.getAttribute(NotationPanel.DATA_TITLE_ATTR);
    final String comment = outerNotation.getAttribute(NotationPanel.DATA_COMMENT_ATTR);
    Set<MoveDto.MoveFlag> moveFlags = new HashSet<>();
    if (simpleMove) {
      moveFlags.add(MoveDto.MoveFlag.SIMPLE_MOVE);
    }
    if (startBeat) {
      moveFlags.add(MoveDto.MoveFlag.START_BEAT);
    }
    if (continueBeat) {
      moveFlags.add(MoveDto.MoveFlag.CONTINUE_BEAT);
    }
    if (stopBeat) {
      moveFlags.add(MoveDto.MoveFlag.STOP_BEAT);
    }

    final String innerHTML = outerNotation.getInnerHTML();
    String[] pos = new String[2];
    if (simpleMove || startBeat) {
      pos = innerHTML.split(simpleMove ? Stroke.SIMPLE_MOVE_SEP : Stroke.BEAT_MOVE_SEP);
    } else if (!prevSteps.isEmpty()) {
      String prevStep = prevSteps.get(prevSteps.size() - 1);
      pos[0] = prevStep.substring(prevStep.indexOf(Stroke.BEAT_MOVE_SEP) + 1);
      pos[1] = innerHTML;
    } else {
      throw new RuntimeException("");
    }
    String startPos = pos[0];
    String endPos = pos[1];
    final Square startSquare = Square.fromNotation(startPos);
    final Square endSquare = Square.fromNotation(endPos);
    Square captured = null;
    if (!simpleMove) {
      captured = findTaken(startSquare, endSquare, prevSteps, back);
    }
    return createStroke(first, number, order,
        startSquare, endSquare, captured,
        moveFlags,
        title, comment,
        false, false, stroke.getPrevStroke(), stroke.getNextStroke());
  }

  public static Stroke cloneStroke(Stroke stroke) {
    return createStroke(stroke.isFirst(), stroke.getNumber(), stroke.getOrder(),
        stroke.getStartSquare(), stroke.getEndSquare(), stroke.getTakenSquare(),
        stroke.getMoveFlags(),
        stroke.getTitle(), stroke.getComment(),
        stroke.isQueen(), stroke.isWhite(),
        stroke.getPrevStroke(), stroke.getNextStroke());
  }

  /**
   * Функция поиска побитой шашки
   *
   * @param firstStep  начало хода бьющей шашки
   * @param secondStep конец хода бьющей шашки
   * @param prevSteps  предыдущие ходы
   * @param back       отматываем назад?
   * @return захваченнная шашка
   */
  private static Square findTaken(Square firstStep, Square secondStep, List<String> prevSteps, boolean back) {
    // для поиска побитой шашки комбинируем два способа.
    // 1. Проходим вверх по предыдущим ходам и ищем совершенный ход который оказался между началом и концом бьющей
    // шашки
    for (int i = prevSteps.size() - 1; i >= 0; i--) {
      String prevStep = prevSteps.get(i);
      Square prevSecond = Square.fromNotation(getPrevStep(prevStep, false));
      if (null != prevSecond && null != firstStep && null != secondStep
          && firstStep.isOnLine(prevSecond) && secondStep.isOnLine(prevSecond)
          && prevSecond.isBetween(firstStep, secondStep)) {
        String prevStepParts = getPrevStep(prevStep, false);
        return Square.fromNotation(prevStepParts);
      }
    }
    // 2. Если не нашли побитую шашку ищем, то значит она ещё не ходила, ищем полным перебором
    for (int row = 0; row < BoardBackgroundLayer.ROWS; row++) {
      for (int col = 0; col < BoardBackgroundLayer.COLS; col++) {
        Square square = Square.fromPosition(row, col);
        if (null != square && (null != square.getOccupant() || back) && square.isBetween(firstStep, secondStep)
            && square.isOnLine(firstStep)) {
          return square;
        }
      }
    }
    throw new RuntimeException("Captured draught not found");
  }

  private static String getPrevStep(String prevStep, boolean first) {
    String prevStepParts;
    int order = first ? 0 : 1;
    if (prevStep.contains(Stroke.SIMPLE_MOVE_SEP)) {
      prevStepParts = prevStep.split(Stroke.SIMPLE_MOVE_SEP)[order];
    } else if (prevStep.contains(Stroke.BEAT_MOVE_SEP)) {
      prevStepParts = prevStep.split(Stroke.BEAT_MOVE_SEP)[order];
    } else {
      prevStepParts = prevStep;
    }
    return prevStepParts;
  }
}
