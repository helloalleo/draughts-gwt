
package online.draughts.rus.client.application.component.playshowpanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import online.draughts.rus.client.application.home.HomeView;
import online.draughts.rus.client.gin.PlayShowPanelFactory;
import online.draughts.rus.client.resources.Variables;
import online.draughts.rus.client.util.DCookies;
import online.draughts.rus.client.util.DTLog;
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

  @UiField
  HTMLPanel playRowList;

  private final int[] gameOnPanelArr = {1, 2, 3, 4, 6};
  private int gamesOnPanelCounter = 1;
  private List<Game> gameList;

  private final int INCREMENT_SIZE = 18;

  private boolean updateFlag = true;

  private int lastMaxHeight = 0;
  private int lastScrollPos = 0;

  @Inject
  public PlayShowPanel(HomeView homeView, PlayShowPanelFactory showPanelFactory) {
    this.showPanelFactory = showPanelFactory;
    this.homeView = homeView;
    initWidget(binder.createAndBindUi(this));

    final String marginStr = Variables.S_MAIN_CONTAINER_MARGIN_TOP.substring(0, Variables.S_MAIN_CONTAINER_MARGIN_TOP.length() - 2);
    Window.addWindowScrollHandler(new Window.ScrollHandler() {
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

  public void setGames(List<Game> gameList) {
    this.gameList = gameList;
    updateGameListPanel();
  }

  private void updateGameListPanel() {
    if (gameList == null || gameList.isEmpty()) {
      DTLog.debug("EMPTY GAME LIST");
      return;
    }
    gamesOnPanelCounter = DCookies.getGamesOnPageCounter();
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

  private void addGameRow(List<Game> rowGameList, int gameInRow) {
    Row row = new Row();
    if (gameInRow == 1) {
      gameInRow = 2;
    }
    for (Game game : rowGameList) {
      Column column = new Column("MD_" + Variables.COLUMNS_IN_LAYOUT / gameInRow);
      column.add(showPanelFactory.createItem(homeView.getPlayer(), game));
      row.add(column);
    }
    playRowList.add(row);
    playRowList.add(new Hr());
  }

  public void moreGameOnPanel() {
    gamesOnPanelCounter++;
    DCookies.setGamesOnPageCounter(gamesOnPanelCounter);
    if (!homeView.isEnabledLessGameButton()) {
      homeView.setEnableLessGameButton(true);
    }
    updateGameListPanel();
    if (gamesOnPanelCounter == gameOnPanelArr.length - 1) {
      homeView.setEnableMoreGameButton(false);
    }
  }

  public void lessGameOnPanel() {
    gamesOnPanelCounter--;
    DCookies.setGamesOnPageCounter(gamesOnPanelCounter);
    if (!homeView.isEnabledMoreGameButton()) {
      homeView.setEnableMoreGameButton(true);
    }
    updateGameListPanel();
    if (gamesOnPanelCounter == 0) {
      homeView.setEnableLessGameButton(false);
    }
  }

  interface Binder extends UiBinder<HTMLPanel, PlayShowPanel> {
  }
}
