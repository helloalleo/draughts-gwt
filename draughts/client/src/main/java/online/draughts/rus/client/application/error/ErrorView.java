package online.draughts.rus.client.application.error;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import online.draughts.rus.client.resources.Variables;

public class ErrorView extends ViewImpl implements ErrorPresenter.MyView {
  @UiField
  SimplePanel main;

  @Inject
  ErrorView(Binder uiBinder) {
    initWidget(uiBinder.createAndBindUi(this));

    bindSlot(ErrorPresenter.SLOT_ERROR, main);
  }

  @Override
  protected void onAttach() {
    main.setHeight(Window.getClientHeight() - Variables.marginTop() - Variables.footerHeight() + "px");
  }

  interface Binder extends UiBinder<Widget, ErrorView> {
  }
}
