package online.shashki.rus.client.application.widget.dialog;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 24.09.15
 * Time: 0:14
 */
public class InfoDialogBox extends MyDialogBox {

  private InfoDialogBox(String header, String content) {
    super(header, content);
  }

  public static InfoDialogBox setMessage(String message) {
    return new InfoDialogBox(messages.info(), message + ".");
  }
}
