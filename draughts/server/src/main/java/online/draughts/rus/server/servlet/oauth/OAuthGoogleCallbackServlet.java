package online.draughts.rus.server.servlet.oauth;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import online.draughts.rus.server.config.Config;
import online.draughts.rus.server.domain.Player;
import online.draughts.rus.shared.exception.BannedException;
import online.draughts.rus.server.service.PlayerService;
import online.draughts.rus.server.util.AuthUtils;
import online.draughts.rus.shared.dto.PlayerDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 16.10.15
 * Time: 7:54
 */
@Singleton
public class OAuthGoogleCallbackServlet extends HttpServlet {

  private final PlayerService playerService;
  private Logger log;

  @Inject
  public OAuthGoogleCallbackServlet(Logger log,
                                    PlayerService playerService) {
    this.log = log;
    this.playerService = playerService;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
      OAuthAuthzResponse oAuthAuthzResponse = OAuthAuthzResponse.oauthCodeAuthzResponse(req);
      String code = oAuthAuthzResponse.getCode();

      OAuthClientRequest request = OAuthClientRequest
          .tokenProvider(OAuthProviderType.GOOGLE)
          .setGrantType(GrantType.AUTHORIZATION_CODE)
          .setClientId(Config.GOOGLE_CLIENT_ID)
          .setClientSecret(Config.GOOGLE_CLIENT_SECRET)
          .setRedirectURI(Config.GOOGLE_REDIRECT_URI)
          .setScope(Config.GOOGLE_SCOPE)
          .setCode(code)
          .buildBodyMessage();

      log.info("Request Token Uri: " + request.getLocationUri());
      OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
      OAuthAccessTokenResponse response = oAuthClient.accessToken(request, OAuthJSONAccessTokenResponse.class);
      String accessToken = response.getAccessToken();
      System.out.println(response);

      if (StringUtils.isEmpty(accessToken)) {
        resp.sendRedirect(Config.SERVER_ERROR_URL);
        return;
      }

      OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(Config.GOOGLE_API_USER_INFO)
          .setAccessToken(accessToken).buildQueryMessage();

      OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest,
          OAuth.HttpMethod.GET,
          OAuthResourceResponse.class);

      if (resourceResponse.getResponseCode() != 200) {
        resp.sendRedirect(Config.SERVER_ERROR_URL);
        return;
      }

      final ByteArrayInputStream inBody = new ByteArrayInputStream(resourceResponse.getBody().getBytes(StandardCharsets.UTF_8));
      JsonReader jsonReader = Json.createReader(inBody);
      JsonObject responseObject = jsonReader.readObject();
      String user_id = responseObject.getString("sub");
      if (StringUtils.isNoneEmpty(user_id)) {
        Player player = null;
        try {
          player = AuthUtils.check(playerService.findByGoogleSub(user_id));
        } catch (BannedException e) {
          req.getSession().invalidate();
          resp.sendRedirect("/");
          return;
        }
        if (player == null) {
          player = new Player();
          player.setGoogleSub(user_id);
          player.setAuthProvider(PlayerDto.AuthProvider.GOOGLE);
          player.setActive(true);
        }
        String given_name = responseObject.getString("given_name");
        player.setFirstName(given_name);
        String family_name = responseObject.getString("family_name");
        player.setLastName(family_name);
        String picture = responseObject.getString("picture") + "?sz=50";
        player.setAvatar(picture);
        String email = responseObject.getString("email");
        player.setEmail(email);

        AuthUtils.processUserAndRedirectToPlayPage(playerService, req, resp, player);
      }
    } catch (OAuthSystemException | OAuthProblemException e) {
      log.severe(e.getLocalizedMessage());
    }
  }
}
