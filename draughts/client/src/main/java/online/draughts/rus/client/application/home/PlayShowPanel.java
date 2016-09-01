
package online.draughts.rus.client.application.home;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import online.draughts.rus.client.gin.DialogFactory;
import online.draughts.rus.client.gin.PlayShowPanelFactory;
import online.draughts.rus.client.resources.Variables;
import online.draughts.rus.client.util.AbstractAsyncCallback;
import online.draughts.rus.client.util.Cookies;
import online.draughts.rus.client.util.Logger;
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

  private static final int MAX_GAMES_IN_ROW = 6;
  private static final int MIN_GAMES_IN_ROW = 1;
  private final PlayShowPanelFactory showPanelFactory;
  private final Cookies cookies;
  private final PlaceManager placeManager;
  private final ResourceDelegate<GamesResource> gamesDelegate;
  private final DraughtsMessages messages;
  private final DialogFactory dialogFactory;
  private final ShowPanelEnum showPanelEnum;
  private final GamesPanelViewable gamesView;
  private PagingList gamesInRow;
//  private PagingList myGamesInRow;

  @UiField
  HTMLPanel playRowList;

  private List<GameDto> gameList;

  private int incrementSize;

  private boolean updateFlag = true;

  private int lastMaxHeight = 0;
  private int lastScrollPos = 0;
  private boolean blockScroll;
  private List<GameDto> showedGames = new ArrayList<>();

  @Inject
  public PlayShowPanel(
      Binder binder,
      PlaceManager placeManager,
      ClientConfiguration config,
      PlayShowPanelFactory showPanelFactory,
      DraughtsMessages messages,
      ResourceDelegate<GamesResource> gamesDelegate,
      Cookies cookies,
      DialogFactory dialogFactory,
      @Assisted ShowPanelEnum showPanelEnum,
      @Assisted GamesPanelViewable gamesView) {

    this.showPanelEnum = showPanelEnum;

    incrementSize = Integer.valueOf(config.initShowGamesPageSize());

    this.showPanelFactory = showPanelFactory;
    this.cookies = cookies;
    this.placeManager = placeManager;
    this.dialogFactory = dialogFactory;
    this.gamesView = gamesView;
    this.messages = messages;
    this.gamesDelegate = gamesDelegate;

    gamesInRow = new PagingList(1).add(2).add(3).add(4).add(6);
    Logger.debug(gamesView);
    int numOnPage = gamesView.getGamesInRowNumber();
    while (gamesInRow.hasPrev()) {
      Logger.debug(gamesInRow);
      if (gamesInRow.getNumInRow() == numOnPage) {
        break;
      }
      gamesInRow = gamesInRow.getPrev();
    }

    initWidget(binder.createAndBindUi(this));
  }

  public void init() {
    initScroll();
    initShowPlay();
  }

  private void initShowPlay() {
    blockScroll = false;
    String showGame = placeManager.getCurrentPlaceRequest().getParameter("game", "");
    if (StringUtils.isNotEmpty(showGame)) {
      final Long id = Long.valueOf(showGame);
      if (id == null) {
        dialogFactory.createInfoDialogBox(messages.gameNotFound()).show();
      } else {
        gamesDelegate.withCallback(
            new AbstractAsyncCallback<GameDto>(dialogFactory) {
              @Override
              public void onSuccess(GameDto result) {
                if (result == null) {
                  dialogFactory.createInfoDialogBox(messages.gameNotFound()).show();
                  return;
                }
                resetGames(Collections.singletonList(result), gamesInRow.getNumInRow());
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
          final int newPageSize = incrementSize;
          gamesView.getMoreGames(newPageSize);
          lastMaxHeight = maxScrollTop;
          updateFlag = false;
        }
        if (maxScrollTop > lastMaxHeight) {
          updateFlag = true;
        }
      }
    });
  }

  public int setGames(List<GameDto> games) {
    blockScroll = false;
    this.gameList = games;
    resetGames(games, gamesInRow.getNumInRow());
    return games.size();
  }

  private void resetGames(List<GameDto> gameList, int gamesNumInRow) {
    lastMaxHeight = 0;
    lastScrollPos = 0;
    resetGamesOnPanel(gameList, gamesNumInRow);
  }

  public int addGames(List<GameDto> gameList) {
    return addGamesToPanel(gameList, gamesInRow.getNumInRow());
  }

  private int addGamesToPanel(List<GameDto> gameList, int numGamesInRow) {
    if (gameList == null || gameList.isEmpty()) {
      return 0;
    }

    int filledGamesInRow = this.gameList.size() % numGamesInRow;
    int offsetRow = 0;
    // в начале дополняем существующую строку
    if (filledGamesInRow != 0) {
      Row lastRow = (Row) playRowList.getWidget(playRowList.getWidgetCount() - 2);
      for (int i = 0; i < numGamesInRow - filledGamesInRow; i++) {
        final GameDto gameDto = gameList.get(i);
        if (showedGames.contains(gameDto)) {
          continue;
        }

        Column column = new Column(getSize(numGamesInRow));
        final PlayItem item = showPanelFactory.createItem(numGamesInRow, gameDto, showPanelEnum, gamesView);
        showedGames.add(gameDto);
        column.add(item);
        lastRow.add(column);

        if (lastRow.getWidgetCount() == numGamesInRow && i != numGamesInRow - filledGamesInRow - 1) {
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
      if (0 == (rowGameList.size() + 1) % (numGamesInRow)) {
        rowGameList.add(gameList.get(i));
        addGameRow(rowGameList, numGamesInRow);
        rowGameList.clear();
      } else {
        rowGameList.add(gameList.get(i));
      }
    }
    // дополняем оставшуюся строку
    if (!rowGameList.isEmpty()) {
      addGameRow(rowGameList, numGamesInRow);
    }

    this.gameList.addAll(gameList);
    return this.gameList.size();
  }

  private String getSize(int gamesInRow) {
    return "MD_" + Variables.COLUMNS_IN_LAYOUT / gamesInRow
        + " " + "SM_" + Variables.COLUMNS_IN_LAYOUT / gamesInRow
        + " " + "XS_" + Variables.COLUMNS_IN_LAYOUT / gamesInRow;
  }

  private void resetGamesOnPanel(List<GameDto> gameList, int numGamesInRow) {
    showedGames.clear();
    if (gameList == null || gameList.isEmpty()) {
      playRowList.clear();
      return;
    }
    updateMoreLessButtons(numGamesInRow);
    playRowList.clear();
    List<GameDto> rowGameList = new ArrayList<>();
    for (int i = 0; i < gameList.size(); i++) {
      if ((i + 1) % numGamesInRow == 0) {
        rowGameList.add(gameList.get(i));
        addGameRow(rowGameList, numGamesInRow);
        rowGameList.clear();
      } else {
        rowGameList.add(gameList.get(i));
      }
    }
    if (!rowGameList.isEmpty()) {
      addGameRow(rowGameList, numGamesInRow);
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
      final PlayItem item = showPanelFactory.createItem(gamesInRow, game, showPanelEnum, gamesView);
      showedGames.add(game);
      column.add(item);
      row.add(column);
    }
    if (row.getWidgetCount() > 0) {
      playRowList.add(row);
      playRowList.add(new Hr());
    }
  }

  public void moreGameOnPanel() {
    gamesView.setEnableLessGameButton(true);
    int gamesNumInRow = getMoreGamesInRow(true);
    resetGames(gameList, gamesNumInRow);
//    if (!hasMoreGames(true)) {
//      homeViewProvider.get().setEnableMoreGameButton(false);
//    }
  }

  public void lessGameOnPanel() {
      gamesView.setEnableMoreGameButton(true);
    int gamesNumInRow = getMoreGamesInRow(false);
    resetGames(gameList, gamesNumInRow);
//    if (!hasMoreGames(false)) {
//      homeViewProvider.get().setEnableLessGameButton(false);
//    }
  }

//  private int getGamesInRow() {
//    switch (showPanelEnum) {
//      case HOME_PANEL:
//        return gamesInRow.getNumInRow();
//      case MY_GAMES_PANEL:
//        return myGamesInRow.getNumInRow();
//    }
//    return MAX_GAMES_IN_ROW;
//  }

  private void updateMoreLessButtons(int gamesInRow) {
    gamesView.setEnableLessGameButton(!(gamesInRow <= MIN_GAMES_IN_ROW));
    gamesView.setEnableMoreGameButton(!(gamesInRow >= MAX_GAMES_IN_ROW));
  }

  /**
   * Следующее количество игр в строке таблицы
   * @param forward идем в перед или назад?
   * @return количество игр в строке
   */
  private int getMoreGamesInRow(boolean forward) {
    gamesInRow = gamesView.getMoreGamesInRow(forward, gamesInRow);
    return gamesInRow.getNumInRow();
  }

  public void updateGames() {
    resetGames(gameList, gamesInRow.getNumInRow());
  }

  public void removeGame(GameDto gameDto) {
    gameList.remove(gameDto);
    updateGames();
  }

  public class PagingList {
    private int numOnPage;

    private PagingList next;
    private PagingList prev;

    PagingList(int numOnPage) {
      this.numOnPage = numOnPage;
    }

    public PagingList add(int numOnPage) {
      next = new PagingList(numOnPage);
      next.setPrev(this);
      return next;
    }

    public int getNumInRow() {
      return numOnPage;
    }

    public void setNumOnPage(int numOnPage) {
      this.numOnPage = numOnPage;
    }

    public PagingList getNext() {
      return next;
    }

    public boolean hasNext() {
      return null != next;
    }

    public boolean hasPrev() {
      return null != prev;
    }

    public void setNext(PagingList next) {
      this.next = next;
    }

    public PagingList getPrev() {
      return prev;
    }

    public void setPrev(PagingList prev) {
      this.prev = prev;
    }
  }

  public interface Binder extends UiBinder<HTMLPanel, PlayShowPanel> {
  }
}
