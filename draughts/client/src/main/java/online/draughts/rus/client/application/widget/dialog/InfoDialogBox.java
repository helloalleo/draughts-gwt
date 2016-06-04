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
 * Date: 24.09.15
 * Time: 0:14
 */
public class InfoDialogBox extends MyDialogBox {

  @Inject
  protected InfoDialogBox(DraughtsMessages messages,
                          PlaySession playSession,
                          ResourceDelegate<ErrorHandlerResource> errorHandlingDelegate,
                          AppResources resources,
                          Cookies cookies,
                          EventBus eventBus,
                          @Assisted String content) {
    super(messages, playSession.getPlayer(), errorHandlingDelegate, resources, cookies, eventBus, messages.info(), content);
    HTML caption = new HTML(messages.info());
    caption.getElement().addClassName(resources.style().dialogCaptionInfo());
    setHTML(new SafeHtmlBuilder().appendHtmlConstant(caption.getElement().getString()).toSafeHtml());
  }

  public void setMessage(String message) {
    setContent(message);
  }

  @Override
  public void submit() {
  }
}
