package online.draughts.rus.client.application.mygame;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import javax.inject.Inject;


public class MyGameView extends ViewWithUiHandlers<MyGameUiHandlers> implements MyGamePresenter.MyView {
  @UiField
  SimplePanel main;

  @Inject
  MyGameView(Binder uiBinder) {
    initWidget(uiBinder.createAndBindUi(this));

    bindSlot(MyGamePresenter.SLOT_MYGAME, main);
  }

  interface Binder extends UiBinder<Widget, MyGameView> {
  }
}
