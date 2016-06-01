package online.draughts.rus.client.application.game;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import javax.inject.Inject;


public class GameView extends ViewWithUiHandlers<GameUiHandlers> implements GamePresenter.MyView {
  @UiField
  HTMLPanel main;
  @UiField
  HTMLPanel player;

  @Inject
  GameView(Binder uiBinder) {
    initWidget(uiBinder.createAndBindUi(this));

    bindSlot(GamePresenter.SLOT_GAME, main);
    bindSlot(GamePresenter.SLOT_PLAYER, player);
  }

  interface Binder extends UiBinder<Widget, GameView> {
  }
}
