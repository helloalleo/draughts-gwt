package online.draughts.rus.client.application.widget;


import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import online.draughts.rus.draughts.Stroke;
import online.draughts.rus.shared.util.StringUtils;

import static online.draughts.rus.client.util.Utils.format;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 13.03.14
 * Time: 19:32
 */
public class NotationPanel extends ScrollPanel {
  public static final String NOTATION_SEP_TAG = "br";
  public static final String NOTATION_SEP = "<" + NOTATION_SEP_TAG + "/>";
  public static final String BEAT_SEP = ":";
  public static final String MOVE_SEP = " ";
  public static final String COUNT_SEP = ". ";
  public static final String DATA_INDEX = "data-index";
  public static final String DATA_SIMPLE_ATTR = "data-simple";
  public static final String DATA_STOP_BEAT_ATTR = "data-stopbeat";
  public static final String DATA_CONTINUE_BEAT_ATTR = "data-continuebeat";
  public static final String DATA_START_BEAT_ATTR = "data-startbeat";
  public static final String DATA_ORDER_ATTR = "data-order";
  public static final String DATA_FIRST_ATTR = "data-first";
  public static final String DATA_IS_QUEEN_ATTR = "data-isqueen";
  public static final String DATA_IS_WHITE_ATTR = "data-iswhite";
  public static final String DATA_NUMBER_ATTR = "data-number";
  public static final String DATA_COMMENT_ATTR = "data-comment";
  public static final String DATA_TITLE_ATTR = "data-title";
  public static final String COMMENT_SEP = "<br>";
  public static final String NOTATION_TAG = "span";
  public static final String TITLE_COMMENT_SEP = ", ";
  public static final String USER_COMMENT_SEP = ":";
  public static final String NOTATION_A_TAG = "a";

  static StringBuilder notation = new StringBuilder();
  private static int order = 0;
  private static Long gameId;
//  private final HandlerRegistration appendMoveHadnlerRegistration;
//  private final HandlerRegistration cancelMoveHandlerRegistration;
//  private final HandlerRegistration cleanNotationHandlerRegistration;

  @Inject
  public NotationPanel(@Assisted Long gameId) {
    NotationPanel.gameId = gameId;

//    appendMoveHadnlerRegistration = eventBus.addHandler(NotationStrokeEvent.TYPE, new NotationStrokeEventHandler() {
//      @Override
//      public void onNotationStroke(NotationStrokeEvent event) {
//        NotationPanel.this.appendMove(event.getStroke());
//      }
//    });
//    cancelMoveHandlerRegistration = eventBus.addHandler(NotationCancelStrokeEvent.TYPE, new NotationCancelStrokeEventHandler() {
//      @Override
//      public void onNotationCancelStroke(NotationCancelStrokeEvent event) {
//        NotationPanel.this.cancelMove(event.getStroke());
//      }
//    });
//    cleanNotationHandlerRegistration = eventBus.addHandler(ClearNotationEvent.TYPE, new ClearNotationEventHandler() {
//      @Override
//      public void onClearNotation(ClearNotationEvent event) {
//        cleanNotationPanel();
//      }
//    });
  }

  public static String getNotation() {
    return wrapNotation(notation.toString());
  }

  private static String wrapNotation(String notation) {
    return format("<div id='%s'>%s</div>", gameId, notation);
  }

  public void appendMove(Stroke stroke) {
    // первый шаг. например, h4:f6:d4 - h4
    final boolean first = stroke.isFirst();
    if (stroke.isSimple()) {
      if (first) {
        notation.append(wrapInSpan(createNotationNum(stroke)))
            .append(wrapStrokeInAnchor(stroke, order));
      } else {
        notation.append(wrapInSpan(MOVE_SEP))
            .append(wrapStrokeInAnchor(stroke, order))
            .append(NOTATION_SEP);
      }
    } else { // взята одна или более шашек
      final boolean startBeat = stroke.isStartBeat();
      final boolean continueBeat = stroke.isContinueBeat();
      final boolean stopBeat = stroke.isStopBeat();
      if (startBeat) {
        if (first) {
          notation.append(wrapInSpan(createNotationNum(stroke)))
              .append(wrapStrokeInAnchor(stroke, order));
        } else {
          if (continueBeat) {
            notation.append(wrapInSpan(MOVE_SEP))
                .append(wrapStrokeInAnchor(stroke, order));
          } else {
            notation.append(wrapInSpan(MOVE_SEP))
                .append(wrapStrokeInAnchor(stroke, order))
                .append(NOTATION_SEP);
          }
        }
      } else if (stopBeat) {
        if (first) {
          notation.append(wrapInSpan(BEAT_SEP))
              .append(wrapStrokeInAnchor(stroke, order))
              .append(wrapInSpan(MOVE_SEP));
        } else {
          notation.append(wrapInSpan(BEAT_SEP))
              .append(wrapStrokeInAnchor(stroke, order))
              .append(NOTATION_SEP);
        }
      } else if (continueBeat) {
        notation.append(wrapInSpan(BEAT_SEP))
            .append(wrapStrokeInAnchor(stroke, order));
      }
    }
    order++;

    getElement().setInnerHTML(notation.toString());
    pushScroll();
  }

  private String[] createNotationNum(Stroke stroke) {
    return new String[]{String.valueOf(stroke.getNumber()), COUNT_SEP};
  }

  private String wrapInSpan(String... args) {
    return "<" + NOTATION_TAG + ">" + StringUtils.join(args, "") + "</" + NOTATION_TAG + ">";
  }

  // код для прокрутки TextArea в конец
  private void pushScroll() {
    scrollToBottom();
  }

  public void cancelMove(Stroke stroke) {
    if (stroke.isSimple()) {
      if (stroke.isFirst()) {
        if (stroke.getNumber() != 1) {
          notation = new StringBuilder(notation.substring(0, notation.lastIndexOf(NOTATION_SEP)) + NOTATION_SEP);
        } else {
          notation = new StringBuilder();
          getElement().setInnerHTML("");
          order = 0;
          return;
        }
      } else {
        notation = new StringBuilder(notation.substring(0, notation.lastIndexOf(wrapInSpan(MOVE_SEP))));
      }
    } else {
      if (stroke.isFirst()) {
        if (stroke.isStartBeat()) {
          notation = new StringBuilder(notation.substring(0, notation.lastIndexOf(NOTATION_SEP)) + NOTATION_SEP);
        } else {
          notation = new StringBuilder(notation.substring(0, notation.lastIndexOf(wrapInSpan(BEAT_SEP))));
        }
      } else {
        if (stroke.isStartBeat()) {
          notation = new StringBuilder(notation.substring(0, notation.lastIndexOf(wrapInSpan(MOVE_SEP))));
        } else {
          notation = new StringBuilder(notation.substring(0, notation.lastIndexOf(wrapInSpan(BEAT_SEP))));
        }
      }
    }
    order--;
    getElement().setInnerHTML(notation.toString());
  }

  public void cleanNotationPanel() {
    notation = new StringBuilder();
    getElement().setInnerHTML("");
    order = 0;
//    appendMoveHadnlerRegistration.removeHandler();
//    cancelMoveHandlerRegistration.removeHandler();
//    cleanNotationHandlerRegistration.removeHandler();
  }

  private static String wrapStrokeInAnchor(Stroke stroke, int order) {
    return format("<%s>%s</%s>", NOTATION_A_TAG, wrapStroke(stroke, order), NOTATION_A_TAG);
  }

  public static String wrapStroke(Stroke stroke, int order) {
    String span = format("<%s id='%s' %s='%s' %s='%s' %s='%s' %s='%s' %s='%s' %s='%s' %s='%s' %s=\"%s\" %s=\"%s\" ",
        NOTATION_TAG,
        order,
        DATA_ORDER_ATTR, order,
        DATA_INDEX, stroke.getIndex(),
        DATA_NUMBER_ATTR, stroke.getNumber(),
        DATA_SIMPLE_ATTR, stroke.isSimple(),
        DATA_FIRST_ATTR, stroke.isFirst(),
        DATA_IS_QUEEN_ATTR, stroke.isQueen(),
        DATA_IS_WHITE_ATTR, stroke.isWhite(),
        DATA_TITLE_ATTR, StringUtils.emptyIfNull(stroke.getTitle()),
        DATA_COMMENT_ATTR, StringUtils.emptyIfNull(stroke.getComment()));
    span += format("%s='%s' %s='%s' %s='%s'>%s</%s>",
        DATA_START_BEAT_ATTR, stroke.isStartBeat(),
        DATA_CONTINUE_BEAT_ATTR, stroke.isContinueBeat(),
        DATA_STOP_BEAT_ATTR, stroke.isStartBeat(),
        stroke.isSimple() ? stroke.toNotation()
            : (stroke.isStartBeat() ? stroke.toNotation()
            : stroke.toNotationLastMove()),
        NOTATION_TAG);
    return span;
  }
}
