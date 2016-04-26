
package online.draughts.rus.client.application.play.messanger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupViewWithUiHandlers;
import com.gwtplatform.mvp.client.view.PopupPositioner;
import online.draughts.rus.client.application.play.PlayView;
import online.draughts.rus.client.resources.AppResources;
import online.draughts.rus.client.util.Cookies;
import online.draughts.rus.shared.util.StringUtils;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessengerView extends PopupViewWithUiHandlers<MessengerUiHandlers> implements MessengerPresenter.MyView {

  private static final List<String> codes = new ArrayList<>();
  static {
    codes.add("1F60");
    codes.add("1F61");
    codes.add("1F62");
    codes.add("1F63");
    codes.add("1F64");
  }

  private final PlayView playView;
  private final AppResources resources;
  private static final int LAST_SMILE_COUNT = 10;
  private final Cookies cookies;
  @UiField
  Button collapseMessengerButton;
  @UiField
  PanelHeader messengerHeader;
  @UiField
  PanelBody messengerBody;
  @UiField
  Heading messengerHeading;
  @UiField
  HTMLPanel messengerMessages;
  @UiField
  TextArea messengerMessage;
  @UiField
  ScrollPanel messengerMessagesScroll;
  @UiField
  HTMLPanel smilesPanel;
  @UiField
  HTMLPanel smileChoosePanel;
  @UiField
  ScrollPanel smilesScrollPanel;
  @UiField
  FlowPanel lastUsedSmiles;

  private List<String> lastUsedSmilesQueue = new ArrayList<>(LAST_SMILE_COUNT);

  @Inject
  public MessengerView(Binder uiBinder,
                       EventBus eventBus,
                       AppResources resources,
                       Cookies cookies,
                       PlayView playView) {
    super(eventBus);

    initWidget(uiBinder.createAndBindUi(this));

    asPopupPanel().setAnimationEnabled(true);

    this.playView = playView;
    this.resources = resources;
    this.cookies = cookies;

    lastUsedSmilesQueue = cookies.getLastUsedSmiles();
  }

  private void fillSmileChoosePanel() {
    for (int i = 0; i < 15; i++) {
      for (int j = 0; j < 5; j++) {
        final String smile = codes.get(j) + Integer.toHexString(i);
        final HTML smileHtml = new HTML("&#x" + smile + ";");
        int ch = Integer.parseInt(smile, 16);
        String sm = new String(Character.toChars(ch));
        final Button smileButton = createSmileButton(sm, smileHtml);
        smileChoosePanel.add(smileButton);
      }
    }
  }

  private Button createSmileButton(final String smile, final HTML smileHtml) {
    final Button smileButton = new Button();
    smileButton.setType(ButtonType.LINK);
    smileButton.getElement().getStyle().setTextDecoration(Style.TextDecoration.NONE);
    smileButton.add(smileHtml);
    smileButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        smileButtonClicked(smile, smileHtml, smileButton);
      }
    });
    return smileButton;
  }

  private void smileButtonClicked(final String smile, final HTML smileHtml, final Button smileButton) {
    String text = messengerMessage.getText();
    messengerMessage.setText(text + smile);
    messengerMessage.setCursorPos(messengerMessage.getText().length());
    if (lastUsedSmilesQueue.contains(smile)) {
      return;
    }

    Button lastUsedButton = new Button();
    lastUsedButton.add(new HTML(smileHtml.getHTML()));
    lastUsedButton.setType(smileButton.getType());
    lastUsedButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        smileButtonClicked(smile, smileHtml, smileButton);
      }
    });
    lastUsedButton.getElement().getStyle().setTextDecoration(Style.TextDecoration.NONE);
    lastUsedButton.getElement().getStyle().setPaddingLeft(4, Style.Unit.PX);
    lastUsedButton.getElement().getStyle().setPaddingRight(4, Style.Unit.PX);
    lastUsedSmiles.insert(lastUsedButton, 0);

    if (lastUsedSmilesQueue.size() > LAST_SMILE_COUNT) {
      lastUsedSmilesQueue.remove(lastUsedSmilesQueue.size() - 1);
      lastUsedSmiles.remove(lastUsedSmiles.getWidgetCount() - 1);
    }
    lastUsedSmilesQueue.add(smile);
    cookies.setLastUsedSmiles(lastUsedSmilesQueue);
  }

  @SuppressWarnings(value = "unused")
  @UiHandler("collapseMessengerButton")
  public void onCollapseMessenger(ClickEvent clickEvent) {
    getUiHandlers().resetOpponent();
    hide();
  }

  @UiHandler("messengerMessage")
  public void onReturn(KeyPressEvent keyPressEvent) {
    if (!keyPressEvent.isShiftKeyDown()
        && keyPressEvent.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
      final String message = messengerMessage.getText();
      if (StringUtils.isEmpty(message)) {
        return;
      }
      HTMLPanel html = formatMessage(message, new Date(), false);

      messengerMessages.add(html);
      messengerMessagesScroll.scrollToBottom();

      getUiHandlers().sendMessage(message);
      Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
        @Override
        public void execute() {
          messengerMessage.setText("");
        }
      });
    }
  }

  @SuppressWarnings("unused")
  @UiHandler("messengerMessage")
  public void onMessageTextAreaClicked(ClickEvent event) {
    smilesPanel.setVisible(false);
  }

  @SuppressWarnings("unused")
  @UiHandler("smilesButton")
  public void onSmilesButtonClicked(ClickEvent event) {
    smilesPanel.setVisible(!smilesPanel.isVisible());
  }

  private HTMLPanel formatMessage(String message, Date messageDate, boolean isFriend) {
    SafeHtmlBuilder safeHtmlBuilder = new SafeHtmlBuilder().appendEscapedLines(message);
    HTML htmlInline = new HTML(safeHtmlBuilder.toSafeHtml());
    htmlInline.addStyleName(resources.style().messageInner());
    htmlInline.addStyleName(isFriend ? resources.style().friendMessageInner() : resources.style().myMessageInner());
    DateTimeFormat timeFormat;
    DateTimeFormat dateFormat;
    Date md = new Date();
    md.setHours(0);
    HTML htmlTime;
    String time, date;
    boolean showDate;
    // формируем дату для отображения в диалоге. Если она до полуночи текущего дня, отображаем время,
    // иначе отображаем дату и время в тайтле
    if (messageDate.before(md)) {
      dateFormat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT);
      date = dateFormat.format(messageDate);
      timeFormat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.TIME_SHORT);
      time = timeFormat.format(messageDate);
      safeHtmlBuilder = new SafeHtmlBuilder().appendEscaped(date);
      htmlTime = new HTML(safeHtmlBuilder.toSafeHtml());
      htmlTime.setTitle(time);
      showDate = true;
    } else {
      timeFormat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.TIME_SHORT);
      time = timeFormat.format(messageDate);
      safeHtmlBuilder = new SafeHtmlBuilder().appendEscaped(time);
      htmlTime = new HTML(safeHtmlBuilder.toSafeHtml());
      showDate = false;
    }
    htmlTime.addStyleName(resources.style().messageTime());
    htmlTime.addStyleName(isFriend ? resources.style().friendMessageTime() : resources.style().myMessageTime());
    HTMLPanel html = new HTMLPanel("");
    if (isFriend) {
      if (message.length() < 35) {
        html.add(htmlInline);
        html.add(htmlTime);
      } else {
        Row row = new Row();
        row.getElement().getStyle().setMargin(-4, Style.Unit.PX);
        Column column;
        // уменьшаем поле для сообщений
        if (showDate) {
          column = new Column(ColumnSize.MD_8);
        } else {
          column = new Column(ColumnSize.MD_9);
        }
        column.getElement().getStyle().setPadding(4, Style.Unit.PX);
        htmlInline.getElement().getStyle().setMarginRight(0, Style.Unit.PX);
        column.add(htmlInline);
        row.add(column);
        // увеличиваем поле для времени
        if (showDate) {
          column = new Column(ColumnSize.MD_3);
        } else {
          column = new Column(ColumnSize.MD_2);
        }
        column.getElement().getStyle().setPadding(4, Style.Unit.PX);
        column.add(htmlTime);
        row.add(column);
        html.add(row);
      }
      html.addStyleName(resources.style().messageOuter());
    } else {
      Row row = new Row();
      row.getElement().getStyle().setMargin(4, Style.Unit.PX);
      Column column;
      // уменьшаем поле для сообщений
      if (showDate) {
        column = new Column(ColumnSize.MD_9);
      } else {
        column = new Column(ColumnSize.MD_10);
      }
      column.getElement().getStyle().setPadding(4, Style.Unit.PX);
      column.add(htmlInline);
      row.add(column);
      // увеличиваем поле для времени
      if (showDate) {
        column = new Column(ColumnSize.MD_3);
      } else {
        column = new Column(ColumnSize.MD_2);
      }
      column.getElement().getStyle().setPadding(4, Style.Unit.PX);
      column.add(htmlTime);
      row.add(column);
      html.add(row);
      html.addStyleName(resources.style().messageOuter());
    }
    return html;
  }

  @Override
  protected void onAttach() {
    super.onAttach();

    setPopupPositioner(new PopupPositioner() {
      @Override
      protected int getLeft(int popupWidth) {
        final Widget playViewWidget = playView.asWidget();
        return playViewWidget.getAbsoluteLeft() + playViewWidget.getOffsetWidth() - popupWidth;
      }

      @Override
      protected int getTop(int popupHeight) {
        return Window.getClientHeight() - popupHeight;
      }
    });

    fillSmileChoosePanel();
    fillLastUsedSmiles();
  }

  /**
   * Заполняем поле над сообщением последними смайлами
   */
  private void fillLastUsedSmiles() {
    // если пользователь только зашёл на сайт и еще не отправлял смайлы
    if (lastUsedSmilesQueue.isEmpty()) {
      final String smile = codes.get(0) + Integer.toHexString(10);
      final HTML smileHtml = new HTML("&#x" + smile + ";");
      int ch = Integer.parseInt(smile, 16);
      String sm = new String(Character.toChars(ch));
      final Button smileButton = createSmileButton(sm, smileHtml);
      smileButton.getElement().getStyle().setPaddingLeft(4, Style.Unit.PX);
      smileButton.getElement().getStyle().setPaddingRight(4, Style.Unit.PX);
      lastUsedSmiles.add(smileButton);
      lastUsedSmilesQueue.add(sm);
      cookies.setLastUsedSmiles(lastUsedSmilesQueue);
      return;
    }
    for (String s : lastUsedSmilesQueue) {
      Button smileButton = createSmileButton(s, new HTML(s));
      smileButton.getElement().getStyle().setPaddingLeft(4, Style.Unit.PX);
      smileButton.getElement().getStyle().setPaddingRight(4, Style.Unit.PX);
      lastUsedSmiles.add(smileButton);
    }
  }

  @Override
  public void setMessageFocus() {
    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
      @Override
      public void execute() {
        messengerMessage.setFocus(true);
      }
    });
  }

  @Override
  public void addMyMessage(String message, Date date) {
    if (StringUtils.isEmpty(message)) {
      return;
    }
    HTMLPanel html = formatMessage(message, date, false);
    messengerMessages.add(html);
    messengerMessagesScroll.scrollToBottom();
  }

  @Override
  public void addFriendMessage(String message, Date date) {
    if (StringUtils.isEmpty(message)) {
      return;
    }
    HTMLPanel html = formatMessage(message, date, true);
    messengerMessages.add(html);
    messengerMessagesScroll.scrollToBottom();
  }

  interface Binder extends UiBinder<PopupPanel, MessengerView> {
  }

  static class ViewFactoryImpl implements MessengerPresenter.ViewFactory {

    private final Binder uiBinder;
    private final EventBus eventBus;
    private final AppResources resources;
    private final Cookies cookies;

    @Inject
    public ViewFactoryImpl(Binder uiBinder,
                           EventBus eventBus,
                           AppResources resources,
                           Cookies cookies) {
      this.uiBinder = uiBinder;
      this.eventBus = eventBus;
      this.resources = resources;
      this.cookies = cookies;
    }

    @Override
    public MessengerPresenter.MyView create(PlayView playView) {
      return new MessengerView(uiBinder, eventBus, resources, cookies, playView);
    }
  }
}
