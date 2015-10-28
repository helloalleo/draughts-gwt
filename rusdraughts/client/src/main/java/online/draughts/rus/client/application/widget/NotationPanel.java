package online.draughts.rus.client.application.widget;


import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.web.bindery.event.shared.EventBus;
import online.draughts.rus.client.event.*;
import online.draughts.rus.client.util.DTLog;
import online.draughts.rus.draughts.Square;
import online.draughts.rus.draughts.Stroke;

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

//  private final DraughtsGinjector draughtsGinjector = DraughtsGinjector.INSTANCE;
  private static String notation;
  private boolean cancelBite;
  private int cancelCounter;

  public NotationPanel(EventBus eventBus) {
    eventBus.addHandler(NotationStrokeEvent.TYPE, new NotationStrokeEventHandler() {
      @Override
      public void onNotationStroke(NotationStrokeEvent event) {
        NotationPanel.this.appendMove(event.getStroke(),
            event.isOpponentStroke());
      }
    });
    eventBus.addHandler(NotationCancelStrokeEvent.TYPE, new NotationCancelStrokeEventHandler() {
      @Override
      public void onNotationCancelStroke(NotationCancelStrokeEvent event) {
        NotationPanel.this.cancelMove(event.getStroke());
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

  public void appendMove(Stroke stroke, boolean opponentMove) {
    notation = getElement().getInnerHTML();
    notation = notation.replaceAll(DIV_GARBAGE, "");

    String[] steps = notation.split(NOTATION_SEP);
    if (steps.length == 0) {
      notation = "";
    }
    DTLog.debug("MOVE in NOTATION " + stroke);
    // первый шаг. например, h4:f6:d4 - h4
    Square start = stroke.getStartSquare();
    DTLog.debug("FIRST STEP " + start.toString());

    if (stroke.isSimple()) {
      if (stroke.isFirst()) {
        notation += stroke.getNumber() + COUNT_SEP + stroke.toNotation(opponentMove);
      } else {
        notation += MOVE_SEP + stroke.toNotation(opponentMove) + NOTATION_SEP;
      }
    } else { // взята одна или более шашек
      DTLog.debug(stroke.isFirst() + " FIRST CONT BEAT");
      if (stroke.isStartBeat()) {
        if (stroke.isFirst()) {
          notation += stroke.getNumber() + COUNT_SEP + stroke.toNotation(opponentMove);
        } else {
          if (stroke.isContinueBeat()) {
            notation += MOVE_SEP + stroke.toNotation(opponentMove);
          } else {
            notation += MOVE_SEP + stroke.toNotation(opponentMove) + NOTATION_SEP;
          }
        }
      } else if (stroke.isStopBeat()) {
        if (stroke.isFirst()) {
          notation += BEAT_SEP + stroke.toNotationLastMove() + MOVE_SEP;
        } else {
          notation += BEAT_SEP + stroke.toNotationLastMove() + NOTATION_SEP;
        }
      } else if (stroke.isContinueBeat()) {
        notation += BEAT_SEP + stroke.toNotationLastMove();
      }
    }

    getElement().setInnerHTML(notation);
    DTLog.debug("Notation " + notation);
    pushScroll();
  }


  // код для прокрутки TextArea в конец
  public void pushScroll() {
    scrollToBottom();
  }

  public static String getNotation() {
    DTLog.debug("getNotation() " + notation);
    return notation;
  }

  public void cancelMove(Stroke stroke) {
    DTLog.debug(notation);
    notation = notation.replaceAll(DIV_GARBAGE, "");
    if (stroke.isSimple()) {
      if (stroke.isFirst()) {
        if (stroke.getNumber() != 0) {
          notation = notation.substring(0, notation.lastIndexOf(NOTATION_SEP)) + NOTATION_SEP;
        } else {
          notation = "";
        }
      } else {
        notation = notation.substring(0, notation.lastIndexOf(MOVE_SEP));
      }
    } else {
      if (stroke.isFirst()) {
        if (stroke.isStartBeat()) {
          notation = notation.substring(0, notation.lastIndexOf(NOTATION_SEP)) + NOTATION_SEP;
        } else {
          notation = notation.substring(0, notation.lastIndexOf(BEAT_SEP));
        }
      } else {
        if (stroke.isStartBeat()) {
          notation = notation.substring(0, notation.lastIndexOf(MOVE_SEP));
        } else {
          notation = notation.substring(0, notation.lastIndexOf(BEAT_SEP));
        }
      }
    }
    getElement().setInnerHTML(notation);
  }
}
