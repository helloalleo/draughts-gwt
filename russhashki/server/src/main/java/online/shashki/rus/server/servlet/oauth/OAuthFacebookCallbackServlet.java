package online.shashki.rus.server.servlet.oauth;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import online.shashki.rus.server.config.ServerConfiguration;
import online.shashki.rus.server.service.PlayerService;
import online.shashki.rus.server.utils.AuthUtils;
import online.shashki.rus.shared.model.Player;
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
import org.apache.oltu.oauth2.common.message.types.GrantType;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 16.10.15
 * Time: 7:54
 */
@Singleton
public class OAuthFacebookCallbackServlet extends HttpServlet {

  private final ServerConfiguration config;
  private final PlayerService playerService;
  private Logger log;


  @Inject
  public OAuthFacebookCallbackServlet(Logger log,
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
          .tokenLocation(config.getFbApiGraph() + "/" + config.getFbApiVersion() + "/" + config.getFbApiOAuthPath())
          .setGrantType(GrantType.AUTHORIZATION_CODE)
          .setClientId(config.getFbClientId())
          .setClientSecret(config.getFbClientSecret())
          .setRedirectURI(config.getFbRedirectUri())
          .setScope(config.getFbScope())
          .setCode(code)
          .buildQueryMessage();

      log.info("Request Token Uri: " + request.getLocationUri());
      OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
      OAuthAccessTokenResponse response = oAuthClient.accessToken(request, OAuthJSONAccessTokenResponse.class);
      System.out.println(response.getBody());

      String accessToken = response.getAccessToken();

      if (StringUtils.isEmpty(accessToken)) {
        resp.sendRedirect(config.getServerError());
        return;
      }

      OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(config.getFbApiGraph() + "/me?fields=id,name,email")
          .setAccessToken(accessToken)
          .buildQueryMessage();
      System.out.println(bearerClientRequest.getLocationUri());

      OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest,
          OAuth.HttpMethod.GET,
          OAuthResourceResponse.class);

      System.out.println(resourceResponse.getBody());

      if (resourceResponse.getResponseCode() != 200) {
        resp.sendRedirect(config.getServerError());
        return;
      }

      final ByteArrayInputStream inBody = new ByteArrayInputStream(resourceResponse.getBody().getBytes());
      JsonReader jsonReader = Json.createReader(inBody);
      JsonObject responseObject = jsonReader.readObject();
      String user_id = responseObject.getString("id");
      if (StringUtils.isNoneEmpty(user_id)) {
        System.out.println(user_id);
        Player player = playerService.findByFbId(user_id);
        if (player == null) {
          player = new Player();
          player.setFbId(user_id);
          player.setAuthProvider(Player.AuthProvider.FACEBOOK);
          String name = responseObject.getString("name");
          String[] nameArr = name.split(" ");
          if (nameArr.length > 1) {
            player.setFirstName(nameArr[0]);
            player.setLastName(nameArr[1]);
          } else {
            player.setFirstName(nameArr[0]);
          }
          String email = responseObject.getString("email");
          player.setEmail(email);
        } else {
          player.setVisitCounter(player.getVisitCounter() + 1);
        }

        player.setLastVisited(new Date());
        player.setLoggedIn(true);
        player.setPlaying(false);
        player.setOnline(false);

        HttpSession session = req.getSession();
        if (player.getSessionId() == null
            || !player.getSessionId().equals(session.getId())) {
          player.setSessionId(session.getId());
        }
        playerService.saveOrCreate(req.getSession(), player, true);

        AuthUtils.login(req);
        resp.sendRedirect(config.getHomeUrl());
      }
    } catch (OAuthSystemException | OAuthProblemException e) {
      log.severe(e.getLocalizedMessage());
    }
  }
}
