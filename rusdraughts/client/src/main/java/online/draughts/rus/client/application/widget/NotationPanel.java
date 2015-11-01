package online.draughts.rus.client.application.widget;


import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.web.bindery.event.shared.EventBus;
import online.draughts.rus.client.event.*;
import online.draughts.rus.client.util.DTLog;
import online.draughts.rus.draughts.Square;
import online.draughts.rus.draughts.Stroke;

import static online.draughts.rus.client.util.Utils.format;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 13.03.14
 * Time: 19:32
 */
public class NotationPanel extends ScrollPanel {
  public static final String NOTATION_SEP_TAG = "br";
  public static final String NOTATION_SEP = "<" + NOTATION_SEP_TAG + ">";
  public static final String BEAT_SEP = ":";
  public static final String MOVE_SEP = " ";
  public static final String COUNT_SEP = ". ";
  private static final String NOTATION_WIDTH = "200px";
  public static final String DATA_SIMPLE_ATTR = "data-simple";
  public static final String DATA_STOP_BEAT_ATTR = "data-stopbeat";
  public static final String DATA_CONTINUE_BEAT_ATTR = "data-continuebeat";
  public static final String DATA_START_BEAT_ATTR = "data-startbeat";
  public static final String DATA_ID_ATTR = "data-id";
  public static final String DATA_ORDER_ATTR = "data-order";
  public static final String DATA_FIRST_ATTR = "data-first";
  public static final String DATA_NUMBER_ATTR = "data-number";
  public static final String DATA_COMMENT_ATTR = "data-comment";
  public static final String DATA_TITLE_ATTR = "data-title";
  public static final String COMMENT_SEP = "<br>";
  public static final String NOTATION_TAG = "span";

  private static StringBuilder notation = new StringBuilder();
  private static int order = 0;

  public NotationPanel(EventBus eventBus) {
    eventBus.addHandler(NotationStrokeEvent.TYPE, new NotationStrokeEventHandler() {
      @Override
      public void onNotationStroke(NotationStrokeEvent event) {
        NotationPanel.this.appendMove(event.getStroke());
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

  public void appendMove(Stroke stroke) {
    DTLog.debug("MOVE in NOTATION " + stroke);
    // первый шаг. например, h4:f6:d4 - h4
    Square start = stroke.getStartSquare();
    DTLog.debug("FIRST STEP " + start.toString());
    final boolean first = stroke.isFirst();
    if (stroke.isSimple()) {
      if (first) {
        notation.append(stroke.getNumber())
            .append(COUNT_SEP)
            .append(wrapStroke(stroke, order));
      } else {
        notation.append(MOVE_SEP)
            .append(wrapStroke(stroke, order))
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
              .append(wrapStroke(stroke, order));
        } else {
          if (continueBeat) {
            notation.append(MOVE_SEP)
                .append(wrapStroke(stroke, order));
          } else {
            notation.append(MOVE_SEP)
                .append(wrapStroke(stroke, order))
                .append(NOTATION_SEP);
          }
        }
      } else if (stopBeat) {
        if (first) {
          notation.append(BEAT_SEP)
              .append(wrapStroke(stroke, order))
              .append(MOVE_SEP);
        } else {
          notation.append(BEAT_SEP)
              .append(wrapStroke(stroke, order))
              .append(NOTATION_SEP);
        }
      } else if (continueBeat) {
        notation.append(BEAT_SEP)
            .append(wrapStroke(stroke, order));
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

  public static String wrapStroke(Stroke stroke, int order) {
    return wrapStroke(stroke, order, stroke.isFirst(), stroke.isSimple(), stroke.isStartBeat(), stroke.isContinueBeat(),
        stroke.isStopBeat());
  }

  private static String wrapStroke(Stroke stroke, int order, boolean first, boolean simple, boolean startBeat, boolean continueBeat, boolean stopBeat) {
    String span = format("<%s id='%s' %s='%s' %s='%s' %s='%s' %s='%s' %s='' %s='' ",
        NOTATION_TAG,
        order,
        DATA_ORDER_ATTR, order,
        DATA_NUMBER_ATTR, stroke.getNumber(),
        DATA_SIMPLE_ATTR, simple,
        DATA_FIRST_ATTR, first,
        DATA_TITLE_ATTR,
        DATA_COMMENT_ATTR);
    span += format("%s='%s' %s='%s' %s='%s'>%s</%s>",
        DATA_START_BEAT_ATTR, startBeat,
        DATA_CONTINUE_BEAT_ATTR, continueBeat,
        DATA_STOP_BEAT_ATTR, stopBeat,
        stroke.isSimple() ? stroke.toNotation()
            : (stroke.isStartBeat() ? stroke.toNotation()
            : stroke.toNotationLastMove()),
        NOTATION_TAG);
    return span;
  }
}
