
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
           DraughtsMessages messages,
           AppResources resources,
           @Assisted int gamesInRow,
           @Assisted final GameDto game, ClientConfiguration config) {
    this.messages = messages;
    this.config = config;
    initWidget(binder.createAndBindUi(this));
    this.gamesInRow = gamesInRow;
    panel.addStyleName(resources.style().playItem());
    setGame(homePresenter, draughtsPlayerFactory, game);

    if (Window.getClientWidth() < 768) {
      whoAndWhenWon.setVisible(false);
      whoPlayed.setVisible(false);
    }
  }

  private void setGame(final HomePresenter homePresenter, final DraughtsPlayerPresenter.Factory draughtsPlayerFactory,
                       final GameDto game) {
    playGameAnchor.setId(game.getId() + GAME_ID);
    playGameAnchor.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        showGame(draughtsPlayerFactory, game, homePresenter);
      }
    });

    final String whitePlayerName = messages.white() + PLAYER_COLOR_DELIMITER + game.getPlayerWhite().getPublicName();
    final String blackPlayerName = messages.black() + PLAYER_COLOR_DELIMITER + game.getPlayerBlack().getPublicName();
    SpanElement script = getVkShareScript(game.getId(), whitePlayerName, blackPlayerName);
    shareVkButton.getElement().appendChild(script);

    linkGameAnchor.setHref(config.linkToGame(String.valueOf(game.getId())));
    if (game.getPlayEndStatus() != null) {
      whoDidWin.setHTML(messages.whoHadWon(TrUtils.translateGameType(game.getGameType()),
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
    if (game.getEndGameScreenshot() != null) {
      endGameScreenshot.setUrl(game.getEndGameScreenshot());
      endGameScreenshot.setResponsive(true);
      endGameScreenshot.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
          showGame(draughtsPlayerFactory, game, homePresenter);
        }
      });
      endGameScreenshot.getElement().getStyle().setCursor(Style.Cursor.POINTER);
    }

    this.whitePlayerName.setHTML(whitePlayerName);
    this.blackPlayerName.setHTML(blackPlayerName);
  }

  private SpanElement getVkShareScript(Long gameId, String whitePlayerName, String blackPlayerName) {
    Document doc = Document.get();
    SpanElement vkShare = doc.createSpanElement();
    vkShare.setInnerHTML("<a href='#' onclick=\"Share.vkontakte('https://shashki.online/shashki/#!/game?id=" + gameId + "'," +
        "'Мне понравилась игра','https://shashki.online/logo.png','" + whitePlayerName + "; " + blackPlayerName + "')\">" +
        "<i class='fa fa-vk fa-2x'></i></a>");
    return vkShare;
  }

  private void showGame(DraughtsPlayerPresenter.Factory draughtsPlayerFactory, GameDto game, HomePresenter homePresenter) {
    DraughtsPlayerPresenter draughtsPlayer = draughtsPlayerFactory.create(game);
    homePresenter.addToPopupSlot(draughtsPlayer);
  }

  interface Binder extends UiBinder<HTMLPanel, PlayItem> {
  }
}
