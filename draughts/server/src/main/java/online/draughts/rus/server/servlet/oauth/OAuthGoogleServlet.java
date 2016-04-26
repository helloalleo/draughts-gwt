package online.draughts.rus.server.servlet.oauth;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import online.draughts.rus.server.config.Config;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 15.10.15
 * Time: 16:51
 */
@Singleton
public class OAuthGoogleServlet extends HttpServlet {

  private final Logger log;

  @Inject
  public OAuthGoogleServlet(Logger log) {
    this.log = log;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
      String state = new BigInteger(130, new SecureRandom()).toString(32);
      OAuthClientRequest oAuthRequest = OAuthClientRequest
          .authorizationProvider(OAuthProviderType.GOOGLE)
          .setClientId(Config.GOOGLE_CLIENT_ID)
          .setScope(Config.GOOGLE_SCOPE)
          .setRedirectURI(Config.GOOGLE_REDIRECT_URI)
          .setResponseType("code")
          .setParameter("state", state)
          .buildQueryMessage();
      log.info("REQUEST URI: " + oAuthRequest.getLocationUri());
      resp.sendRedirect(oAuthRequest.getLocationUri());
    } catch (OAuthSystemException e) {
      log.severe(e.getLocalizedMessage());
    }
  }
}
