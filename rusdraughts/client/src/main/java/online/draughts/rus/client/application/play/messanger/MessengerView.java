
package online.draughts.rus.client.application.play.messanger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupViewWithUiHandlers;
import com.gwtplatform.mvp.client.view.PopupPositioner;
import online.draughts.rus.client.application.play.PlayView;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.IconType;

public class MessengerView extends PopupViewWithUiHandlers<MessengerUiHandlers> implements MessengerPresenter.MyView {
  private final PlayView playView;
  @UiField
  Button collapseMessengerButton;
  @UiField
  PanelHeader messengerHeader;
  @UiField
  PanelBody messengerBody;
  @UiField
  Heading messengerHeading;

  @Inject
  MessengerView(Binder uiBinder, EventBus eventBus,
                final PlayView playView) {
    super(eventBus);

    initWidget(uiBinder.createAndBindUi(this));

    this.playView = playView;
  }

  @UiHandler("collapseMessengerButton")
  public void onCollapseMessenger(ClickEvent clickEvent) {
    if (collapseMessengerButton.getIcon().equals(IconType.CLOSE)) {
      messengerBody.setVisible(false);
      collapseMessengerButton.setIcon(IconType.SQUARE_O);
      ((Column) messengerHeading.getParent()).setSize("MD_8");
      ((Column) collapseMessengerButton.getParent()).setSize("MD_4");
    } else {
      messengerBody.setVisible(true);
      collapseMessengerButton.setIcon(IconType.CLOSE);
      ((Column) messengerHeading.getParent()).setSize("MD_10");
      ((Column) collapseMessengerButton.getParent()).setSize("MD_2");
    }
    showAndReposition();
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

  interface Binder extends UiBinder<PopupPanel, MessengerView> {
  }
}
