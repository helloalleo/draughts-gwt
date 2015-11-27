
package online.draughts.rus.client.application.play.messanger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
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
import online.draughts.rus.shared.util.StringUtils;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;

import java.util.Date;

public class MessengerView extends PopupViewWithUiHandlers<MessengerUiHandlers> implements MessengerPresenter.MyView {
  private final PlayView playView;
  private final AppResources resources;
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

  @Inject
  public MessengerView(Binder uiBinder,
                EventBus eventBus,
                AppResources resources,
                PlayView playView) {
    super(eventBus);

    initWidget(uiBinder.createAndBindUi(this));

    asPopupPanel().setAnimationEnabled(true);

    this.playView = playView;
    this.resources = resources;
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

  private HTMLPanel formatMessage(String message, Date date, boolean isFriend) {
    SafeHtmlBuilder safeHtmlBuilder = new SafeHtmlBuilder().appendEscapedLines(message);
    HTML htmlInline = new HTML(safeHtmlBuilder.toSafeHtml());
    htmlInline.addStyleName(resources.style().messageInner());
    htmlInline.addStyleName(isFriend ? resources.style().friendMessageInner() : resources.style().myMessageInner());
    DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.TIME_SHORT);
    safeHtmlBuilder = new SafeHtmlBuilder().appendEscaped(dateTimeFormat.format(date));
    HTML htmlTime = new HTML(safeHtmlBuilder.toSafeHtml());
    htmlTime.addStyleName(resources.style().messageTime());
    htmlTime.addStyleName(isFriend ? resources.style().friendMessageTime() : resources.style().myMessageTime());
    HTMLPanel html = new HTMLPanel("");
    if (isFriend) {
      html.add(htmlInline);
      html.add(htmlTime);
      html.addStyleName(resources.style().messageOuter());
    } else {
      Row row = new Row();
      row.getElement().getStyle().setMargin(4, Style.Unit.PX);
      Column column = new Column(ColumnSize.MD_10);
      column.getElement().getStyle().setPadding(4, Style.Unit.PX);
      column.add(htmlInline);
      row.add(column);
      column = new Column(ColumnSize.MD_2);
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

  public static class ViewFactoryImpl implements MessengerPresenter.ViewFactory {

    private final Binder uiBinder;
    private final EventBus eventBus;
    private final AppResources resources;

    @Inject
    public ViewFactoryImpl(Binder uiBinder,
                           EventBus eventBus,
                           AppResources resources) {
      this.uiBinder = uiBinder;
      this.eventBus = eventBus;
      this.resources = resources;
    }

    @Override
    public MessengerPresenter.MyView create(PlayView playView) {
      return new MessengerView(uiBinder, eventBus, resources, playView);
    }
  }
}
