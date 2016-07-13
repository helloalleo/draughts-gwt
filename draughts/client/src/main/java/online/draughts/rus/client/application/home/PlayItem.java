
package online.draughts.rus.client.application.home;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import online.draughts.rus.client.application.mygames.MyGamesPresenter;
import online.draughts.rus.client.application.widget.popup.DraughtsPlayerPresenter;
import online.draughts.rus.client.resources.AppResources;
import online.draughts.rus.client.util.TrUtils;
import online.draughts.rus.shared.config.ClientConfiguration;
import online.draughts.rus.shared.dto.GameDto;
import online.draughts.rus.shared.locale.DraughtsMessages;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Image;

import java.util.Date;

public class PlayItem extends Composite {

  private static final String GAME_ID = "-game";
  private final DraughtsMessages messages;
  private final ClientConfiguration config;
  private final static String PLAYER_COLOR_DELIMITER = ": ";
  private final int gamesInRow;
  private final ShowPanelEnum showPanelEnum;

  @UiField
  HTMLPanel panel;
  @UiField
  HTML whitePlayerName;
  @UiField
  HTML blackPlayerName;
  @UiField
  HTML whoWon;
  @UiField
  Image endGameScreenshot;
  @UiField
  HTML playEndDate;
  @UiField
  HTMLPanel whoAndWhenWon;
  @UiField
  Anchor playGameAnchor;
  @UiField
  Anchor linkGameAnchor;
  @UiField
  HTMLPanel whoPlayed;
  @UiField
  HTMLPanel shareVkButton;

  @Inject
  PlayItem(Binder binder,
           final DraughtsPlayerPresenter.Factory draughtsPlayerFactory,
           final HomePresenter homePresenter,
           final MyGamesPresenter myGamesPresenter,
           DraughtsMessages messages,
           AppResources resources,
           ClientConfiguration config,
           @Assisted int gamesInRow,
           @Assisted final GameDto game,
           @Assisted ShowPanelEnum showPanelEnum) {
    this.messages = messages;
    this.config = config;
    initWidget(binder.createAndBindUi(this));
    this.gamesInRow = gamesInRow;
    this.showPanelEnum = showPanelEnum;
    panel.addStyleName(resources.style().playItem());
    setGame(homePresenter, myGamesPresenter, draughtsPlayerFactory, game);

    if (Window.getClientWidth() < 768) {
      whoAndWhenWon.setVisible(false);
      whoPlayed.setVisible(false);
    }
  }

  private void setGame(final HomePresenter homePresenter, final MyGamesPresenter myGamesPresenter,
                       final DraughtsPlayerPresenter.Factory draughtsPlayerFactory,
                       final GameDto game) {
    playGameAnchor.setId(game.getId() + GAME_ID);
    playGameAnchor.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        showGame(draughtsPlayerFactory, game, homePresenter, myGamesPresenter);
      }
    });

    final String whitePlayerName = messages.white() + PLAYER_COLOR_DELIMITER + game.getPlayerWhite().getPublicName();
    final String blackPlayerName = messages.black() + PLAYER_COLOR_DELIMITER + game.getPlayerBlack().getPublicName();
    SpanElement script = getVkShareScript(game.getId(), game.getEndGameScreenshotFullUrl(config.draughtsOnlineBucket()),
        whitePlayerName, blackPlayerName);
    shareVkButton.getElement().appendChild(script);

    linkGameAnchor.setHref(config.linkToGame(String.valueOf(game.getId())));
    if (game.getPlayEndStatus() != null) {
      whoWon.setHTML(messages.whoWon(TrUtils.translateGameType(game.getGameType()),
          TrUtils.translateEndGame(game.getPlayEndStatus())));
    }
    final Date playFinishDate = game.getPlayFinishDate();
    if (playFinishDate != null) {
      final DateTimeFormat dateTimeFormat;
      switch (gamesInRow) {
        case 2:
          dateTimeFormat = DateTimeFormat.getFormat("EEEE, dd MMMM yyyy, HH:mm:ss");
          break;
        case 4:
          dateTimeFormat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM);
          break;
        case 6:
          dateTimeFormat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT);
          break;
        default:
          dateTimeFormat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM);
          break;
      }
      String date = DateTimeFormat.getFormat(dateTimeFormat.getPattern())
          .format(playFinishDate);
      playEndDate.setHTML(date);
    }
    if (game.getEndGameScreenshotUrl() != null) {
      endGameScreenshot.setUrl(game.getEndGameScreenshotFullUrl(config.draughtsOnlineBucket()));
    } else if (game.getEndGameScreenshot() != null) {
      endGameScreenshot.setUrl(game.getEndGameScreenshot());
    }
    endGameScreenshot.setResponsive(true);
    endGameScreenshot.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        showGame(draughtsPlayerFactory, game, homePresenter, myGamesPresenter);
      }
    });
    endGameScreenshot.getElement().getStyle().setCursor(Style.Cursor.POINTER);

    this.whitePlayerName.setHTML(whitePlayerName);
    this.blackPlayerName.setHTML(blackPlayerName);
  }

  private SpanElement getVkShareScript(Long gameId, String screenshotUrl, String whitePlayerName, String blackPlayerName) {
    Document doc = Document.get();
    SpanElement vkShare = doc.createSpanElement();
    vkShare.setInnerHTML("<a href='#' onclick=\"Share.vkontakte('https://shashki.online/shashki/#!/game?id=" + gameId + "'," +
        "'Мне понравилась игра','"+ screenshotUrl +"','" + whitePlayerName + "; " + blackPlayerName + "')\">" +
        "<i class='fa fa-vk fa-2x'></i></a>");
    return vkShare;
  }

  private void showGame(DraughtsPlayerPresenter.Factory draughtsPlayerFactory, GameDto game,
                        HomePresenter homePresenter, MyGamesPresenter myGamesPresenter) {
    DraughtsPlayerPresenter draughtsPlayer = draughtsPlayerFactory.create(game);
    switch (showPanelEnum) {
      case HOME_PANEL:
        homePresenter.addToPopupSlot(draughtsPlayer);
        break;
      case MY_GAMES_PANE:
        myGamesPresenter.addToPopupSlot(draughtsPlayer);
        break;
    }
  }

  interface Binder extends UiBinder<HTMLPanel, PlayItem> {
  }
}
