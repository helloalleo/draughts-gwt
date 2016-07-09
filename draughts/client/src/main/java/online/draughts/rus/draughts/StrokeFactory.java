package online.draughts.rus.draughts;

import com.google.gwt.user.client.Element;
import online.draughts.rus.client.application.widget.NotationPanel;
import online.draughts.rus.shared.dto.MoveDto;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.10.15
 * Time: 16:49
 */
public class StrokeFactory {

  public static Stroke createStrokeFromMove(MoveDto move) {
    return new Stroke()
        .setTitle(move.getTitle())
        .setComment(move.getComment())
        .setFirst(move.isFirst())
        .setOrder(move.getMoveOrder())
        .setNumber(move.getNumber())
        .setMoveFlags(move.getMoveFlags())
        .setStartSquare(Square.fromPosition(move.getMovingDraught().getRow(), move.getMovingDraught().getCol()))
        .setEndSquare(Square.fromPosition(move.getMovedDraught().getRow(), move.getMovedDraught().getCol()))
        .setTakenSquare(move.getTakenDraught() != null ?
            Square.fromPosition(move.getTakenDraught().getRow(), move.getTakenDraught().getCol()) : null)
        .setQueen(move.getMovedDraught().isQueen())
        .setWhite(move.getMovedDraught().isWhite());
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
    stroke.setOrder(order);
    stroke.setTitle(title);
    stroke.setComment(comment);
    if (simpleMove) {
      stroke.setOnSimpleMove();
    }
    if (startBeat) {
      stroke.setOnStartBeat();
    }
    if (continueBeat) {
      stroke.setOnContinueBeat();
    }
    if (stopBeat) {
      stroke.setOnStopBeat();
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
    if (!stroke.isSimple()) {
      captured = findTaken(startSquare, endSquare, prevSteps, back);
    }
    stroke.setFirst(first)
        .setNumber(number)
        .setStartSquare(startSquare)
        .setEndSquare(endSquare)
        .setTakenSquare(captured);
    return stroke;
  }

  /**
   * Функция поиска побитой шашки
   * @param firstStep начало хода бьющей шашки
   * @param secondStep конец хода бьющей шашки
   * @param prevSteps предыдущие ходы
   * @param back отматываем назад?
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
