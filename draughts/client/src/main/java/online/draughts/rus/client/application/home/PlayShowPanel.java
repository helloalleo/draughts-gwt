
package online.draughts.rus.client.application.home;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import online.draughts.rus.client.application.widget.dialog.InfoDialogBox;
import online.draughts.rus.client.gin.PlayShowPanelFactory;
import online.draughts.rus.client.resources.Variables;
import online.draughts.rus.client.util.AbstractAsyncCallback;
import online.draughts.rus.client.util.Cookies;
import online.draughts.rus.shared.config.ClientConfiguration;
import online.draughts.rus.shared.dto.GameDto;
import online.draughts.rus.shared.locale.DraughtsMessages;
import online.draughts.rus.shared.resource.GamesResource;
import online.draughts.rus.shared.util.StringUtils;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.html.Hr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayShowPanel extends Composite {

  private final Provider<HomeView> homeViewProvider;
  private final PlayShowPanelFactory showPanelFactory;
  private final Cookies cookies;
  private final PlaceManager placeManager;
  private final ResourceDelegate<GamesResource> gamesDelegate;
  private final DraughtsMessages messages;

  @UiField
  HTMLPanel playRowList;

  static final int[] GAMES_ON_PAGE = {1, 2, 3, 4, 6};
  private int gamesOnPanelCounter = 1;
  private List<GameDto> gameList;

  private int INCREMENT_SIZE;

  private boolean updateFlag = true;

  private int lastMaxHeight = 0;
  private int lastScrollPos = 0;
  private boolean blockScroll;
  private List<GameDto> showedGames = new ArrayList<>();

  @Inject
  public PlayShowPanel(Binder binder,
                       PlaceManager placeManager,
                       ClientConfiguration config,
                       PlayShowPanelFactory showPanelFactory,
                       Provider<HomeView> homeViewProvider,
                       DraughtsMessages messages,
                       ResourceDelegate<GamesResource> gamesDelegate,
                       Cookies cookies) {
    this.showPanelFactory = showPanelFactory;
    this.cookies = cookies;
    this.placeManager = placeManager;
    INCREMENT_SIZE = HomePresenter.getIncrementPlaysOnPage(config, cookies);
    this.homeViewProvider = homeViewProvider;
    this.messages = messages;
    this.gamesDelegate = gamesDelegate;

    initWidget(binder.createAndBindUi(this));
  }

  void init() {
    initScroll();
    initShowPlay();
  }

  private void initShowPlay() {
    blockScroll = false;
    String showGame = placeManager.getCurrentPlaceRequest().getParameter("game", "");
    if (StringUtils.isNotEmpty(showGame)) {
      final Long id = Long.valueOf(showGame);
      if (id == null) {
        InfoDialogBox.setMessage(messages.gameNotFound()).show();
      } else {
        gamesDelegate.withCallback(
            new AbstractAsyncCallback<GameDto>() {
              @Override
              public void onSuccess(GameDto result) {
                if (result == null) {
                  InfoDialogBox.setMessage(messages.gameNotFound()).show();
                  return;
                }
                resetGames(Collections.singletonList(result));
                blockScroll = true;
              }
            }
        ).game(id);
      }
    }
  }

  private HandlerRegistration initScroll() {
    return Window.addWindowScrollHandler(new Window.ScrollHandler() {
      @Override
      public void onWindowScroll(Window.ScrollEvent event) {
        if (blockScroll) {
          return;
        }
        int oldScrollPos = lastScrollPos;
        lastScrollPos = Window.getScrollTop();
        // если листаем вверх
        if (oldScrollPos >= lastScrollPos) {
          return;
        }

        int maxScrollTop = getOffsetHeight() - Window.getScrollTop();
        if (lastScrollPos >= maxScrollTop && updateFlag) {
          final int newPageSize = INCREMENT_SIZE;
          PlayShowPanel.this.homeViewProvider.get().getMoreGames(newPageSize);
          lastMaxHeight = maxScrollTop;
          updateFlag = false;
        }
        if (maxScrollTop > lastMaxHeight) {
          updateFlag = true;
        }
      }
    });
  }

  int setGames(List<GameDto> games) {
    blockScroll = false;
    this.gameList = games;
    resetGames(games);
    return games.size();
  }

  private void resetGames(List<GameDto> gameList) {
    lastMaxHeight = 0;
    lastScrollPos = 0;
    resetGamesOnPanel(gameList);
  }

  int addGames(List<GameDto> gameList) {
    return addGamesToPanel(gameList);
  }

  private int addGamesToPanel(List<GameDto> gameList) {
    if (gameList == null || gameList.isEmpty()) {
      return 0;
    }

    final int gamesInRow = GAMES_ON_PAGE[gamesOnPanelCounter];
    int filledGamesInRow = this.gameList.size() % gamesInRow;
    int offsetRow = 0;
    // в начале дополняем существующую строку
    if (filledGamesInRow != 0) {
      Row lastRow = (Row) playRowList.getWidget(playRowList.getWidgetCount() - 2);
      for (int i = 0; i < gamesInRow - filledGamesInRow; i++) {
        final GameDto gameDto = gameList.get(i);
        if (showedGames.contains(gameDto)) {
          continue;
        }

        Column column = new Column(getSize(gamesInRow));
        final PlayItem item = showPanelFactory.createItem(gamesInRow, gameDto);
        showedGames.add(gameDto);
        column.add(item);
        lastRow.add(column);

        if (lastRow.getWidgetCount() == gamesInRow && i != gamesInRow - filledGamesInRow - 1) {
          playRowList.add(new Hr());
          lastRow = new Row();
          playRowList.add(lastRow);
        }
        offsetRow++;
      }
    }
    // затем дополняем новыми полными строками
    List<GameDto> rowGameList = new ArrayList<>();
    for (int i = offsetRow; i < gameList.size(); i++) {
      if (0 == (rowGameList.size() + 1) % (gamesInRow)) {
        rowGameList.add(gameList.get(i));
        addGameRow(rowGameList, gamesInRow);
        rowGameList.clear();
      } else {
        rowGameList.add(gameList.get(i));
      }
    }
    // дополняем оставшуюся строку
    if (!rowGameList.isEmpty()) {
      addGameRow(rowGameList, gamesInRow);
    }

    this.gameList.addAll(gameList);
    return this.gameList.size();
  }

  private String getSize(int gamesInRow) {
    return "MD_" + Variables.COLUMNS_IN_LAYOUT / gamesInRow
        + " " + "SM_" + Variables.COLUMNS_IN_LAYOUT / gamesInRow
        + " " + "XS_" + Variables.COLUMNS_IN_LAYOUT / gamesInRow;
  }

  private void resetGamesOnPanel(List<GameDto> gameList) {
    showedGames.clear();
    if (gameList == null || gameList.isEmpty()) {
      return;
    }
    gamesOnPanelCounter = cookies.getGamesOnPageCounter();
    if (gamesOnPanelCounter <= 0) {
      homeViewProvider.get().setEnableLessGameButton(false);
    }
    if (gamesOnPanelCounter >= 4) {
      homeViewProvider.get().setEnableMoreGameButton(false);
    }
    playRowList.clear();
    List<GameDto> rowGameList = new ArrayList<>();
    final int gameInRow = GAMES_ON_PAGE[gamesOnPanelCounter];
    for (int i = 0; i < gameList.size(); i++) {
      if ((i + 1) % gameInRow == 0) {
        rowGameList.add(gameList.get(i));
        addGameRow(rowGameList, gameInRow);
        rowGameList.clear();
      } else {
        rowGameList.add(gameList.get(i));
      }
    }
    if (!rowGameList.isEmpty()) {
      addGameRow(rowGameList, gameInRow);
    }
  }

  private void addGameRow(List<GameDto> rowGameList, int gamesInRow) {
    Row row = new Row();
    if (gamesInRow == 1) {
      gamesInRow = 2;
    }
    for (GameDto game : rowGameList) {
      if (showedGames.contains(game)) {
        continue;
      }
      Column column = new Column(getSize(gamesInRow));
      final PlayItem item = showPanelFactory.createItem(gamesInRow, game);
      showedGames.add(game);
      column.add(item);
      row.add(column);
    }
    if (row.getWidgetCount() > 0) {
      playRowList.add(row);
      playRowList.add(new Hr());
    }
  }

  void moreGameOnPanel() {
    gamesOnPanelCounter++;
    cookies.setGamesOnPageCounter(gamesOnPanelCounter);
    if (!homeViewProvider.get().isEnabledLessGameButton()) {
      homeViewProvider.get().setEnableLessGameButton(true);
    }
    resetGames(gameList);
    if (gamesOnPanelCounter == GAMES_ON_PAGE.length - 1) {
      homeViewProvider.get().setEnableMoreGameButton(false);
    }
  }

  void lessGameOnPanel() {
    gamesOnPanelCounter--;
    cookies.setGamesOnPageCounter(gamesOnPanelCounter);
    if (!homeViewProvider.get().isEnabledMoreGameButton()) {
      homeViewProvider.get().setEnableMoreGameButton(true);
    }
    resetGames(gameList);
    if (gamesOnPanelCounter == 0) {
      homeViewProvider.get().setEnableLessGameButton(false);
    }
  }

  public interface Binder extends UiBinder<HTMLPanel, PlayShowPanel> {
  }
}
