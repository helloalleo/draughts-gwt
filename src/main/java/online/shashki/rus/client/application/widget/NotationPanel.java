package online.shashki.rus.client.application.widget;


import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.web.bindery.event.shared.EventBus;
import online.shashki.rus.client.event.*;
import online.shashki.rus.shashki.Square;
import online.shashki.rus.shared.dto.MoveDto;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 13.03.14
 * Time: 19:32
 */
public class NotationPanel extends ScrollPanel {
  private static final String NOTATION_SEP = "<br>";
  private static final String BEAT_SEP = ":";
  private static final String MOVE_SEP = " ";
  private static final String DIV_GARBAGE = "<div[\\s\\w\\d\";:=]*></div>";
  private static final String COUNT_SEP_REGEX = "\\. ";
  private static final String COUNT_SEP = ". ";
  private static final String NOTATION_WIDTH = "200px";

  private final EventBus eventBus = new SimpleEventBus();
//  private final ShashkiGinjector shashkiGinjector = ShashkiGinjector.INSTANCE;
  private static String notation;
  private boolean cancelBite;
  private int cancelCounter;

  public NotationPanel() {
//    this.eventBus = shashkiGinjector.getEventBus();

    // TODO: Not Compile
    eventBus.addHandler(NotationMoveEvent.TYPE, new NotationMoveEventHandler() {
      @Override
      public void onNotationMove(NotationMoveEvent event) {
        NotationPanel.this.appendMove(event.getMove());
      }
    });
    eventBus.addHandler(NotationCancelMoveEvent.TYPE, new NotationCancelMoveEventHandler() {
      @Override
      public void onNotationCancelMove(NotationCancelMoveEvent event) {
        NotationPanel.this.cancelMove(event.getMove());
      }
    });
    eventBus.addHandler(ClearNotationEvent.TYPE, new ClearNotationEventHandler() {
      @Override
      public void onClearNotation(ClearNotationEvent event) {
        notation = "";
        getElement().setInnerHTML("");
      }
    });

    Scheduler.get().scheduleFinally(new Scheduler.ScheduledCommand() {
      @Override
      public void execute() {
        setWidth(NOTATION_WIDTH);
      }
    });
  }

  public void appendMove(MoveDto move) {
    notation = getElement().getInnerHTML();
    notation = notation.replaceAll(DIV_GARBAGE, "");

    String[] steps = notation.split(NOTATION_SEP);
    if (steps.length == 0) {
      notation = "";
    }
    GWT.log("MOVE in NOTATION " + move);
    // первый шаг. например, h4:f6:d4 - h4
    Square start = move.getStartSquare();
    GWT.log("FIRST STEP " + start.toString());

    if (move.isSimple()) {
      if (move.isFirst()) {
        notation += move.getNumber() + COUNT_SEP + move.toNotation();
      } else {
        notation += MOVE_SEP + move.toNotation() + NOTATION_SEP;
      }
    } else { // взята одна или более шашек
      GWT.log(move.isFirst() + " FIRST CONT BEAT");
      if (move.isStartBeat()) {
        if (move.isFirst()) {
          notation += move.getNumber() + COUNT_SEP + move.toNotation();
        } else {
          if (move.isContinueBeat()) {
            notation += MOVE_SEP + move.toNotation();
          } else {
            notation += MOVE_SEP + move.toNotation() + NOTATION_SEP;
          }
        }
      } else if (move.isStopBeat()) {
        if (move.isFirst()) {
          notation += BEAT_SEP + move.toNotationLastMove() + MOVE_SEP;
        } else {
          notation += BEAT_SEP + move.toNotationLastMove() + NOTATION_SEP;
        }
      } else if (move.isContinueBeat()) {
        notation += BEAT_SEP + move.toNotationLastMove();
      }
    }

    getElement().setInnerHTML(notation);
    GWT.log("Notation " + notation);
    pushScroll();
  }


  // код для прокрутки TextArea в конец
  public void pushScroll() {
    scrollToBottom();
  }

  public static String getNotation() {
    return notation;
  }

  public void cancelMove(MoveDto move) {
    GWT.log(notation);
    notation = notation.replaceAll(DIV_GARBAGE, "");
    if (move.isSimple()) {
      if (move.isFirst()) {
        notation = notation.substring(0, notation.lastIndexOf(NOTATION_SEP)) + NOTATION_SEP;
      } else {
        notation = notation.substring(0, notation.lastIndexOf(MOVE_SEP));
      }
    } else {
      if (move.isFirst()) {
        if (move.isStartBeat()) {
          notation = notation.substring(0, notation.lastIndexOf(NOTATION_SEP)) + NOTATION_SEP;
        } else {
          notation = notation.substring(0, notation.lastIndexOf(BEAT_SEP));
        }
      } else {
        if (move.isStartBeat()) {
          notation = notation.substring(0, notation.lastIndexOf(MOVE_SEP));
        } else {
          notation = notation.substring(0, notation.lastIndexOf(BEAT_SEP));
        }
      }
    }
//    String[] notationArray = notation.split(NOTATION_SEP);
//    // ход. 2. h4:f6 e7:g5
//    String calcMove = notationArray[notationArray.length - 1];
//    GWT.log("MOVE " + calcMove);
//    String lastMove = calcMove.split(COUNT_SEP_REGEX)[1];
//    String[] lastMoveArray = lastMove.split(MOVE_SEP);
//    GWT.log("LAST MOVE " + lastMove);
//    if (lastMove.contains(BEAT_SEP) && !lastMove.contains(MOVE_SEP)) { // первый ход
//      GWT.log(notation + " - " + notation.lastIndexOf(BEAT_SEP));
//      if (lastMove.split(BEAT_SEP).length == 2) { // первый ход содержит побитые шашки и она одна
//        notation = notation.substring(0, notation.lastIndexOf(NOTATION_SEP)) + NOTATION_SEP;
//        stepCounter -= cancelCounter;
//        cancelCounter = 1;
//      } else { // побито несколько шашек
//        notation = notation.substring(0, notation.lastIndexOf(BEAT_SEP));
//        cancelBite = true;
//      }
//    } else { // второй ход
//      if (lastMoveArray.length == 2) {
//        // послед ход e7:g5
//        final String lastStep = lastMoveArray[1];
//        final String[] lastStepArray = lastStep.split(BEAT_SEP);
//        GWT.log(lastStep);
//        if (lastStep.contains(BEAT_SEP) && lastStep.split(BEAT_SEP).length != 2) { // последний ход содержит побитые
//          // шашки
//          notation = notation.substring(0, notation.lastIndexOf(BEAT_SEP));
//          cancelBite = true;
//        } else {
//          notation = notation.substring(0, notation.lastIndexOf(MOVE_SEP));
//          stepCounter -= cancelCounter;
//          cancelCounter = 1;
//        }
//      } else { // первый ход, где нет побитых шашек
//        notation = notation.substring(0, notation.lastIndexOf(NOTATION_SEP)) + NOTATION_SEP;
//        stepCounter -= cancelCounter;
//        cancelCounter = 1;
//      }
//    }
    getElement().setInnerHTML(notation);
  }
}
