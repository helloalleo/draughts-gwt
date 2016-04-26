package online.draughts.rus.server.servlet.oauth;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import online.draughts.rus.server.config.Config;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
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
public class OAuthVKServlet extends HttpServlet {

  private final Logger log;

  @Inject
  public OAuthVKServlet(Logger log) {
    this.log = log;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
      OAuthClientRequest oAuthRequest = OAuthClientRequest
          .authorizationLocation(Config.VK_AUTH_URI)
          .setClientId(Config.VK_CLIENT_ID)
          .setScope(Config.VK_SCOPE)
          .setRedirectURI(Config.VK_REDIRECT_URI)
          .setParameter("v", Config.VK_API_VERSION)
          .setResponseType("code")
          .buildQueryMessage();
      log.info("REQUEST URI: " + oAuthRequest.getLocationUri());
      resp.sendRedirect(oAuthRequest.getLocationUri());
    } catch (OAuthSystemException e) {
      log.severe(e.getLocalizedMessage());
    }
  }
}
