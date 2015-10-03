package online.shashki.rus.client.application.widget.dialog;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 24.09.15
 * Time: 0:14
 */
public class InfoDialogBox extends MyDialogBox {

  private InfoDialogBox(String content) {
    super(messages.info(), content);
  }

  public static InfoDialogBox setMessage(String message) {
    return new InfoDialogBox(message);
  }
}
