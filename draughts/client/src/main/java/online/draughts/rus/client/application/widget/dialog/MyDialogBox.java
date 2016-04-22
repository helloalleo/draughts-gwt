package online.draughts.rus.client.application.widget.dialog;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.web.bindery.event.shared.EventBus;
import online.draughts.rus.client.event.GameMessageEvent;
import online.draughts.rus.client.util.Logger;
import online.draughts.rus.shared.dto.GameMessageDto;
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

  public MyDialogBox(String header, String content) {
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

  public void messageAdmins(EventBus eventBus) {
    if (StringUtils.isEmpty(messageToAdmins.getValue())) {
      return;
    }

    GameMessageDto gameMessage = new GameMessageDto();
    gameMessage.setMessageType(GameMessageDto.MessageType.CHAT_PRIVATE_MESSAGE);
    gameMessage.setReceiver(null);
    gameMessage.setMessage(messageToAdmins.getValue());

    eventBus.fireEvent(new GameMessageEvent(gameMessage));
  }

  public void setContent(String content) {
    contentHTML.setHTML(content);
  }

  public void setHeader(String header) {
    setText(header);
  }

  protected void setMessageToAdminsVisible(boolean visible) {
    messageToAdmins.setVisible(visible);
    Logger.debug("Message to admins " + visible);
  }

  public abstract void submit();
}
