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

package online.shashki.rus.server.guice;

import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.ServletModule;
import com.gwtplatform.dispatch.rpc.server.guice.DispatchServiceImpl;
import com.gwtplatform.dispatch.rpc.shared.ActionImpl;
import online.shashki.rus.server.servlet.LogoutServlet;
import online.shashki.rus.server.servlet.oauth.OAuthFacebookCallbackServlet;
import online.shashki.rus.server.servlet.oauth.OAuthFacebookServlet;
import online.shashki.rus.server.servlet.oauth.OAuthVKCallbackServlet;
import online.shashki.rus.server.servlet.oauth.OAuthVKServlet;

public class DispatchServletModule extends ServletModule {

  public static final String SHASHKI64_PU = "shashki64PU";

  @Override
  public void configureServlets() {
    install(new JpaPersistModule(SHASHKI64_PU));
    filter("/*").through(PersistFilter.class);

    serve("/" + ActionImpl.DEFAULT_SERVICE_NAME + "*").with(DispatchServiceImpl.class);
    serve("/logout").with(LogoutServlet.class);
    serve("/OAuthVKServlet").with(OAuthVKServlet.class);
    serve("/OAuthVKCallbackServlet").with(OAuthVKCallbackServlet.class);
    serve("/OAuthFacebookServlet").with(OAuthFacebookServlet.class);
    serve("/OAuthFacebookCallbackServlet").with(OAuthFacebookCallbackServlet.class);

    requestStaticInjection(CustomConfigurator.class);
  }
}
