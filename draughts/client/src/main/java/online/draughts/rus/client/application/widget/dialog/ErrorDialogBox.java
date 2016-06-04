package online.draughts.rus.client.application.widget.dialog;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HTML;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import online.draughts.rus.client.channel.PlaySession;
import online.draughts.rus.client.resources.AppResources;
import online.draughts.rus.client.util.Cookies;
import online.draughts.rus.shared.locale.DraughtsMessages;
import online.draughts.rus.shared.resource.ErrorHandlerResource;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 24.09.15
 * Time: 0:14
 */
public class ErrorDialogBox extends MyDialogBox {

  @Inject
  protected ErrorDialogBox(DraughtsMessages messages, PlaySession playSession,
                           ResourceDelegate<ErrorHandlerResource> errorHandlingDelegate,
                           AppResources resources,
                           Cookies cookies, EventBus eventBus) {
    super(messages, playSession.getPlayer(), errorHandlingDelegate, resources, cookies, eventBus, messages.error(), null);

    HTML caption = new HTML(messages.error());
    caption.getElement().addClassName(resources.style().dialogCaptionError());
    setHTML(new SafeHtmlBuilder().appendHtmlConstant(caption.getElement().getString()).toSafeHtml());

    setMessageToAdminsVisible(true);
  }

  private String constructMessage(String message, Throwable throwable) {
    if (message == null || message.isEmpty()) {
      return throwable.getLocalizedMessage();// + "<br>" + Arrays.toString(throwable.getStackTrace());
    }
    if (throwable == null || throwable.getLocalizedMessage().isEmpty()) {
      return message;
    }
    return message + "<br>" + throwable.getLocalizedMessage();// + "<br>" + Arrays.toString(throwable.getStackTrace());
  }

  public void setMessage(Throwable caught) {
    setContent(constructMessage(null, caught));
    addMessageToAdmins(Arrays.toString(caught.getStackTrace()));
  }

  public void setMessage(String message, Throwable throwable) {
    setContent(constructMessage(message, throwable));
    addMessageToAdmins(Arrays.toString(throwable.getStackTrace()));
  }

  public void setMessage(String message) {
    setContent(message);
  }

  @Override
  public void submit() {
    messageAdmins();
  }
}
