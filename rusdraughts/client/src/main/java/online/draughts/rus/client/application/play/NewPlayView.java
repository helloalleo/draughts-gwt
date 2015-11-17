package online.draughts.rus.client.application.play;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import javax.inject.Inject;


public class NewPlayView extends ViewWithUiHandlers<NewPlayUiHandlers> implements NewPlayPresenter.MyView {
  @UiField
  SimplePanel main;

  @Inject
  NewPlayView(Binder uiBinder) {
    initWidget(uiBinder.createAndBindUi(this));

    bindSlot(NewPlayPresenter.SLOT_NEWPLAY, main);
  }

  interface Binder extends UiBinder<Widget, NewPlayView> {
  }
}
