package online.draughts.rus.client.application.widget.dialog;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HTML;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.03.16
 * Time: 18:55
 */
public class GameResultDialogBox extends MyDialogBox {
  private EventBus eventBus;

  protected GameResultDialogBox(String content, EventBus eventBus) {
    super(messages.info(), content);
    HTML caption = new HTML(messages.info());
    caption.getElement().addClassName(resources.style().dialogCaptionInfo());
    setHTML(new SafeHtmlBuilder().appendHtmlConstant(caption.getElement().getString()).toSafeHtml());

    setMessageToAdminsVisible(true);
    this.eventBus = eventBus;
  }

  public static GameResultDialogBox setMessage(String message, EventBus eventBus) {
    return new GameResultDialogBox(message, eventBus);
  }

  @Override
  public void submit() {
    messageAdmins(eventBus);
  }
}
