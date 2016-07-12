package online.draughts.rus.client.application.rules;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

import javax.inject.Inject;


public class RulesView extends ViewImpl implements RulesPresenter.MyView {
  @UiField
  SimplePanel main;

  @Inject
  RulesView(Binder uiBinder) {
    initWidget(uiBinder.createAndBindUi(this));

    bindSlot(RulesPresenter.SLOT_RULES, main);
  }

  interface Binder extends UiBinder<Widget, RulesView> {
  }
}
