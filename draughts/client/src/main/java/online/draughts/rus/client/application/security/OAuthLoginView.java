package online.draughts.rus.client.application.security;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import online.draughts.rus.client.resources.Variables;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.constants.IconSize;


public class OAuthLoginView extends ViewWithUiHandlers<OAuthLoginUiHandlers> implements OAuthLoginPresenter.MyView {

  @UiField
  HTMLPanel loginContainer;
  @UiField
  Row loginRow;
  @UiField
  HTMLPanel marginPanel;
  @UiField
  Icon signIn;
  @UiField
  Anchor vkSignIn;
  @UiField
  Anchor fbSignIn;
  @UiField
  Anchor gSignIn;

  @Inject
  OAuthLoginView(Binder uiBinder) {
    initWidget(uiBinder.createAndBindUi(this));
  }

  @Override
  protected void onAttach() {
    verticalAlignEnterBox();
  }

  private void verticalAlignEnterBox() {
    loginContainer.setHeight(Window.getClientHeight() - Variables.marginTop() - Variables.footerHeight() + "px");
    double containerMarginTop = loginContainer.getOffsetHeight() / 3;
    marginPanel.setHeight(containerMarginTop + "px");

    if (Window.getClientWidth() < 768) {
      signIn.setSize(IconSize.TIMES3);
      vkSignIn.setIconSize(IconSize.TIMES3);
      fbSignIn.setIconSize(IconSize.TIMES3);
      gSignIn.setIconSize(IconSize.TIMES3);
    }
  }

  interface Binder extends UiBinder<Widget, OAuthLoginView> {
  }
}
