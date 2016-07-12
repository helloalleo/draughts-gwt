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

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import online.draughts.rus.client.application.analysis.AnalysisModule;
import online.draughts.rus.client.application.error.ErrorModule;
import online.draughts.rus.client.application.footer.FooterModule;
import online.draughts.rus.client.application.game.GameModule;
import online.draughts.rus.client.application.home.HomeModule;
import online.draughts.rus.client.application.learn.LearnModule;
import online.draughts.rus.client.application.love.LoveModule;
import online.draughts.rus.client.application.menu.MenuModule;
import online.draughts.rus.client.application.play.PlayModule;
import online.draughts.rus.client.application.profile.ProfileModule;
import online.draughts.rus.client.application.rules.RulesModule;
import online.draughts.rus.client.application.security.OAuthLoginModule;

public class ApplicationModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    install(new RulesModule());
    install(new GameModule());
    install(new FooterModule());
    install(new AnalysisModule());
    install(new LoveModule());
    install(new LearnModule());
    install(new PlayModule());
    install(new ProfileModule());
    install(new ErrorModule());
    install(new MenuModule());
    install(new HomeModule());
    install(new OAuthLoginModule());

    // Application Presenters
    bindPresenter(ApplicationPresenter.class, ApplicationPresenter.MyView.class, ApplicationView.class,
        ApplicationPresenter.MyProxy.class);
  }
}
