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

package online.draughts.rus.client.application;

import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.presenter.slots.PermanentSlot;
import com.gwtplatform.mvp.client.proxy.Proxy;
import online.draughts.rus.client.application.footer.FooterPresenter;
import online.draughts.rus.client.application.menu.MenuPresenter;
import online.draughts.rus.client.util.Cookies;
import online.draughts.rus.shared.util.StringUtils;

/**
 * This is the top-logLevel presenter of the hierarchy. Other presenters reveal themselves within this presenter.
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
  static final PermanentSlot<FooterPresenter> SLOT_FOOTER = new PermanentSlot<>();
  private final MenuPresenter menuPresenter;
  private final FooterPresenter footerPresenter;

  @Inject
  ApplicationPresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy,
      MenuPresenter menuPresenter,
      FooterPresenter footerPresenter,
      Cookies cookies) {
    super(eventBus, view, proxy, RevealType.Root);

    this.menuPresenter = menuPresenter;
    this.footerPresenter = footerPresenter;

    setLocation(cookies);
  }

  private void setLocation(Cookies cookies) {
    String savedLocale = cookies.getLocale();
    String currentLocale = Window.Location.getParameter("locale");
    if (!savedLocale.equals(currentLocale) && StringUtils.isNotEmpty(currentLocale)) {
      String hash = Window.Location.getHash();
      Window.Location.assign("/?locale=" + savedLocale + hash);
      cookies.setLocale(savedLocale);
    }
  }

  @Override
  protected void onBind() {
    super.onBind();

    setInSlot(SLOT_MENU, menuPresenter);
    setInSlot(SLOT_FOOTER, footerPresenter);
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
  interface MyView extends View {
  }
}
