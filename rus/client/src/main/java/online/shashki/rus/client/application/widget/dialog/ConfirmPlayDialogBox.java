package online.shashki.rus.client.application.widget.dialog;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import online.shashki.rus.shared.model.Player;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 10.02.14
 * Time: 9:16
 */
public abstract class ConfirmPlayDialogBox extends BasicDialogBox {

  private final Label confirmLabel = new Label();
  private String senderId;
  private boolean white;
  private String senderName;

  public ConfirmPlayDialogBox() {
    setText(messages.captionGame());
    VerticalPanel panel = new VerticalPanel();
    panel.setPixelSize(WIDTH, HEIGHT);
    panel.add(confirmLabel);

    ButtonGroup group = new ButtonGroup();
    Button yesButton = new Button(messages.yes());
    Button noButton = new Button(messages.no());
    group.add(noButton);
    group.add(yesButton);

    yesButton.addStyleName("btn-primary");

    panel.add(group);
    panel.setCellHorizontalAlignment(group, HasAlignment.ALIGN_RIGHT);

    setWidget(panel);

    yesButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        hide();
        submitted();
      }
    });
    noButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        hide();
        canceled();
      }
    });

  }

  public abstract void submitted();

  public abstract void canceled();

  public boolean isWhite() {
    return white;
  }

  public String getSenderId() {
    return senderId;
  }

  public String getSenderName() {
    return senderName;
  }

  public void show(String message, Player sender, boolean playWithColor) {
    this.senderId = sender.getSessionId();
    this.senderName = sender.getPublicName();
    this.white = playWithColor;
    show();
    confirmLabel.setText(message);
    center();
  }
}
