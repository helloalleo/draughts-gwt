package online.draughts.rus.client.application.love;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

import javax.inject.Inject;


public class LoveView extends ViewImpl implements LovePresenter.MyView {
  @UiField
  SimplePanel main;

  @Inject
  LoveView(Binder uiBinder) {
    initWidget(uiBinder.createAndBindUi(this));

    bindSlot(LovePresenter.SLOT_LOVE, main);
  }

  interface Binder extends UiBinder<Widget, LoveView> {
  }
}
