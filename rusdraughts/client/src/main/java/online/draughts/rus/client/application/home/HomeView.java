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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import online.draughts.rus.client.application.component.playshowpanel.PlayShowPanel;
import online.draughts.rus.client.application.security.CurrentSession;
import online.draughts.rus.client.util.DebugUtils;
import online.draughts.rus.client.util.SHCookies;
import online.draughts.rus.client.util.SHLog;
import online.draughts.rus.shared.config.ClientConfiguration;
import online.draughts.rus.shared.locale.DraughtsMessages;
import online.draughts.rus.shared.model.Game;
import online.draughts.rus.shared.model.Player;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.CheckBoxButton;
import org.gwtbootstrap3.client.ui.html.Strong;

import java.util.List;

public class HomeView extends ViewWithUiHandlers<HomeUiHandlers> implements HomePresenter.MyView {

  private static Binder binder = GWT.create(Binder.class);
  private final DraughtsMessages messages;
  private final CurrentSession currentSession;
  private final ClientConfiguration config;

  @UiField
  SimplePanel play;
  @UiField(provided = true)
  PlayShowPanel playShowPanel;
  @UiField
  Button newGameButton;
  @UiField
  Button moreGameOnPage;
  @UiField
  Button lessGameOnPage;
  @UiField
  ButtonGroup showGameOnPageButtonGroup;
  @UiField
  CheckBoxButton myGameListCheckButton;
  @UiField
  Strong gameListLabel;

  private boolean newGameState = true;

  @Inject
  HomeView(CurrentSession currentSession,
           ClientConfiguration config,
           DraughtsMessages messages) {
    this.config = config;
    playShowPanel = new PlayShowPanel(this);
    initWidget(binder.createAndBindUi(this));

    this.currentSession = currentSession;
    this.messages = messages;
    bindSlot(HomePresenter.SLOT_PLAY, play);
  }

  @Override
  protected void onAttach() {
    super.onAttach();
    newGameState = SHCookies.getNewGameButtonState();
    SHLog.debug("NEW STATE " + newGameState);
    if (DebugUtils.isProduction()) {
      newGameState = true;
      newGameButton.setVisible(false);
    }
    if (currentSession.isLoggedIn()) {
      showControlsNewGameButton();
    }
  }

  @UiHandler("newGameButton")
  public void onNewGame(ClickEvent event) {
    toggleNewGameButton();
    showControlsNewGameButton();
  }

  private void showControlsNewGameButton() {
    showGameOnPageButtonGroup.setVisible(!newGameState);
    myGameListCheckButton.setVisible(!newGameState);

    newGameButton.setText(newGameState ? messages.playListButtonText() : messages.newGameButtonText());

    play.setVisible(newGameState);
    playShowPanel.setVisible(!newGameState);
  }

  private void toggleNewGameButton() {
    newGameState = !newGameState;
    SHCookies.setNewGameButtonState(newGameState);
  }

  @UiHandler("moreGameOnPage")
  public void onMoreGameOnPage(ClickEvent event) {
    playShowPanel.moreGameOnPanel();
  }

  @UiHandler("lessGameOnPage")
  public void onLessGameOnPage(ClickEvent event) {
    playShowPanel.lessGameOnPanel();
  }

  @UiHandler("myGameListCheckButton")
  public void onMyGameList(ClickEvent event) {
    getUiHandlers().getMoreGames(myGameListCheckButton.getValue(), HomePresenter.INIT_SHOW_GAMES_PAGE_SIZE);
  }

  @Override
  public void setShowLoggedInControls(Boolean loggedIn) {
    SHLog.debug("LOGGED IN " + loggedIn);
    newGameButton.setVisible(loggedIn);
    myGameListCheckButton.setVisible(loggedIn);
    gameListLabel.setVisible(!loggedIn);
  }

  @Override
  public void setGames(List<Game> gameList) {
    playShowPanel.setGames(gameList);
  }

  public void setEnableMoreGameButton(boolean enable) {
    moreGameOnPage.setEnabled(enable);
  }

  public boolean isEnabledMoreGameButton() {
    return moreGameOnPage.isEnabled();
  }

  public void setEnableLessGameButton(boolean enableMoreGameButton) {
    lessGameOnPage.setEnabled(enableMoreGameButton);
  }

  public boolean isEnabledLessGameButton() {
    return lessGameOnPage.isEnabled();
  }

  public void getMoreGames(int newPageSize) {
    SHLog.debug("GET MORE GAMES " + newPageSize);
    getUiHandlers().getMoreGames(myGameListCheckButton.getValue(), newPageSize);
  }

  public Player getPlayer() {
    return getUiHandlers().getPlayer();
  }

  interface Binder extends UiBinder<Widget, HomeView> {
  }
}
