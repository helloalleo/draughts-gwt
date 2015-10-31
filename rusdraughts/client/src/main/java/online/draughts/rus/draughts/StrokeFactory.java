package online.draughts.rus.draughts;

import com.google.gwt.user.client.Element;
import online.draughts.rus.client.application.widget.NotationPanel;
import online.draughts.rus.shared.model.Move;
import online.draughts.rus.shared.util.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.10.15
 * Time: 16:49
 */
public class StrokeFactory {

  public static Stroke createStrokeFromMove(Move move) {
    return new Stroke()
        .setFirst(move.isFirst())
        .setNumber(move.getNumber())
        .setMoveFlags(move.getMoveFlags())
        .setStartSquare(Square.fromString(move.getStartPos()))
        .setEndSquare(Square.fromString(move.getEndPos()))
        .setTakenSquare(Square.fromString(move.getTakenPos()));
  }

  public static Stroke createStrokeFromNotationHtml(Element outerNotation, String step, boolean back) {
    if (outerNotation == null) {
      return null;
    }
    Stroke stroke = new Stroke();
    final Boolean first = Boolean.valueOf(outerNotation.getAttribute(NotationPanel.FIRST_ATTR));
    final Integer number = Integer.valueOf(outerNotation.getAttribute(NotationPanel.DATA_NUMBER));
    final Boolean simpleMove = Boolean.valueOf(outerNotation.getAttribute(NotationPanel.SIMPLE_MOVE_TAG));
    final Boolean startBeat = Boolean.valueOf(outerNotation.getAttribute(NotationPanel.START_BEAT));
    final Boolean continueBeat = Boolean.valueOf(outerNotation.getAttribute(NotationPanel.CONTINUE_BEAT));
    final Boolean stopBeat = Boolean.valueOf(outerNotation.getAttribute(NotationPanel.STOP_BEAT));
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
    } else if (StringUtils.isNotEmpty(step)) {
      pos[0] = step.substring(step.indexOf(Stroke.BEAT_MOVE_SEP) + 1);
      pos[1] = innerHTML;
    } else {
      return null;
    }
    String startPos = pos[0];
    String endPos = pos[1];
    final Square startSquare = Square.fromNotation(startPos);
    final Square endSquare = Square.fromNotation(endPos);
    Square captured = null;
    if (!stroke.isSimple()) {
      captured = findCaptured(startSquare, endSquare, back);
    }
    stroke.setFirst(first)
        .setNumber(number)
        .setStartSquare(startSquare)
        .setEndSquare(endSquare)
        .setTakenSquare(captured);
    return stroke;
  }

  private static Square findCaptured(Square firstStep, Square secondStep, boolean back) {
    for (int row = 0; row < BoardBackgroundLayer.ROWS; row++) {
      for (int col = 0; col < BoardBackgroundLayer.COLS; col++) {
        Square current;
        current = Square.getFromPos(row, col);
        if (null != current && (null != current.getOccupant() || back) && current.isBetween(firstStep, secondStep)
            && current.isOnLine(firstStep)) {
          return current;
        }
      }
    }
    return null;
  }
}
