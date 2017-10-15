package online.draughts.rus.server.channel;

import com.google.appengine.api.channel.ChannelPresence;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 12.06.16
 * Time: 17:10
 */
@Singleton
public class ChannelPresenceServlet extends HttpServlet {

  private final Logger logger = Logger.getLogger(ServerChannel.class);

  @Inject
  private CoreChannel coreChannel;

  private String getGameUriWithGameParam(HttpServletRequest request, String gameKey) {
    try {
      String query = "";
      if (gameKey != null) {
        query = "gameKey=" + gameKey;
      }
      URI thisUri = new URI(request.getRequestURL().toString());
      URI uriWithOptionalGameParam = new URI(
          thisUri.getScheme(), thisUri.getUserInfo(), thisUri.getHost(),
          thisUri.getPort(), thisUri.getPath(), query, "");
      return uriWithOptionalGameParam.toString();
    } catch (URISyntaxException e) {
      // This should never happen, since we're constructing the URI from a valid URI.
      // Nonetheless, wrap it in a RuntimeException to placate java.
      throw new RuntimeException(e);
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String clientId = request.getParameter("clientId");

    // 1. Create or fetch a Game object from the datastore
    String userId = UserServiceFactory.getUserService().getCurrentUser().getUserId();
    if (StringUtils.isNotBlank(clientId)) {
      coreChannel.connectPlayer(clientId);
    } else {
      // Initialize a new board. The board is represented as a String of 9 spaces, one for each
      // blank spot on the tic-tac-toe board.
    }
    coreChannel.initFirebase(getServletContext());

    // 2. Create this Game in the firebase db
//    game.sendUpdateToClients(getServletContext());

    // 3. Inject a secure token into the client, so it can get game updates

    // [START pass_token]
    // The 'Game' object exposes a method which creates a unique string based on the game's key
    // and the user's id.
    String token = coreChannel.createFirebaseToken(clientId);
    request.setAttribute("token", token);

    // 4. More general template values
//    request.setAttribute("game_key", gameKey);
//    request.setAttribute("me", userId);
//    request.setAttribute("channel_id", game.getChannelKey(userId));
//    request.setAttribute("initial_message", new Gson().toJson(game));
//    request.setAttribute("game_link", getGameUriWithGameParam(request, gameKey));
//    request.getRequestDispatcher("/WEB-INF/view/index.jsp").forward(request, response);
    // [END pass_token]
    response.setStatus(HttpServletResponse.SC_OK);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    ChannelService channelService = ChannelServiceFactory.getChannelService();
    ChannelPresence channelPresence = channelService.parsePresence(req);

    String clientId = channelPresence.clientId();
    if (channelPresence.isConnected()) {
      coreChannel.connectPlayer(clientId);
    } else {
      coreChannel.disconnectChannel(clientId);
    }
    logger.info("Client ID: " + channelPresence.clientId() + ", Connected: " + channelPresence.isConnected());

    resp.setStatus(HttpServletResponse.SC_OK);
  }
}
