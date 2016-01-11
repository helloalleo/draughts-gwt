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

import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.ServletModule;
import com.gwtplatform.dispatch.rpc.server.guice.DispatchServiceImpl;
import com.gwtplatform.dispatch.rpc.shared.ActionImpl;
import online.draughts.rus.server.channel.ServerChannel;
import online.draughts.rus.server.servlet.CheckOnlineServlet;
import online.draughts.rus.server.servlet.GameGiff;
import online.draughts.rus.server.servlet.LogoutServlet;
import online.draughts.rus.server.servlet.oauth.*;
import online.draughts.rus.server.util.AuthUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class DispatchServletModule extends ServletModule {

  @Override
  public void configureServlets() {
    filter("/*").through(createUserIdScopingFilter());

    serve("/" + ActionImpl.DEFAULT_SERVICE_NAME + "*").with(DispatchServiceImpl.class);

    serve("/Application/channel").with(ServerChannel.class);
    serve("/cron/checkonline").with(CheckOnlineServlet.class);

    serve("/logout").with(LogoutServlet.class);

    serve("/vkOAuth").with(OAuthVKServlet.class);
    serve("/vkOAuthCallback").with(OAuthVKCallbackServlet.class);

    serve("/fbOAuth").with(OAuthFacebookServlet.class);
    serve("/fbOAuthCallback").with(OAuthFacebookCallbackServlet.class);

    serve("/gOAuth").with(OAuthGoogleServlet.class);
    serve("/gOAuthCallback").with(OAuthGoogleCallbackServlet.class);

    serve("/getGame").with(GameGiff.class);
  }

  protected Filter createUserIdScopingFilter() {
    return new Filter() {
      @Override
      public void doFilter(
          ServletRequest request, ServletResponse response, FilterChain chain)
          throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (httpRequest != null) {
          Boolean authenticated = (Boolean) httpRequest.getSession().getAttribute(AuthUtils.AUTHENTICATED);
          httpRequest.setAttribute(
              Key.get(Boolean.class, Names.named(AuthUtils.AUTHENTICATED)).toString(),
              authenticated);
          chain.doFilter(request, response);
        }
      }

      @Override
      public void init(FilterConfig filterConfig) throws ServletException {
      }

      @Override
      public void destroy() {
      }
    };
  }

  @Provides
  @Named(AuthUtils.AUTHENTICATED)
  @RequestScoped
  Boolean provideAuthenticated() {
    return false;
  }
}