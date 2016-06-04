package online.draughts.rus.client.application.widget.dialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import online.draughts.rus.client.resources.AppResources;
import online.draughts.rus.shared.locale.DraughtsMessages;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 10.02.14
 * Time: 9:16
 */
public abstract class ConfirmPlayDialogBox extends AbstractDialogBox {

  private final Label confirmLabel = new Label();
  private Long senderId;
  private boolean white;
  private String senderName;

  @Inject
  protected ConfirmPlayDialogBox() {
    HTML caption = new HTML(messages.captionGame());
    caption.getElement().addClassName(resources.style().dialogCaptionInfo());
    setHTML(new SafeHtmlBuilder().appendHtmlConstant(caption.getElement().getString()).toSafeHtml());

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

  public void show(String message, boolean playWithColor) {
    this.white = playWithColor;
    show();
    confirmLabel.setText(message);
    center();
  }
}
