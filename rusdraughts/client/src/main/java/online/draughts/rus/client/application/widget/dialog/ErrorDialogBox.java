package online.draughts.rus.client.application.widget.dialog;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HTML;
import online.draughts.rus.client.util.DTLog;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 24.09.15
 * Time: 0:14
 */
public class ErrorDialogBox extends MyDialogBox {

  private ErrorDialogBox(String content) {
    super(messages.error(), content);

    HTML caption = new HTML(messages.error());
    caption.getElement().addClassName(resources.style().dialogCaptionError());
    setHTML(new SafeHtmlBuilder().appendHtmlConstant(caption.getElement().getString()).toSafeHtml());
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
    DTLog.error(caught.getLocalizedMessage(), caught);
    return new ErrorDialogBox(constructMessage(null, caught));
  }

  public static ErrorDialogBox setMessage(String message, Throwable throwable) {
    return new ErrorDialogBox(constructMessage(message, throwable));
  }

  public static ErrorDialogBox setMessage(String message) {
    return new ErrorDialogBox(constructMessage(message, null));
  }
}
