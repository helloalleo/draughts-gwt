package online.shashki.rus.client.application.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;


public class OAuthLoginView extends ViewWithUiHandlers<OAuthLoginUiHandlers> implements OAuthLoginPresenter.MyView {

  @UiField
  HTMLPanel loginContainer;

  @Inject
  OAuthLoginView(Binder uiBinder) {
    initWidget(uiBinder.createAndBindUi(this));

    GWT.runAsync(new RunAsyncCallback() {
      @Override
      public void onFailure(Throwable reason) {

      }

      @Override
      public void onSuccess() {
        verticalAlignEnterBox();
      }
    });
  }

  private void verticalAlignEnterBox() {
    int windowHeight = Window.getClientHeight();
    int loginHeight = loginContainer.getOffsetHeight();
    double containerMarginTop = windowHeight / 2 - loginHeight - 100;
    loginContainer.getElement().getStyle().setMarginTop(containerMarginTop, Style.Unit.PX);
  }

  interface Binder extends UiBinder<Widget, OAuthLoginView> {
  }
}
