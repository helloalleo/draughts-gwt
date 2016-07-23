package online.draughts.rus.client.application.coach;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import javax.inject.Inject;


public class CoachRoomView extends ViewWithUiHandlers<CoachRoomUiHandlers> implements CoachRoomPresenter.MyView {
  interface Binder extends UiBinder<Widget, CoachRoomView> {
  }

  @UiField
  SimplePanel main;

  @Inject
  CoachRoomView(Binder uiBinder) {
    initWidget(uiBinder.createAndBindUi(this));

    bindSlot(CoachRoomPresenter.SLOT_COACHROOM, main);
  }
}
