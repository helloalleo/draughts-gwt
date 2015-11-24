package online.draughts.rus.client.application.learn;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

import javax.inject.Inject;


public class LearnView extends ViewImpl implements LearnPresenter.MyView {

  @Inject
  LearnView(Binder uiBinder) {
    initWidget(uiBinder.createAndBindUi(this));

    bindSlot(LearnPresenter.SLOT_LEARNRUSSIANDRAUGHTS, main);
  }

  @UiField
  SimplePanel main;

  interface Binder extends UiBinder<Widget, LearnView> {
  }
}
