
package online.shashki.rus.client.application.component.playitem;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import online.shashki.rus.shared.model.Game;

public class PlayItemView extends ViewWithUiHandlers<PlayItemUiHandlers> implements PlayItemPresenter.MyView {
  @UiField
  HTMLPanel panel;
  @UiField
  HTML whitePlayerName;
  @UiField
  HTML blackPlayerName;
  @UiField
  HTML whoDidWin;
  @UiField
  Image endGameScreenshot;
  @UiField
  HTML playEndDate;

  @Inject
  PlayItemView(Binder binder) {
    initWidget(binder.createAndBindUi(this));
  }

  @Override
  public void setGame(Game game) {
    whitePlayerName.setHTML(game.getPlayerWhite().getPlayerName());
    blackPlayerName.setHTML(game.getPlayerBlack().getPlayerName());
    whoDidWin.setHTML(game.getPlayEndStatus().name());
    playEndDate.setHTML(game.getPlayEndDate().toString());
  }

  interface Binder extends UiBinder<HTMLPanel, PlayItemView> {
  }
}
