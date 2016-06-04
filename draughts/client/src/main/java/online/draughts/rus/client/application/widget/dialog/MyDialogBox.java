package online.draughts.rus.client.application.widget.dialog;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import online.draughts.rus.client.resources.AppResources;
import online.draughts.rus.client.util.Cookies;
import online.draughts.rus.client.util.Logger;
import online.draughts.rus.shared.dto.PlayerDto;
import online.draughts.rus.shared.locale.DraughtsMessages;
import online.draughts.rus.shared.resource.ErrorHandlerResource;
import online.draughts.rus.shared.util.StringUtils;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.TextArea;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 25.01.14
 * Time: 14:12
 */
public abstract class MyDialogBox extends BasicDialogBox {
  private final TextArea messageToAdmins = new TextArea();
  private HTML contentHTML;
  private String messageToAdminsText;
  private final PlayerDto player;
  private final ResourceDelegate<ErrorHandlerResource> errorHandlingDelegate;


  MyDialogBox(DraughtsMessages messages,
              PlayerDto player,
              ResourceDelegate<ErrorHandlerResource> errorHandlingDelegate,
              AppResources resources,
              Cookies cookies, EventBus eventBus,
              String header,
              String content) {
    super(messages, resources, cookies, eventBus, header, content);
    this.player = player;
    this.errorHandlingDelegate = errorHandlingDelegate;
    ScrollPanel contentScrollPanel = new ScrollPanel();

    contentHTML = new HTML();
    contentHTML.setHTML(content);
    contentScrollPanel.add(contentHTML);

    VerticalPanel panel = new VerticalPanel();
    panel.setPixelSize(WIDTH, HEIGHT);
    panel.add(contentScrollPanel);

    messageToAdmins.setPlaceholder(messages.messageToAdmins());
    panel.add(messageToAdmins);
    messageToAdmins.setVisible(false);

    final Button buttonClose = new Button(messages.ok(), new ClickHandler() {
      @Override
      public void onClick(final ClickEvent event) {
        hide();
        submit();
      }
    });
    panel.add(buttonClose);

    panel.setCellHorizontalAlignment(buttonClose, HasAlignment.ALIGN_RIGHT);

    setWidget(panel);

    center();

    getElement().getStyle().setZIndex(1000);
  }

  void messageAdmins() {
    addMessageToAdmins(messageToAdmins.getValue());
    if (StringUtils.isEmpty(messageToAdminsText)) {
      return;
    }

    Long senderId = null;
    if (null != player) {
      senderId = player.getId();
    }
    Logger.debug(messageToAdminsText);
    errorHandlingDelegate.withoutCallback().postError(messageToAdminsText, senderId);
  }

  void setContent(String content) {
    contentHTML.setHTML(content);
  }

  public void setHeader(String header) {
    setText(header);
  }

  void addMessageToAdmins(String message) {
    messageToAdminsText += message;
  }

  void setMessageToAdminsVisible(boolean visible) {
    messageToAdmins.setVisible(visible);
  }

  public abstract void submit();
}
