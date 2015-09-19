package online.shashki.rus.client.application.widget.dialog;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import online.shashki.rus.client.resources.ResourceLoader;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.Image;
import org.gwtbootstrap3.client.ui.RadioButton;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 09.02.14
 * Time: 21:15
 */
public abstract class InviteDialogBox extends BasicDialogBox {
  private final HTML inviteLabel = new HTML();
  private final RadioButton whiteRadio;
  private final Button submitButton;
  private final Button cancelButton;
  private final HorizontalPanel waitMessage = new HorizontalPanel();
  private Timer waitResponseTimer;

  private int waitCounter = 0;
  private final int WAIT = 29;
  private Timer timerCounterTimer;
  private Label waitMessageLabel;

  public InviteDialogBox() {
    setText(constants.captionGame());
    VerticalPanel panel = new VerticalPanel();
    panel.setPixelSize(WIDTH, HEIGHT);

    panel.add(inviteLabel);

    submitButton = new Button(constants.next());
    cancelButton = new Button(constants.cancel());

    Label label = new Label(constants.chooseYourColor());
    panel.add(label);

    FlowPanel radioGroup = new FlowPanel();
    whiteRadio = new RadioButton(constants.white());
    whiteRadio.setName("rb-color");
    whiteRadio.getElement().getStyle().setMargin(10, Style.Unit.PX);
    RadioButton blackRadio = new RadioButton(constants.black());
    blackRadio.setName("rb-color");
    whiteRadio.setValue(true);

    radioGroup.add(whiteRadio);
    radioGroup.add(blackRadio);
    panel.add(radioGroup);

    final Image waitImage = new Image(ResourceLoader.INSTANCE.images().busyIconImage());
    waitMessage.setVisible(false);
    waitMessage.add(waitImage);
    waitMessageLabel = new Label(constants.waitResponse());
    waitMessageLabel.setHeight("32px");
    waitMessageLabel.getElement().getStyle().setPaddingLeft(5, Style.Unit.PX);
    waitMessageLabel.getElement().getStyle().setVerticalAlign(Style.VerticalAlign.MIDDLE);
    waitMessageLabel.getElement().getStyle().setProperty("display", "inherit");
    waitMessage.add(waitMessageLabel);

    waitCounter = WAIT;
    timerCounterTimer = new Timer() {
      @Override
      public void run() {
        waitMessageLabel.setText(constants.waitResponse() + " " + waitCounter);
        waitCounter -= 1;
      }
    };
    panel.add(waitMessage);

    ButtonGroup group = new ButtonGroup();
    group.add(cancelButton);
    group.add(submitButton);
    submitButton.addStyleName("btn-primary");

    panel.add(group);
    panel.setCellHorizontalAlignment(group, HasAlignment.ALIGN_RIGHT);

    setWidget(panel);

    waitResponseTimer = new Timer() {
      @Override
      public void run() {
        hide();
        stopTimers();
      }
    };

    submitButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        waitMessage.setVisible(true);
        waitResponseTimer.schedule((WAIT + 1) * 1000);
        timerCounterTimer.scheduleRepeating(1000);
        submitted(whiteRadio.getValue());
        submitButton.setEnabled(false);
      }
    });

    cancelButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        hide();
        timerCounterTimer.cancel();
        waitResponseTimer.cancel();
        submitButton.setEnabled(true);
        waitMessageLabel.setText(constants.waitResponse());
        waitMessage.setVisible(false);
        waitCounter = WAIT;
      }
    });
  }

  public void show(String message) {
    inviteLabel.setHTML(message);
    show();
    center();
  }

  @Override
  public void hide() {
    super.hide();
    stopTimers();
  }

  private void stopTimers() {
    timerCounterTimer.cancel();
    waitMessageLabel.setText(constants.waitResponse());
    waitCounter = WAIT;
    submitButton.setEnabled(true);
    waitMessage.setVisible(false);
  }

  public abstract void submitted(boolean white);

}
