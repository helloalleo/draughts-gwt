
package online.shashki.rus.client.application.component.playshowpanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import online.shashki.rus.client.application.home.HomeView;
import online.shashki.rus.client.resources.Variables;
import online.shashki.rus.client.utils.SHLog;
import online.shashki.rus.shared.model.Game;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Row;

import java.util.ArrayList;
import java.util.List;

public class PlayShowPanel extends Composite {

  private static Binder binder = GWT.create(Binder.class);

  @UiField
  HTMLPanel playRowList;

  private final int[] gameOnPanelArr = {6, 4, 3, 2, 1};
  private int gameOnPanelCounter = 3;
  private List<Game> gameList;

  PlayShowPanel() {
    initWidget(binder.createAndBindUi(this));
  }

  public void setGames(List<Game> gameList) {
    this.gameList = gameList;
    updateGameListPanel();
  }

  private void updateGameListPanel() {
    if (gameList == null || gameList.isEmpty()) {
      SHLog.log("EMPTY GAME LIST");
      return;
    }
    playRowList.clear();
    List<Game> rowGameList = new ArrayList<>();
    final int gameInRow = gameOnPanelArr[gameOnPanelCounter];
    SHLog.log("GAME IN ROW " + gameInRow);
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
      column.add(new PlayItem(game));
      row.add(column);
    }
    playRowList.add(row);
  }

  public void moreGameOnPanel(HomeView homeView) {
    gameOnPanelCounter++;
    if (!homeView.isEnabledLessGameButton()) {
      homeView.setEnableLessGameButton(true);
    }
    updateGameListPanel();
    if (gameOnPanelCounter == gameOnPanelArr.length - 1) {
      homeView.setEnableMoreGameButton(false);
    }
  }

  public void lessGameOnPanel(HomeView homeView) {
    gameOnPanelCounter--;
    if (!homeView.isEnabledMoreGameButton()) {
      homeView.setEnableMoreGameButton(true);
    }
    updateGameListPanel();
    if (gameOnPanelCounter == 0) {
      homeView.setEnableLessGameButton(false);
    }
  }

  interface Binder extends UiBinder<HTMLPanel, PlayShowPanel> {
  }
}
