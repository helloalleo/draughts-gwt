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

package online.shashki.rus.client.application;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.presenter.slots.PermanentSlot;
import com.gwtplatform.mvp.client.proxy.Proxy;
import online.shashki.rus.client.application.login.CurrentSession;
import online.shashki.rus.client.application.menu.MenuPresenter;
import online.shashki.rus.client.rpc.ProfileRpcServiceAsync;
import online.shashki.rus.shared.model.Shashist;

/**
 * This is the top-level presenter of the hierarchy. Other presenters reveal themselves within this presenter.
 * <p>
 * The goal of this sample is to show how to use nested presenters. These can be useful to decouple multiple presenters
 * that need to be displayed on the screen simultaneously.
 */
public class ApplicationPresenter extends Presenter<ApplicationPresenter.MyView, ApplicationPresenter.MyProxy> {
  /**
   * Use this in leaf presenters, inside their {@link #revealInParent} method.
   */
  public static final NestedSlot SLOT_MAIN_CONTENT = new NestedSlot();
  static final PermanentSlot<MenuPresenter> SLOT_MENU = new PermanentSlot<>();
  private final MenuPresenter menuPresenter;
  private final ProfileRpcServiceAsync profileService;
  private final CurrentSession currentSession;

  @Inject
  ApplicationPresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy,
      MenuPresenter menuPresenter,
      CurrentSession currentSession,
      ProfileRpcServiceAsync profileService) {
    super(eventBus, view, proxy, RevealType.Root);

    this.menuPresenter = menuPresenter;
    this.profileService = profileService;
    this.currentSession = currentSession;

    getCurrentProfile();
  }

  @Override
  protected void onBind() {
    super.onBind();

    setInSlot(SLOT_MENU, menuPresenter);
  }

  public void getCurrentProfile() {
    GWT.log("Current Profile");
    profileService.getCurrentProfile(new AsyncCallback<Shashist>() {
      @Override
      public void onFailure(Throwable caught) {

      }

      @Override
      public void onSuccess(Shashist result) {
        GWT.log(result != null ? result.getPublicName() : "NONE");
        currentSession.setCurrentPlayer(result);
      }
    });
  }

  /**
   * {@link ApplicationPresenter}'s proxy.
   */
  @ProxyStandard
  public interface MyProxy extends Proxy<ApplicationPresenter> {
  }

  /**
   * {@link ApplicationPresenter}'s view.
   */
  public interface MyView extends View {
  }
}
