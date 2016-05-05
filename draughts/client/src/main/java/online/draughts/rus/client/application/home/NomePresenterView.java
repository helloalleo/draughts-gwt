package online.draughts.rus.client.application.home;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import online.draughts.rus.client.util.Logger;
import org.gwtbootstrap3.client.ui.CheckBoxButton;

import javax.inject.Inject;


public class NomePresenterView extends ViewWithUiHandlers<NomePresenterUiHandlers> implements NomePresenterPresenter.MyView {
  @UiField
  HTMLPanel main;
  @UiField
  CheckBoxButton myGameListCheckButton;
  @Inject
  NomePresenterView(Binder uiBinder) {
    initWidget(uiBinder.createAndBindUi(this));

    bindSlot(NomePresenterPresenter.SLOT_NOMEPRESENTER, main);
  }

  @UiHandler("myGameListCheckButton")
  public void test(ClickEvent clickEvent) {
    Logger.debug(myGameListCheckButton.getValue().toString());
  }

  interface Binder extends UiBinder<Widget, NomePresenterView> {
  }
}
