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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import org.gwtbootstrap3.client.ui.Button;

public class HomeView extends ViewWithUiHandlers<HomeUiHandlers> implements HomePresenter.MyView {

  @UiField
  SimplePanel play;
  @UiField
  SimplePanel playShowPanel;
  @UiField
  Button newGameButton;

  @Inject
  HomeView(
      Binder binder
      ) {
    initWidget(binder.createAndBindUi(this));


    bindSlot(HomePresenter.SLOT_PLAY, play);
    bindSlot(HomePresenter.SLOT_PLAY_SHOW_PANEL, playShowPanel);
  }

  @UiHandler("newGameButton")
  public void onNewGame(ClickEvent event) {
    play.setVisible(!play.isVisible());
    playShowPanel.setVisible(!playShowPanel.isVisible());
  }

  @Override
  public void setShowLoggedInControls(Boolean loggedIn) {
    newGameButton.setVisible(loggedIn);
  }

  interface Binder extends UiBinder<Widget, HomeView> {
  }
}
