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

package online.shashki.rus.client.application.home;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import online.shashki.rus.client.application.component.playshowpanel.PlayShowPanel;
import online.shashki.rus.client.utils.SHLog;
import online.shashki.rus.shared.locale.ShashkiMessages;
import online.shashki.rus.shared.model.Game;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;

import java.util.List;

public class HomeView extends ViewWithUiHandlers<HomeUiHandlers> implements HomePresenter.MyView {

  private static Binder binder = GWT.create(Binder.class);
  private final ShashkiMessages messages;

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
  ButtonGroup countGameOnPageButtonGroup;

  private boolean newGameState = true;

  @Inject
  HomeView(ShashkiMessages messages) {
    playShowPanel = new PlayShowPanel(this);
    initWidget(binder.createAndBindUi(this));

    this.messages = messages;
    bindSlot(HomePresenter.SLOT_PLAY, play);
  }

  @UiHandler("newGameButton")
  public void onNewGame(ClickEvent event) {
    newGameState = !newGameState;
    countGameOnPageButtonGroup.setVisible(newGameState);
    newGameButton.setText(newGameState ? messages.newGameButtonText() : messages.playListButtonText());
    play.setVisible(!play.isVisible());
    playShowPanel.setVisible(!playShowPanel.isVisible());
  }

  @UiHandler("moreGameOnPage")
  public void onLessGameOnPage(ClickEvent event) {
    playShowPanel.moreGameOnPanel();
  }

  @UiHandler("lessGameOnPage")
  public void onMoreGameOnPage(ClickEvent event) {
    playShowPanel.lessGameOnPanel();
  }

  @Override
  public void setShowLoggedInControls(Boolean loggedIn) {
    SHLog.debug("LOGGED IN " + loggedIn);
    newGameButton.setVisible(loggedIn);
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
    getUiHandlers().getMoreGames(newPageSize);
  }

  interface Binder extends UiBinder<Widget, HomeView> {
  }
}
