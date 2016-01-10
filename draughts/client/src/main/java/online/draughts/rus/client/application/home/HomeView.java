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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import online.draughts.rus.client.gin.PlayShowPanelFactory;
import online.draughts.rus.client.resources.Variables;
import online.draughts.rus.client.util.Cookies;
import online.draughts.rus.shared.dto.GameDto;
import online.draughts.rus.shared.dto.PlayerDto;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.CheckBoxButton;
import org.gwtbootstrap3.client.ui.html.Span;
import org.gwtbootstrap3.client.ui.html.Strong;

import java.util.List;

public class HomeView extends ViewWithUiHandlers<HomeUiHandlers> implements HomePresenter.MyView {

  private final Cookies cookies;
  private boolean isMyGames;

  PlayShowPanel playShowPanel;
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
    myGameListCheckButton.setValue(isMyGames);
    playShowPanel = playShowPanelFactory.createShowPanel();
    playShowSimplePanel.add(playShowPanel);

    bindSlot(HomePresenter.SLOT_SHOW_PLAY_PANEL, main);
  }

  @Override
  protected void onAttach() {
    super.onAttach();
    playShowPanel.init();
  }

  @SuppressWarnings("unused")
  @UiHandler("moreGameOnPage")
  public void onMoreGameOnPage(ClickEvent event) {
    playShowPanel.moreGameOnPanel();
  }

  @SuppressWarnings("unused")
  @UiHandler("lessGameOnPage")
  public void onLessGameOnPage(ClickEvent event) {
    playShowPanel.lessGameOnPanel();
  }

  @SuppressWarnings("unused")
  @UiHandler("myGameListCheckButton")
  public void onMyGameList(ClickEvent event) {
    final Boolean value = myGameListCheckButton.getValue();
    cookies.setMyGames(value);
    isMyGames = value;
    getUiHandlers().updatePlayShowPanel(value);
  }

  @Override
  public void setShowLoggedInControls(Boolean loggedIn) {
    myGameListCheckButton.setVisible(loggedIn);
    gameListLabel.setVisible(!loggedIn);
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
    getUiHandlers().getMoreGames(myGameListCheckButton.getValue(), newPageSize);
  }

  public PlayerDto getPlayer() {
    return getUiHandlers().getPlayer();
  }

  public interface Binder extends UiBinder<Widget, HomeView> {
  }
}
