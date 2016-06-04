package online.draughts.rus.client.application.widget.dialog;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 10/03/14
 * Time: 11:07
 */
public abstract class ConfirmDialogBox extends AbstractDialogBox {

  private boolean confirmed;

  protected ConfirmDialogBox(String content) {
    HTML caption = new HTML(messages.confirm());
    caption.getElement().addClassName(resources.style().dialogCaptionInfo());
    setHTML(new SafeHtmlBuilder().appendHtmlConstant(caption.getElement().getString()).toSafeHtml());
    setModal(true);

    VerticalPanel panel = new VerticalPanel();
    panel.setPixelSize(WIDTH, HEIGHT);

    HTML askHtml = new HTML(content);
    panel.add(askHtml);

    ButtonGroup askButtons = new ButtonGroup();
    Button yesButton = new Button(messages.yes());
    yesButton.addStyleName("btn-primary");

    Button noButton = new Button(messages.no());
    askButtons.add(noButton);
    askButtons.add(yesButton);
    panel.add(askButtons);
    panel.setCellHorizontalAlignment(askButtons, HasAlignment.ALIGN_RIGHT);

    setWidget(panel);

    yesButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        confirmed = true;
        hide();
        procConfirm();
      }
    });

    noButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        confirmed = false;
        hide();
        procConfirm();
      }
    });

    center();
  }

  public boolean isConfirmed() {
    return confirmed;
  }

  public abstract void procConfirm();
}
