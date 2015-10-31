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
  public static final String NOTATION_SEP = "<br>";
  public static final String BEAT_SEP = ":";
  public static final String MOVE_SEP = " ";
  public static final String COUNT_SEP = ". ";
  private static final String NOTATION_WIDTH = "200px";
  public static final String SIMPLE_MOVE_TAG = "data-simple";
  public static final String STOP_BEAT = "data-stopbeat";
  public static final String CONTINUE_BEAT = "data-continuebeat";
  public static final String START_BEAT = "data-startbeat";
  public static final String DATA_ID_ATTR = "data-id";
  public static final String FIRST_ATTR = "data-first";
  public static final String DATA_NUMBER = "data-number";

  private static StringBuilder notation = new StringBuilder();
  private int order = 0;

  public NotationPanel(EventBus eventBus) {
    eventBus.addHandler(NotationStrokeEvent.TYPE, new NotationStrokeEventHandler() {
      @Override
      public void onNotationStroke(NotationStrokeEvent event) {
        NotationPanel.this.appendMove(event.getStroke(), event.isOpponentStroke());
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
        cleanNotationPanel();
      }
    });

    Scheduler.get().scheduleFinally(new Scheduler.ScheduledCommand() {
      @Override
      public void execute() {
        setWidth(NOTATION_WIDTH);
      }
    });
  }

  public static String getNotation() {
    return notation.toString();
  }

  public void appendMove(Stroke stroke, boolean opponentStroke) {
    DTLog.debug("MOVE in NOTATION " + stroke);
    // первый шаг. например, h4:f6:d4 - h4
    Square start = stroke.getStartSquare();
    DTLog.debug("FIRST STEP " + start.toString());
    final boolean first = stroke.isFirst();
    if (stroke.isSimple()) {
      if (first) {
        notation.append(stroke.getNumber())
            .append(COUNT_SEP)
            .append(wrapSimpleStroke(stroke, true));
      } else {
        notation.append(MOVE_SEP)
            .append(wrapSimpleStroke(stroke, false))
            .append(NOTATION_SEP);
      }
    } else { // взята одна или более шашек
      final boolean startBeat = stroke.isStartBeat();
      final boolean continueBeat = stroke.isContinueBeat();
      final boolean stopBeat = stroke.isStopBeat();
      if (startBeat) {
        if (first) {
          notation.append(stroke.getNumber())
              .append(COUNT_SEP)
              .append(wrapBeatStroke(stroke, true, true, continueBeat, stopBeat));
        } else {
          if (continueBeat) {
            notation.append(MOVE_SEP)
                .append(wrapBeatStroke(stroke, false, true, true, stopBeat));
          } else {
            notation.append(MOVE_SEP)
                .append(wrapBeatStroke(stroke, false, true, false, stopBeat))
                .append(NOTATION_SEP);
          }
        }
      } else if (stopBeat) {
        if (first) {
          notation.append(BEAT_SEP)
              .append(wrapBeatStroke(stroke, true, false, continueBeat, true))
              .append(MOVE_SEP);
        } else {
          notation.append(BEAT_SEP)
              .append(wrapBeatStroke(stroke, false, false, continueBeat, true))
              .append(NOTATION_SEP);
        }
      } else if (continueBeat) {
        notation.append(BEAT_SEP)
            .append(wrapBeatStroke(stroke, first, false, true, false));
      }
    }
    order++;

    getElement().setInnerHTML(notation.toString());
    pushScroll();
  }

  // код для прокрутки TextArea в конец
  public void pushScroll() {
    scrollToBottom();
  }

  public void cancelMove(Stroke stroke) {
    if (stroke.isSimple()) {
      if (stroke.isFirst()) {
        if (stroke.getNumber() != 1) { // при первом ходе из нотации нужно удалить див
          notation = new StringBuilder(notation.substring(0, notation.lastIndexOf(NOTATION_SEP)) + NOTATION_SEP);
        } else {
          cleanNotationPanel();
        }
      } else {
        notation = new StringBuilder(notation.substring(0, notation.lastIndexOf(MOVE_SEP)));
      }
    } else {
      if (stroke.isFirst()) {
        if (stroke.isStartBeat()) {
          notation = new StringBuilder(notation.substring(0, notation.lastIndexOf(NOTATION_SEP)) + NOTATION_SEP);
        } else {
          notation = new StringBuilder(notation.substring(0, notation.lastIndexOf(BEAT_SEP)));
        }
      } else {
        if (stroke.isStartBeat()) {
          notation = new StringBuilder(notation.substring(0, notation.lastIndexOf(MOVE_SEP)));
        } else {
          notation = new StringBuilder(notation.substring(0, notation.lastIndexOf(BEAT_SEP)));
        }
      }
    }
    order--;
    getElement().setInnerHTML(notation.toString());
  }

  private void cleanNotationPanel() {
    notation = new StringBuilder();
    getElement().setInnerHTML("");
    order = 0;
  }

  private String wrapBeatStroke(Stroke stroke, boolean first, boolean startBeat, boolean continueBeat, boolean stopBeat) {
    return wrapStroke(stroke, first, false, startBeat, continueBeat, stopBeat);
  }

  private String wrapSimpleStroke(Stroke stroke, boolean first) {
    return wrapStroke(stroke, first, true, false, false, false);
  }

  private String wrapStroke(Stroke stroke, boolean first, boolean simple, boolean startBeat, boolean continueBeat, boolean stopBeat) {
    if (simple) {
      return "<span id='" + order + "' "
          + "data-number='" + stroke.getNumber() + "' "
          + "data-simple='true' "
          + "data-first='" + first + "'"
          + ">"
          + stroke.toNotation()
          + "</span>";
    } else {
      return "<span id='" + order + "' "
          + "data-number='" + stroke.getNumber() + "' "
          + "data-simple='false' "
          + "data-first='" + first + "' "
          + "data-startbeat='" + startBeat + "' "
          + "data-continuebeat='" + continueBeat + "' "
          + "data-stopbeat='" + stopBeat + "'"
          + ">"
          + (stroke.isStartBeat() ? stroke.toNotation() : stroke.toNotationLastMove())
          + "</span>";
    }
  }
}
