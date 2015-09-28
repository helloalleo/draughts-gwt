package online.shashki.rus.client.application.widget;


import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.web.bindery.event.shared.EventBus;
import online.shashki.rus.client.event.*;
import online.shashki.rus.client.utils.SHLog;
import online.shashki.rus.shared.model.Move;
import online.shashki.rus.shashki.Square;

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

//  private final ShashkiGinjector shashkiGinjector = ShashkiGinjector.INSTANCE;
  private static String notation;
  private boolean cancelBite;
  private int cancelCounter;

  public NotationPanel(EventBus eventBus) {
    eventBus.addHandler(NotationMoveEvent.TYPE, new NotationMoveEventHandler() {
      @Override
      public void onNotationMove(NotationMoveEvent event) {
        NotationPanel.this.appendMove(event.getMove(),
            event.isOpponentMove());
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

  public void appendMove(Move move, boolean opponentMove) {
    notation = getElement().getInnerHTML();
    notation = notation.replaceAll(DIV_GARBAGE, "");

    String[] steps = notation.split(NOTATION_SEP);
    if (steps.length == 0) {
      notation = "";
    }
    SHLog.log("MOVE in NOTATION " + move);
    // первый шаг. например, h4:f6:d4 - h4
    Square start = move.getStartSquare();
    SHLog.log("FIRST STEP " + start.toString());

    if (move.isSimple()) {
      if (move.isFirst()) {
        notation += move.getNumber() + COUNT_SEP + move.toNotation(opponentMove);
      } else {
        notation += MOVE_SEP + move.toNotation(opponentMove) + NOTATION_SEP;
      }
    } else { // взята одна или более шашек
      SHLog.log(move.isFirst() + " FIRST CONT BEAT");
      if (move.isStartBeat()) {
        if (move.isFirst()) {
          notation += move.getNumber() + COUNT_SEP + move.toNotation(opponentMove);
        } else {
          if (move.isContinueBeat()) {
            notation += MOVE_SEP + move.toNotation(opponentMove);
          } else {
            notation += MOVE_SEP + move.toNotation(opponentMove) + NOTATION_SEP;
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
    SHLog.log("Notation " + notation);
    pushScroll();
  }


  // код для прокрутки TextArea в конец
  public void pushScroll() {
    scrollToBottom();
  }

  public static String getNotation() {
    return notation;
  }

  public void cancelMove(Move move) {
    SHLog.log(notation);
    notation = notation.replaceAll(DIV_GARBAGE, "");
    if (move.isSimple()) {
      if (move.isFirst()) {
        if (move.getNumber() != 0) {
          notation = notation.substring(0, notation.lastIndexOf(NOTATION_SEP)) + NOTATION_SEP;
        } else {
          notation = "";
        }
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
    getElement().setInnerHTML(notation);
  }
}
