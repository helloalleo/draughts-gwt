package online.shashki.rus.server.servlet.oauth;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeCallbackServlet;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import online.shashki.rus.server.config.ServerConfiguration;
import online.shashki.rus.server.rest.PlayersResourceImpl;
import online.shashki.rus.server.utils.AuthUtils;
import online.shashki.rus.server.utils.Utils;
import online.shashki.rus.shared.model.Player;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 16.11.14
 * Time: 15:55
 */
@Singleton
public class OAuthVKCallbackServlet extends AbstractAuthorizationCodeCallbackServlet {

  private final PlayersResourceImpl playersResource;

  private final ServerConfiguration serverConfiguration;

  private final List<String> scope = Collections.singletonList("email");

  @Inject
  OAuthVKCallbackServlet(PlayersResourceImpl playersResource, ServerConfiguration serverConfiguration) {
    this.playersResource = playersResource;
    this.serverConfiguration = serverConfiguration;
  }

  @Override
  protected void onSuccess(HttpServletRequest req, HttpServletResponse resp, Credential credential) throws ServletException, IOException {
    String accessToken = credential.getAccessToken();
    GenericUrl url = new GenericUrl(serverConfiguration.getVkApiUserInfo());
    url.set("access_token", accessToken);

    HttpRequest request = Utils.HTTP_TRANSPORT.createRequestFactory().buildGetRequest(url);
    HttpResponse response = request.execute();
    InputStream inputStream = response.getContent();
    JsonReader jsonReader = Json.createReader(inputStream);
    JsonObject responseObject = jsonReader.readObject();

    if (responseObject.getJsonObject("error") != null) {
      resp.sendRedirect("/rus/500.html");
      return;
    }

    if (responseObject.getJsonArray("response").isEmpty()) {
      resp.sendRedirect("/rus/404.html");
      return;
    }

    JsonArray usersArray = responseObject.getJsonArray("response");
    JsonObject array = usersArray.getJsonObject(0);
    JsonNumber uid = array.getJsonNumber("uid");
    String vkUid = uid.toString();

    Player player = playersResource.findByVkUid(vkUid);
    if (player == null) {
      JsonString firstName = array.getJsonString("first_name");
      JsonString lastName = array.getJsonString("last_name");
      player = new Player();
      player.setVkUid(vkUid);
      player.setFirstName(firstName.getString());
      player.setLastName(lastName.getString());
    } else {
      player.setVisitCounter(player.getVisitCounter() + 1);
    }
    player.setLoggedIn(true);
    player.setPlaying(false);
    player.setOnline(false);

    HttpSession session = req.getSession();
    if (player.getSessionId() == null
        || !player.getSessionId().equals(session.getId())) {
      player.setSessionId(session.getId());
    }
    playersResource.saveOrCreate(player, true);

    AuthUtils.login(req);
    resp.sendRedirect(serverConfiguration.getContext());
  }

  @Override
  protected void onError(HttpServletRequest req, HttpServletResponse resp, AuthorizationCodeResponseUrl errorResponse)
      throws ServletException, IOException {
    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
  }

  @Override
  protected AuthorizationCodeFlow initializeFlow() throws ServletException, IOException {
    ClientSecrets clientSecrets = new ClientSecrets(serverConfiguration, ClientSecrets.SocialType.VK);
    return Utils.getFlow(clientSecrets, scope);
  }

  @Override
  protected String getRedirectUri(HttpServletRequest httpServletRequest) throws ServletException, IOException {
    GenericUrl url = new GenericUrl(httpServletRequest.getRequestURL().toString());
    url.setRawPath(serverConfiguration.getVkRedirectUri());
    return url.build();
  }

  @Override
  protected String getUserId(HttpServletRequest httpServletRequest) throws ServletException, IOException {
    return httpServletRequest.getSession(true).getId();
  }
}
