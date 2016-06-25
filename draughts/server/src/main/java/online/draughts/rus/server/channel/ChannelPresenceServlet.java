package online.draughts.rus.server.channel;

import com.google.appengine.api.channel.ChannelPresence;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
