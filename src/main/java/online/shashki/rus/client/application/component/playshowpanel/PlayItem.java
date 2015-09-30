
package online.shashki.rus.client.application.component.playshowpanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import online.shashki.rus.shared.model.Game;

public class PlayItem extends Composite {

  private static Binder binder = GWT.create(Binder.class);

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

  PlayItem() {
    initWidget(binder.createAndBindUi(this));
  }

  public void setGame(Game game) {
    whitePlayerName.setHTML(game.getPlayerWhite().getPlayerName());
    blackPlayerName.setHTML(game.getPlayerBlack().getPlayerName());
    whoDidWin.setHTML(game.getPlayEndStatus().name());
    playEndDate.setHTML(game.getPlayEndDate().toString());
  }

  interface Binder extends UiBinder<HTMLPanel, PlayItem> {
  }
}
