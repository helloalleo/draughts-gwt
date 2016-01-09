
package online.draughts.rus.client.application.home;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import online.draughts.rus.client.application.widget.popup.DraughtsPlayerPresenter;
import online.draughts.rus.client.resources.AppResources;
import online.draughts.rus.client.util.TrUtils;
import online.draughts.rus.shared.dto.GameDto;
import online.draughts.rus.shared.locale.DraughtsMessages;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Image;

import java.util.Date;

public class PlayItem extends Composite {

  public static final String GAME_ID = "-game";
  private final DraughtsMessages messages;
  private final static String PLAYER_COLOR_DELIMITER = ": ";
  private final int gamesInRow;
  private DraughtsPlayerPresenter draughtsPlayer;

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
  @UiField
  HTMLPanel whoAndWhenDidWin;
  @UiField
  Anchor showGameAnchor;

  @Inject
  PlayItem(Binder binder,
           final DraughtsPlayerPresenter.Factory draughtsPlayerFactory,
           final HomePresenter homePresenter,
           DraughtsMessages messages,
           AppResources resources,
           @Assisted int gamesInRow,
           @Assisted final GameDto game) {
    this.messages = messages;
    initWidget(binder.createAndBindUi(this));

    this.gamesInRow = gamesInRow;
    panel.addStyleName(resources.style().playItem());
    setGame(homePresenter, draughtsPlayerFactory, game);
  }

  public void setGame(final HomePresenter homePresenter, final DraughtsPlayerPresenter.Factory draughtsPlayerFactory,
                      final GameDto game) {
    showGameAnchor.setId(game.getId() + GAME_ID);
    showGameAnchor.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        draughtsPlayer = draughtsPlayerFactory.create(game);
        homePresenter.addToPopupSlot(draughtsPlayer);
      }
    });
    if (game.getPlayEndStatus() != null) {
      whoDidWin.setHTML(TrUtils.translateEndGame(game.getPlayEndStatus()));
    }
    final Date playFinishDate = game.getPlayFinishDate();
    if (playFinishDate != null) {
      final DateTimeFormat dateTimeFromat;
      switch (gamesInRow) {
        case 2:
          dateTimeFromat = DateTimeFormat.getFormat("EEEE, dd MMMM yyyy, HH:mm:ss");
          break;
        case 4:
          dateTimeFromat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM);
          break;
        case 6:
          dateTimeFromat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT);
          break;
        default:
          dateTimeFromat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM);
          break;
      }
      String date = DateTimeFormat.getFormat(dateTimeFromat.getPattern())
          .format(playFinishDate);
      playEndDate.setHTML(date);
    }
    if (game.getEndGameScreenshot() != null) {
      endGameScreenshot.setUrl(game.getEndGameScreenshot());
      endGameScreenshot.setResponsive(true);
    }

    whitePlayerName.setHTML(messages.white() + PLAYER_COLOR_DELIMITER + game.getPlayerWhite().getPublicName());
    blackPlayerName.setHTML(messages.black() + PLAYER_COLOR_DELIMITER + game.getPlayerBlack().getPublicName());
  }

  interface Binder extends UiBinder<HTMLPanel, PlayItem> {
  }
}
