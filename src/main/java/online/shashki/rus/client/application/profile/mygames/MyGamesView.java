package online.shashki.rus.client.application.profile.mygames;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;


public class MyGamesView extends ViewWithUiHandlers<MyGamesUiHandlers> implements MyGamesPresenter.MyView {
  @UiField
  SimplePanel main;

  @Inject
  MyGamesView(Binder uiBinder) {
    initWidget(uiBinder.createAndBindUi(this));

    bindSlot(MyGamesPresenter.SLOT_MYGAMES, main);
  }

  interface Binder extends UiBinder<Widget, MyGamesView> {
  }
}
