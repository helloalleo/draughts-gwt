
package online.shashki.rus.client.application.component.playshowpanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import online.shashki.rus.client.application.home.HomeView;
import online.shashki.rus.client.resources.Variables;
import online.shashki.rus.client.util.SHLog;
import online.shashki.rus.shared.model.Game;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.html.Hr;

import java.util.ArrayList;
import java.util.List;

public class PlayShowPanel extends Composite {

  private static Binder binder = GWT.create(Binder.class);
  private final HomeView homeView;

  @UiField
  HTMLPanel playRowList;
  @UiField
  ScrollPanel mainScrollPanel;

  private final int[] gameOnPanelArr = {6, 4, 3, 2, 1};
  private int gameOnPanelCounter = 1;
  private List<Game> gameList;
  private int panelHeight;

  private final int incrementSize = 50;

  private boolean updateFlag = true;

  private int lastMaxHeight = 0;
  private int lastScrollPos = 0;

  public PlayShowPanel(HomeView homeView) {
    this.homeView = homeView;
    initWidget(binder.createAndBindUi(this));
    final String marginStr = Variables.S_MAIN_CONTAINER_MARGIN_TOP.substring(0, Variables.S_MAIN_CONTAINER_MARGIN_TOP.length() - 2);
    panelHeight = Window.getClientHeight() - Integer.valueOf(marginStr) - 50;
    mainScrollPanel.setHeight(panelHeight + "px");
    mainScrollPanel.addScrollHandler(new ScrollHandler() {
      @Override
      public void onScroll(ScrollEvent event) {
        int oldScrollPos = lastScrollPos;
        lastScrollPos = mainScrollPanel.getVerticalScrollPosition();
        if (oldScrollPos >= lastScrollPos) {
          return;
        }

        int maxScrollTop = mainScrollPanel.getWidget().getOffsetHeight() - mainScrollPanel.getOffsetHeight();
        int halfIncrementScrollSize = (maxScrollTop - lastMaxHeight) / 2;
        if (lastScrollPos >= (maxScrollTop - halfIncrementScrollSize) && updateFlag) {
          final int newPageSize = gameList.size() + incrementSize;
          PlayShowPanel.this.homeView.getMoreGames(newPageSize);
          SHLog.debug("DISPLAYED GAMES " + gameList.size());
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
      SHLog.debug("EMPTY GAME LIST");
      return;
    }
    playRowList.clear();
    List<Game> rowGameList = new ArrayList<>();
    final int gameInRow = gameOnPanelArr[gameOnPanelCounter];
    SHLog.debug("GAME IN ROW " + gameInRow);
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
    for (Game game : rowGameList) {
      Column column = new Column("MD_" + Variables.COLUMNS_IN_LAYOUT / gameInRow);
      column.add(new PlayItem(homeView.getPlayer(), game));
      row.add(column);
    }
    playRowList.add(row);
    playRowList.add(new Hr());
  }

  public void moreGameOnPanel() {
    gameOnPanelCounter++;
    if (!homeView.isEnabledLessGameButton()) {
      homeView.setEnableLessGameButton(true);
    }
    updateGameListPanel();
    if (gameOnPanelCounter == gameOnPanelArr.length - 1) {
      homeView.setEnableMoreGameButton(false);
    }
  }

  public void lessGameOnPanel() {
    gameOnPanelCounter--;
    if (!homeView.isEnabledMoreGameButton()) {
      homeView.setEnableMoreGameButton(true);
    }
    updateGameListPanel();
    if (gameOnPanelCounter == 0) {
      homeView.setEnableLessGameButton(false);
    }
  }

  interface Binder extends UiBinder<ScrollPanel, PlayShowPanel> {
  }
}
