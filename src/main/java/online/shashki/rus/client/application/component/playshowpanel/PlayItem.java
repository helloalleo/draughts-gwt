
package online.shashki.rus.client.application.component.playshowpanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import online.shashki.rus.client.resources.AppResources;
import online.shashki.rus.client.utils.TrUtils;
import online.shashki.rus.shared.locale.ShashkiMessages;
import online.shashki.rus.shared.model.Game;
import org.gwtbootstrap3.client.ui.Image;

public class PlayItem extends Composite {

  private static Binder binder = GWT.create(Binder.class);
  private final ShashkiMessages messages = GWT.create(ShashkiMessages.class);
  private final AppResources resources = GWT.create(AppResources.class);

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

    panel.addStyleName(resources.style().playItem());
    setGame(game);
  }

  public void setGame(Game game) {
    if (game.getPlayEndStatus() != null) {
      whoDidWin.setHTML(TrUtils.translateEndGame(game.getPlayEndStatus()));
    }
    if (game.getPlayFinishDate() != null) {
      String date = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM)
          .format(game.getPlayFinishDate());
      playEndDate.setHTML(date);
    }
    if (game.getEndGameScreenshot() != null) {
      endGameScreenshot.setUrl(game.getEndGameScreenshot());
      endGameScreenshot.setResponsive(true);
    }
  }

  interface Binder extends UiBinder<HTMLPanel, PlayItem> {
  }
}
