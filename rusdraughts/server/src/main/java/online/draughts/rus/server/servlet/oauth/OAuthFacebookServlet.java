package online.draughts.rus.server.servlet.oauth;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import online.draughts.rus.server.config.ServerConfiguration;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 15.10.15
 * Time: 16:51
 */
@Singleton
public class OAuthFacebookServlet extends HttpServlet {

  private final Logger log;
  private final ServerConfiguration config;

  @Inject
  public OAuthFacebookServlet(ServerConfiguration configuration, Logger log) {
    this.config = configuration;
    this.log = log;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
      OAuthClientRequest oAuthRequest = OAuthClientRequest
          .authorizationProvider(OAuthProviderType.FACEBOOK)
          .setClientId(config.getFbClientId())
          .setScope(config.getFbScope())
          .setRedirectURI(config.getFbRedirectUri())
          .setResponseType("code")
          .buildQueryMessage();
      log.info("REQUEST URI: " + oAuthRequest.getLocationUri());
      resp.sendRedirect(oAuthRequest.getLocationUri());
    } catch (OAuthSystemException e) {
      log.severe(e.getLocalizedMessage());
    }
  }
}
