
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
import online.shashki.rus.shared.model.Player;
import org.gwtbootstrap3.client.ui.Image;

public class PlayItem extends Composite {

  private static Binder binder = GWT.create(Binder.class);
  private final ShashkiMessages messages = GWT.create(ShashkiMessages.class);
  private final AppResources resources = GWT.create(AppResources.class);
  private final String PLAYER_COLOR_DELIMITER = ": ";

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

  PlayItem(final Game game) {
    initWidget(binder.createAndBindUi(this));

    panel.addStyleName(resources.style().playItem());

//    PlayerService.App.getInstance().getCurrentProfile(new AsyncCallback<Player>() {
//      @Override
//      public void onFailure(Throwable caught) {
//        ErrorDialogBox.setMessage(caught).show();
//      }
//
//      @Override
//      public void onSuccess(Player result) {
//        setGame(result, game);
//      }
//    });
  }

  public void setGame(Player player, Game game) {
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

    if (player != null
        && (player.getId().equals(game.getPlayerBlack().getId())
        || player.getId().equals(game.getPlayerWhite().getId()))) {
      whitePlayerName.setHTML(messages.white() + PLAYER_COLOR_DELIMITER + game.getPlayerWhite().getPublicName());
      blackPlayerName.setHTML(messages.black() + PLAYER_COLOR_DELIMITER + game.getPlayerBlack().getPublicName());
    }
  }

  interface Binder extends UiBinder<HTMLPanel, PlayItem> {
  }
}
