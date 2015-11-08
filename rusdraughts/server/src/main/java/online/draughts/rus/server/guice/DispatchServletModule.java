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

package online.draughts.rus.server.guice;

import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.ServletModule;
import com.gwtplatform.dispatch.rpc.server.guice.DispatchServiceImpl;
import com.gwtplatform.dispatch.rpc.shared.ActionImpl;
import online.draughts.rus.server.config.CustomConfigurator;
import online.draughts.rus.server.servlet.GameGiff;
import online.draughts.rus.server.servlet.LogoutServlet;
import online.draughts.rus.server.servlet.oauth.*;

public class DispatchServletModule extends ServletModule {

  public static final String DRAUGHTS_PU = "draughtsPU";

  @Override
  public void configureServlets() {
    install(new JpaPersistModule(DRAUGHTS_PU));
    filter("/*").through(PersistFilter.class);

    serve("/" + ActionImpl.DEFAULT_SERVICE_NAME + "*").with(DispatchServiceImpl.class);
    serve("/logout").with(LogoutServlet.class);
    serve("/vkOAuth").with(OAuthVKServlet.class);
    serve("/vkOAuthCallback").with(OAuthVKCallbackServlet.class);
    serve("/fbOAuth").with(OAuthFacebookServlet.class);
    serve("/fbOAuthCallback").with(OAuthFacebookCallbackServlet.class);
    serve("/gOAuth").with(OAuthGoogleServlet.class);
    serve("/gOAuthCallback").with(OAuthGoogleCallbackServlet.class);
    serve("/gameGiff").with(GameGiff.class);

    requestStaticInjection(CustomConfigurator.class);
  }
}
