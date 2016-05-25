package online.draughts.rus.client.application.widget.dialog;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.Label;
import online.draughts.rus.shared.dto.GameDto;
import online.draughts.rus.shared.util.StringUtils;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.RadioButton;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
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
  private final HorizontalPanel waitMessage = new HorizontalPanel();
  private final TextBox customTime;
  private CheckBox publishGame;
  private final Timer waitResponseTimer;
  private final ListBox playsListBox;
  private final ListBox minutesListBox;
  private final TextBox fisherTime;

  private int waitCounter = 0;
  private final int WAIT = 20;
  private Timer timerCounterTimer;
  private Label waitMessageLabel;

  protected InviteDialogBox(boolean subscribed, ClickHandler submitClickHandler) {
    HTML caption = new HTML(messages.inviteCaption());
    caption.getElement().addClassName(resources.style().dialogCaptionInfo());
    setHTML(new SafeHtmlBuilder().appendHtmlConstant(caption.getElement().getString()).toSafeHtml());

    VerticalPanel panel = new VerticalPanel();
    panel.setPixelSize(WIDTH, HEIGHT);

    panel.add(inviteLabel);

    playsListBox = new ListBox();
    playsListBox.addItem(messages.draughts(), GameDto.GameType.DRAUGHTS.name());
    playsListBox.addItem(messages.giveaway(), GameDto.GameType.GIVEAWAY.name());
    panel.add(playsListBox);

    submitButton = new Button(messages.next());
    final Button cancelButton = new Button(messages.cancel());

    Label label = new Label(messages.chooseYourColor());
    panel.add(label);

    FlowPanel radioGroup = new FlowPanel();
    whiteRadio = new RadioButton("white", messages.white());
    whiteRadio.setName("rb-color");
    whiteRadio.getElement().getStyle().setMargin(10, Style.Unit.PX);
    RadioButton blackRadio = new RadioButton("black", messages.black());
    blackRadio.setName("rb-color");
    whiteRadio.setValue(true);

    radioGroup.add(whiteRadio);
    radioGroup.add(blackRadio);
    panel.add(radioGroup);

    customTime = new TextBox();
    customTime.setPlaceholder(messages.minutes());
    customTime.setVisible(false);
    customTime.addKeyUpHandler(new KeyUpHandler() {
      @Override
      public void onKeyUp(KeyUpEvent keyDownEvent) {
        if (!customTime.getValue().matches("\\d+")) {
          customTime.getElement().getStyle().setBorderColor("red");
          keyDownEvent.preventDefault();
        } else {
          customTime.getElement().getStyle().setBorderColor("#ccc");
          cookies.setTimeOnPlay(customTime.getValue());
        }
      }
    });
    Label timeLabel = new Label(messages.timeOnPlay());
    minutesListBox = new ListBox();
    minutesListBox.addItem("3");
    minutesListBox.addItem("5");
    minutesListBox.addItem("10");
    minutesListBox.addItem("20");
    minutesListBox.addItem(messages.specifyTime());

    minutesListBox.addChangeHandler(
        new ChangeHandler() {
          @Override
          public void onChange(ChangeEvent changeEvent) {
            customTime.setValue("");
            if (minutesListBox.getSelectedValue().equals(messages.specifyTime())) {
              customTime.setVisible(true);
              cookies.setTimeOnPlay(customTime.getValue());
            } else {
              customTime.setVisible(false);
              cookies.setTimeOnPlay(minutesListBox.getSelectedValue());
            }
            cookies.setTimeOnPlayCustom(String.valueOf(customTime.isVisible()));
          }
        });
    panel.add(timeLabel);
    panel.add(minutesListBox);
    panel.add(customTime);

    fisherTime = new TextBox();
    fisherTime.setPlaceholder(messages.seconds());
    fisherTime.addKeyUpHandler(new KeyUpHandler() {
      @Override
      public void onKeyUp(KeyUpEvent keyDownEvent) {
        if (!fisherTime.getValue().matches("\\d+")) {
          fisherTime.getElement().getStyle().setBorderColor("red");
          keyDownEvent.preventDefault();
        } else {
          fisherTime.getElement().getStyle().setBorderColor("#ccc");
          cookies.setFisherTime(fisherTime.getValue());
        }
      }
    });
    Label fisherTimeLabel = new HTML(messages.fisherTime());
    panel.add(fisherTimeLabel);
    panel.add(fisherTime);

    if (subscribed) {
      publishGame = new CheckBox(messages.publishGame());
      publishGame.setValue(cookies.isPublishGame());
      panel.add(publishGame);
    }

    final Icon waitImage = new Icon(IconType.SPINNER);
    waitImage.setSize(IconSize.LARGE);
    waitImage.setSpin(true);
    waitImage.getElement().getStyle().setMarginTop(8, Style.Unit.PX);
    waitMessage.setVisible(false);
    waitMessage.add(waitImage);
    waitMessageLabel = new Label(messages.waitResponse());
    waitMessageLabel.setHeight("30px");
    waitMessageLabel.getElement().getStyle().setPaddingLeft(5, Style.Unit.PX);
    waitMessageLabel.getElement().getStyle().setVerticalAlign(Style.VerticalAlign.MIDDLE);
    waitMessageLabel.getElement().getStyle().setProperty("display", "inherit");
    waitMessage.add(waitMessageLabel);

    waitCounter = WAIT;
    timerCounterTimer = new Timer() {
      @Override
      public void run() {
        waitMessageLabel.setText(messages.waitResponse() + " " + waitCounter);
        waitCounter -= 1;
      }
    };
    panel.add(waitMessage);

    ButtonGroup group = new ButtonGroup();
    group.add(cancelButton);
    group.add(submitButton);
    submitButton.setType(ButtonType.PRIMARY);

    panel.add(group);
    panel.setCellHorizontalAlignment(group, HasAlignment.ALIGN_RIGHT);

    setWidget(panel);

    waitResponseTimer = new Timer() {
      @Override
      public void run() {
        hide();
        didNotResponse();
        stopWaiting();
      }
    };

    // последовательность обработчиков кликов обязательна!
    submitButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        if (StringUtils.isNotEmpty(customTime.getValue()) && !customTime.getValue().matches("\\d+")) {
          event.preventDefault();
          return;
        }
        if (StringUtils.isEmpty(customTime.getValue())
            && messages.specifyTime().equals(minutesListBox.getSelectedItemText())) {
          customTime.getElement().getStyle().setBorderColor("red");
          event.preventDefault();
          return;
        }
        if (!fisherTime.getValue().matches("\\d+") && !fisherTime.getValue().isEmpty()) {
          fisherTime.getElement().getStyle().setBorderColor("red");
          event.preventDefault();
          return;
        }
        final boolean publish = InviteDialogBox.this.publishGame == null ? true : InviteDialogBox.this.publishGame.getValue();
        cookies.setPublishGame(publish);
        waitMessage.setVisible(true);
        waitResponseTimer.schedule(WAIT * 1000);
        timerCounterTimer.scheduleRepeating(1000);
        String timeOnPlay;
        if (messages.specifyTime().equals(minutesListBox.getSelectedValue())) {
          timeOnPlay = customTime.getValue();
        } else {
          timeOnPlay = minutesListBox.getSelectedValue();
        }
        String fTime = fisherTime.getValue().isEmpty() ? "0" : fisherTime.getValue();
        submitted(GameDto.GameType.valueOf(playsListBox.getSelectedValue()), whiteRadio.getValue(),
            timeOnPlay, fTime, publish);
        submitButton.setEnabled(false);
      }
    });
    submitButton.addClickHandler(submitClickHandler);

    cancelButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        hide();
        stopWaiting();
      }
    });

    setTime();
  }

  public void show(String message) {
    inviteLabel.setHTML(message);
    show();
    center();
  }

  private void setTime() {
    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
      @Override
      public void execute() {
        final int timeOnPlayCookie = cookies.getTimeOnPlay();
        final boolean timeOnPlayCustomCookie = cookies.getTimeOnPlayCustom();
        if (timeOnPlayCustomCookie) {
          customTime.setValue(String.valueOf(timeOnPlayCookie));
          customTime.setVisible(true);
          setSelectedValue(minutesListBox, messages.specifyTime());
        } else {
          setSelectedValue(minutesListBox, String.valueOf(timeOnPlayCookie));
        }

        final int fTime = cookies.getFisherTime();
        fisherTime.setValue(String.valueOf(fTime));
      }
    });
  }

  private void setSelectedValue(ListBox listBox, String value) {
    if (StringUtils.isEmpty(value)) {
      return;
    }
    for (int i = 0; i < listBox.getItemCount(); i++) {
      if (value.equals(listBox.getValue(i))) {
        listBox.setSelectedIndex(i);
        return;
      }
    }
  }

  @Override
  public void hide() {
    super.hide();
    stopWaiting();
  }

  public abstract void didNotResponse();

  private void stopWaiting() {
    timerCounterTimer.cancel();
    waitResponseTimer.cancel();
    submitButton.setEnabled(true);
    waitMessageLabel.setText(messages.waitResponse());
    waitMessage.setVisible(false);
    waitCounter = WAIT;
  }

  public abstract void submitted(GameDto.GameType gameType, boolean white, String timeOnPlay, String fisherTime,
                                 Boolean publishGame);
}
