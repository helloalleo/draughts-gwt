
package online.draughts.rus.client.application.component.playshowpanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import online.draughts.rus.client.application.home.HomeView;
import online.draughts.rus.client.gin.PlayShowPanelFactory;
import online.draughts.rus.client.resources.Variables;
import online.draughts.rus.client.util.DTCookies;
import online.draughts.rus.client.util.DTLog;
import online.draughts.rus.shared.config.ClientConfiguration;
import online.draughts.rus.shared.model.Game;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.html.Hr;

import java.util.ArrayList;
import java.util.List;

public class PlayShowPanel extends Composite {

  private static Binder binder = GWT.create(Binder.class);
  private final HomeView homeView;
  private final PlayShowPanelFactory showPanelFactory;
  private int lastPageSize;

  @UiField
  HTMLPanel playRowList;

  private final int[] gameOnPanelArr = {1, 2, 3, 4, 6};
  private int gamesOnPanelCounter = 1;
  private List<Game> gameList;

  private int INCREMENT_SIZE;

  private boolean updateFlag = true;

  private int lastMaxHeight = 0;
  private int lastScrollPos = 0;
  private HandlerRegistration scrollHandler;
  private boolean updateFlag2;

  public PlayShowPanel(HomeView homeView, PlayShowPanelFactory showPanelFactory, ClientConfiguration config) {
    this.showPanelFactory = showPanelFactory;
    this.homeView = homeView;
    INCREMENT_SIZE = Integer.valueOf(config.incrementPlayShowSize());
    initWidget(binder.createAndBindUi(this));

    initScroll();
  }

  private HandlerRegistration initScroll() {
    return Window.addWindowScrollHandler(new Window.ScrollHandler() {
      @Override
      public void onWindowScroll(Window.ScrollEvent event) {
        int oldScrollPos = lastScrollPos;
        lastScrollPos = Window.getScrollTop();
        // если листаем вверх
        if (oldScrollPos >= lastScrollPos) {
          return;
        }
        DTLog.debug("SCROLL POS " + lastScrollPos);
        DTLog.debug("OLD POS " + oldScrollPos);
        int maxScrollTop = getOffsetHeight() - Window.getScrollTop();
        DTLog.debug("max scroll top" + maxScrollTop);
        int halfIncrementScrollSize = (maxScrollTop - lastMaxHeight) / 2;
        DTLog.debug("half inc " + halfIncrementScrollSize);
        if (lastScrollPos >= (maxScrollTop - halfIncrementScrollSize) && updateFlag) {
          DTLog.debug("load new games");
          final int newPageSize = gameList.size() + INCREMENT_SIZE;
          PlayShowPanel.this.homeView.getMoreGames(newPageSize);
          DTLog.debug("DISPLAYED GAMES " + gameList.size());
          lastMaxHeight = maxScrollTop;
          updateFlag = false;
        }
        if (maxScrollTop > lastMaxHeight) {
          updateFlag = true;
        }
      }
    });
  }

  public int setGames(List<Game> games) {
    this.gameList = games;
    resetGames(games);
    return games.size();
  }

  private void resetGames(List<Game> gameList) {
    resetGamesOnPanel(gameList);
  }

  public int addGames(List<Game> gameList) {
    return addGamesToPanel(gameList);
  }

  private int addGamesToPanel(List<Game> gameList) {
    if (gameList == null || gameList.isEmpty()) {
      return 0;
    }

    final int gamesInRow = gameOnPanelArr[gamesOnPanelCounter];
    int filledGamesInRow = this.gameList.size() % gamesInRow;
    int offsetRow = 0;
    // в начале дополняем существующую строку
    if (filledGamesInRow != 0) {
      Row lastRow = (Row) playRowList.getWidget(playRowList.getWidgetCount() - 2);
      for (int i = 0; i < gamesInRow - filledGamesInRow; i++) {
        Column column = new Column("MD_" + Variables.COLUMNS_IN_LAYOUT / gamesInRow);
        final PlayItem item = showPanelFactory.createItem(homeView.getPlayer(), gameList.get(i));
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
    List<Game> rowGameList = new ArrayList<>();
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

  private void resetGamesOnPanel(List<Game> gameList) {
    if (gameList == null || gameList.isEmpty()) {
      DTLog.debug("EMPTY GAME LIST");
      return;
    }
    gamesOnPanelCounter = DTCookies.getGamesOnPageCounter();
    if (gamesOnPanelCounter <= 0) {
      homeView.setEnableLessGameButton(false);
    }
    if (gamesOnPanelCounter >= 4) {
      homeView.setEnableMoreGameButton(false);
    }
    playRowList.clear();
    List<Game> rowGameList = new ArrayList<>();
    final int gameInRow = gameOnPanelArr[gamesOnPanelCounter];
    DTLog.debug("GAME IN ROW " + gameInRow + " " + gamesOnPanelCounter);
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

  private void addGameRow(List<Game> rowGameList, int gamesInRow) {
    Row row = new Row();
    if (gamesInRow == 1) {
      gamesInRow = 2;
    }
    for (Game game : rowGameList) {
      Column column = new Column("MD_" + Variables.COLUMNS_IN_LAYOUT / gamesInRow);
      final PlayItem item = showPanelFactory.createItem(homeView.getPlayer(), game);
      column.add(item);
      row.add(column);
    }
    playRowList.add(row);
    playRowList.add(new Hr());
  }

  public void moreGameOnPanel() {
    gamesOnPanelCounter++;
    DTCookies.setGamesOnPageCounter(gamesOnPanelCounter);
    if (!homeView.isEnabledLessGameButton()) {
      homeView.setEnableLessGameButton(true);
    }
    resetGames(gameList);
    if (gamesOnPanelCounter == gameOnPanelArr.length - 1) {
      homeView.setEnableMoreGameButton(false);
    }
  }

  public void lessGameOnPanel() {
    gamesOnPanelCounter--;
    DTCookies.setGamesOnPageCounter(gamesOnPanelCounter);
    if (!homeView.isEnabledMoreGameButton()) {
      homeView.setEnableMoreGameButton(true);
    }
    resetGames(gameList);
    if (gamesOnPanelCounter == 0) {
      homeView.setEnableLessGameButton(false);
    }
  }

  interface Binder extends UiBinder<HTMLPanel, PlayShowPanel> {
  }
}
