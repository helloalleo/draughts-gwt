package online.draughts.rus.client.gin;

import online.draughts.rus.client.application.widget.dialog.*;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.06.16
 * Time: 6:02
 */
public interface DialogFactory {
  ErrorDialogBox createErrorDialogBox();
  InfoDialogBox createInfoDialogBox(String content);
  GameResultDialogBox createGameResultDialogBox(String content);
}
