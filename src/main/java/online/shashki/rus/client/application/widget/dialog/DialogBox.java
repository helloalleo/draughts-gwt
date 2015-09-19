package online.shashki.rus.client.application.widget.dialog;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.gwtbootstrap3.client.ui.Button;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 25.01.14
 * Time: 14:12
 */
public class DialogBox extends BasicDialogBox {
  private HTML contentHTML;

  public DialogBox(String header, String content) {
    setText(header);

    ScrollPanel contentScrollPanel = new ScrollPanel();

    contentHTML = new HTML();
    contentHTML.setHTML(content);
    contentScrollPanel.add(contentHTML);

    VerticalPanel panel = new VerticalPanel();
    panel.setPixelSize(WIDTH, HEIGHT);
    panel.add(contentScrollPanel);

    final Button buttonClose = new Button(constants.close(), new ClickHandler() {
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

  public void setContent(String content) {
    contentHTML.setHTML(content);
  }

  public void setHeader(String header) {
    setText(header);
  }

  public void submit() {
  }
}
