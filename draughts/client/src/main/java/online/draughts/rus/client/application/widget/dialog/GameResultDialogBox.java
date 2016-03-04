package online.draughts.rus.client.application.widget.dialog;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.03.16
 * Time: 18:55
 */
public class GameResultDialogBox extends InfoDialogBox {
  protected GameResultDialogBox(String content) {
    super(content);

    setMessageToAdminsVisible(true);
  }
}
