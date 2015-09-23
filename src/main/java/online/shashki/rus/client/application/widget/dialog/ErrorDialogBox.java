package online.shashki.rus.client.application.widget.dialog;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 24.09.15
 * Time: 0:14
 */
public class ErrorDialogBox extends MyDialogBox {

  private ErrorDialogBox(String header, String content) {
    super(header, content);
  }

  public static ErrorDialogBox showError(Throwable caught) {
    return new ErrorDialogBox(messages.error(), caught.getLocalizedMessage());
  }
}
