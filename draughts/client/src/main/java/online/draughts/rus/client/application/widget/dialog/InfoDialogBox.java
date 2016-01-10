package online.draughts.rus.client.application.widget.dialog;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HTML;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 24.09.15
 * Time: 0:14
 */
public class InfoDialogBox extends MyDialogBox {

  private InfoDialogBox(String content) {
    super(messages.info(), content);
    HTML caption = new HTML(messages.info());
    caption.getElement().addClassName(resources.style().dialogCaptionInfo());
    setHTML(new SafeHtmlBuilder().appendHtmlConstant(caption.getElement().getString()).toSafeHtml());
  }

  public static InfoDialogBox setMessage(String message) {
    return new InfoDialogBox(message);
  }
}
