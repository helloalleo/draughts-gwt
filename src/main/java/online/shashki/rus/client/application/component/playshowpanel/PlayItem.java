
package online.shashki.rus.client.application.component.playshowpanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
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

  PlayItem(Game game) {
    initWidget(binder.createAndBindUi(this));

    setGame(game);
  }

  public void setGame(Game game) {
    if (game.getPlayerWhite() != null) {
      whitePlayerName.setHTML(game.getPlayerWhite().getPlayerName());
    }
    if (game.getPlayerBlack() != null) {
      blackPlayerName.setHTML(game.getPlayerBlack().getPlayerName());
    }
    if (game.getPlayEndStatus() != null) {
      whoDidWin.setHTML(game.getPlayEndStatus().name());
    }
    if (game.getPlayEndDate() != null) {
      String date = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM)
          .format(game.getPlayEndDate());
      playEndDate.setHTML(date);
    }
  }

  interface Binder extends UiBinder<HTMLPanel, PlayItem> {
  }
}
