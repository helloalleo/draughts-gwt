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

  private static String constructMessage(String message, Throwable throwable) {
    if (message == null || message.isEmpty()) {
      return throwable.getLocalizedMessage();
    }
    if (throwable == null || throwable.getLocalizedMessage().isEmpty()) {
      return message;
    }
    return message + "<br>" + throwable.getLocalizedMessage();
  }

  public static ErrorDialogBox setMessage(Throwable caught) {
    return new ErrorDialogBox(messages.error(), constructMessage(null, caught));
  }

  public static ErrorDialogBox setMessage(String message, Throwable throwable) {
    return new ErrorDialogBox(messages.error(), constructMessage(message, throwable));
  }

  public static ErrorDialogBox setMessage(String message) {
    return new ErrorDialogBox(messages.error(), constructMessage(message, null));
  }
}
