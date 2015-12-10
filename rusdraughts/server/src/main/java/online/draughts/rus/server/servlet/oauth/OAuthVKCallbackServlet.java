package online.draughts.rus.server.servlet.oauth;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import online.draughts.rus.server.config.ServerConfiguration;
import online.draughts.rus.server.domain.Player;
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
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import javax.json.Json;
import javax.json.JsonArray;
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
public class OAuthVKCallbackServlet extends HttpServlet {

  private final ServerConfiguration config;
  private final PlayerService playerService;
  private Logger log;


  @Inject
  public OAuthVKCallbackServlet(Logger log,
                                ServerConfiguration config,
                                PlayerService playerService) {
    this.log = log;
    this.config = config;
    this.playerService = playerService;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
      OAuthAuthzResponse oAuthAuthzResponse = OAuthAuthzResponse.oauthCodeAuthzResponse(req);
      String code = oAuthAuthzResponse.getCode();

      OAuthClientRequest request = OAuthClientRequest
          .tokenLocation(config.getVkTokenUri())
          .setClientId(config.getVkClientId())
          .setClientSecret(config.getVkClientSecret())
          .setRedirectURI(config.getVkRedirectUri())
          .setScope(config.getVkScope())
          .setCode(code)
          .buildQueryMessage();

      OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
      OAuthAccessTokenResponse response = oAuthClient.accessToken(request, OAuthJSONAccessTokenResponse.class);
      String accessToken = response.getAccessToken();
      Long expiresIn = response.getExpiresIn();
      String user_id = response.getParam("user_id");
      String email = response.getParam("email");

      if (StringUtils.isEmpty(accessToken)) {
        resp.sendRedirect(config.getServerErrorUrl());
        return;
      }

      OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(config.getVkApiUserInfo())
          .setAccessToken(accessToken).buildQueryMessage();

      OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest,
          OAuth.HttpMethod.GET,
          OAuthResourceResponse.class);

      if (resourceResponse.getResponseCode() != 200) {
        resp.sendRedirect(config.getServerErrorUrl());
        return;
      }

      Player player = playerService.findByVkId(user_id);
      if (player == null) {
        final ByteArrayInputStream inBody = new ByteArrayInputStream(resourceResponse.getBody().getBytes(StandardCharsets.UTF_8));
        JsonReader jsonReader = Json.createReader(inBody);
        JsonObject responseObject = jsonReader.readObject();
        JsonArray usersArray = responseObject.getJsonArray("response");
        JsonObject array = usersArray.getJsonObject(0);
        String firstName = array.getString("first_name");
        String lastName = array.getString("last_name");

        player = new Player();
        player.setVkId(user_id);
        player.setAuthProvider(PlayerDto.AuthProvider.VK);
        player.setFirstName(firstName);
        player.setLastName(lastName);
        if (StringUtils.isNotEmpty(email)) {
          player.setEmail(email);
        }
        player.setActive(true);
      }

      AuthUtils.processUserAndRedirectToPlayPage(playerService, config, req, resp, player);
    } catch (OAuthSystemException | OAuthProblemException e) {
      log.severe(e.getLocalizedMessage());
    }
  }
}
