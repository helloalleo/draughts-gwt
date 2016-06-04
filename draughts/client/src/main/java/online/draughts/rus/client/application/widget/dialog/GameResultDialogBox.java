package online.draughts.rus.client.application.widget.dialog;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HTML;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import online.draughts.rus.client.channel.PlaySession;
import online.draughts.rus.client.resources.AppResources;
import online.draughts.rus.client.util.Cookies;
import online.draughts.rus.shared.locale.DraughtsMessages;
import online.draughts.rus.shared.resource.ErrorHandlerResource;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.03.16
 * Time: 18:55
 */
public class GameResultDialogBox extends MyDialogBox {

  @Inject
  protected GameResultDialogBox(DraughtsMessages messages,
                                PlaySession playSession,
                                ResourceDelegate<ErrorHandlerResource> errorHandlingDelegate,
                                AppResources resources,
                                Cookies cookies, EventBus eventBus, @Assisted String content) {
    super(messages, playSession.getPlayer(), errorHandlingDelegate, resources, cookies, eventBus,
        messages.info(), content);
    HTML caption = new HTML(messages.info());
    caption.getElement().addClassName(resources.style().dialogCaptionInfo());
    setHTML(new SafeHtmlBuilder().appendHtmlConstant(caption.getElement().getString()).toSafeHtml());

    setMessageToAdminsVisible(true);
  }

  public void setMessage(String message) {
    setContent(message);
  }

  @Override
  public void submit() {
    messageAdmins();
  }
}
