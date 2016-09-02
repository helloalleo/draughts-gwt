/*
 * Copyright 2011 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package online.draughts.rus.client.application.home;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import online.draughts.rus.client.gin.PlayShowPanelFactory;
import online.draughts.rus.client.util.AdsUtils;
import online.draughts.rus.client.util.Cookies;
import online.draughts.rus.shared.dto.GameDto;
import online.draughts.rus.shared.dto.PlayerDto;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.html.Span;
import org.gwtbootstrap3.client.ui.html.Strong;

import java.util.List;

public class HomeView extends ViewWithUiHandlers<HomeUiHandlers>
    implements HomePresenter.MyView, GamesPanelViewable {

  private final Cookies cookies;
  private boolean isMyGames;

  private PlayShowPanel playShowPanel;
  @UiField
  Button moreGamesInRow;
  @UiField
  Button lessGamesInRow;
  @UiField
  ButtonGroup showGameOnPageButtonGroup;
  @UiField
  Strong gameListLabel;
  @UiField
  SimplePanel playShowSimplePanel;
  @UiField
  HTMLPanel main;
  @UiField
  Span totalPlayersSpan;
  @UiField
  Span onlinePlayersSpan;

  @Inject
  HomeView(Binder binder,
           PlayShowPanelFactory playShowPanelFactory,
           Cookies cookies) {
    initWidget(binder.createAndBindUi(this));

    this.cookies = cookies;
    isMyGames = cookies.isMyGames();
//    myGameListCheckButton.setValue(isMyGames);
    playShowPanel = playShowPanelFactory.createShowPanel(ShowPanelEnum.HOME_PANEL, this);
    playShowSimplePanel.add(playShowPanel);

    bindSlot(HomePresenter.SLOT_SHOW_PLAY_PANEL, main);
  }

  @Override
  protected void onAttach() {
    playShowPanel.init();
    AdsUtils.addAdvertisementFromGoogleScript();
  }

  @SuppressWarnings("unused")
  @UiHandler("moreGamesInRow")
  public void onMoreGameOnPage(ClickEvent event) {
    playShowPanel.moreGameOnPanel();
  }

  @SuppressWarnings("unused")
  @UiHandler("lessGamesInRow")
  public void onLessGameOnPage(ClickEvent event) {
    playShowPanel.lessGameOnPanel();
  }

//  @UiHandler("myGameListCheckButton")
//  public void test(ClickEvent clickEvent) {
//    if (!secondMyGameListClick) {
//      toggleMyGameList(!myGameListCheckButton.getValue());
//      secondMyGameListClick = true;
//      return;
//    }
//    secondMyGameListClick = false;
//  }

//  private void toggleMyGameList(boolean value) {
//    cookies.setMyGames(value);
//    isMyGames = value;
//    getUiHandlers().updatePlayShowPanel();
//  }

  @Override
  public void setShowLoggedInControls(Boolean loggedIn) {
//    myGameListCheckButton.setVisible(loggedIn);
//    gameListLabel.setVisible(!loggedIn);
  }

  @Override
  public int setGames(List<GameDto> gameList) {
    return playShowPanel.setGames(gameList);
  }

  @Override
  public int addGames(List<GameDto> games) {
    return playShowPanel.addGames(games);
  }

  @Override
  public boolean isMyGames() {
    return isMyGames;
  }

  @Override
  public void updateOnlinePlayerCounter(int size) {
    onlinePlayersSpan.setText(String.valueOf(size));
  }

  @Override
  public void updateTotalPlayersCounter(Integer result) {
    totalPlayersSpan.setText(String.valueOf(result));
  }

  public void setEnableMoreGameButton(boolean enable) {
    moreGamesInRow.setEnabled(enable);
  }

  @Override
  public PlayShowPanel.PagingList getMoreGamesInRow(boolean forward, PlayShowPanel.PagingList gamesInRow) {
    PlayShowPanel.PagingList tmpList = forward ? gamesInRow.getNext() : gamesInRow.getPrev();
    if (null != tmpList) {
      gamesInRow = tmpList;
    }
    cookies.setGamesInRowNumber(gamesInRow.getNumInRow());
    return gamesInRow;
  }

  @Override
  public int getGamesInRowNumber() {
    return cookies.getGamesInRowNumber();
  }

  @Override
  public void updateGames() {
    playShowPanel.updateGames();
  }

  @Override
  public void removeGame(GameDto gameDto) {
    throw new RuntimeException("Not implemented");
  }

  public void setEnableLessGameButton(boolean enableMoreGameButton) {
    lessGamesInRow.setEnabled(enableMoreGameButton);
  }

  public void getMoreGames(int newPageSize) {
    getUiHandlers().getMoreGames(newPageSize);
  }

  @Override
  public GamesPanelPresentable getPresenter() {
    return (GamesPanelPresentable) getUiHandlers();
  }

  public PlayerDto getPlayer() {
    return getUiHandlers().getPlayer();
  }

  public interface Binder extends UiBinder<Widget, HomeView> {
  }
}
