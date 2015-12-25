package online.draughts.rus.server.channel;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import no.eirikb.gwtchannelapi.server.ChannelServer;
import online.draughts.rus.server.domain.GameMessage;
import online.draughts.rus.server.service.GameService;
import online.draughts.rus.server.service.PlayerService;
import online.draughts.rus.server.util.Utils;
import online.draughts.rus.shared.util.StringUtils;
import org.apache.log4j.Logger;
import org.dozer.Mapper;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 16.12.15
 * Time: 13:26
 */
@Singleton
public class AnonymousChannel extends ChannelServer {

  private final Logger logger = Logger.getLogger(AnonymousChannel.class);


  private final CoreChannel coreChannel;
  private final PlayerService playerService;
  private final GameService gameService;
  private final Mapper mapper;

  @Inject
  public AnonymousChannel(CoreChannel coreChannel,
                          PlayerService playerService,
                          GameService gameService,
                          Mapper mapper) {
    this.coreChannel = coreChannel;
    this.playerService = playerService;
    this.gameService = gameService;
    this.mapper = mapper;
  }

  @Override
  protected void onJoin(String token, String channelName) {
    coreChannel.joinPlayer(token, channelName);
  }

  @Override
  public void onMessage(String token, String channel, String message) {
    if (StringUtils.isEmpty(message)) {
      logger.info("Empty message");
      return;
    }
    logger.info("New message: " + message);
    GameMessage gameMessage = null;
    gameMessage = Utils.deserializeFromJson(message, GameMessage.class);
    if (gameMessage == null) {
      return;
    }

    // обработка сообщений
    switch (gameMessage.getMessageType()) {
      case PLAYER_REGISTER:
        handlePlayerConnect(gameMessage, token);
        break;
      case USER_LIST_UPDATE:
        updatePlayerList();
        break;
      case GAME_LIST_UPDATE:
        updateGameList(gameMessage);
      case PLAY_INVITE:
      case PLAY_REJECT_INVITE:
      case PLAY_ALREADY_PLAYING:
      case PLAY_START:
      case PLAY_OPPONENT_MOVE:
      case PLAY_PROPOSE_DRAW:
      case PLAY_CANCEL_MOVE:
      case PLAY_CANCEL_MOVE_RESPONSE:
        handleChatPrivateMessage(gameMessage);
        break;
      case PLAY_END:
      case PLAY_SURRENDER:
      case PLAY_ACCEPT_DRAW:
        handleGameOver(gameMessage);
        break;
      case CHANNEL_CLOSE:
        handleClose(channel);
        break;
    }
  }

  private void updatePlayerList() {
    coreChannel.updatePlayerList();
  }

  private void handleClose(String channel) {

  }

  private void handleGameOver(GameMessage gameMessage) {

  }

  private void handleChatPrivateMessage(GameMessage gameMessage) {

  }

  private void updateGameList(GameMessage gameMessage) {
    for (String channel : coreChannel.getChannels()) {
      coreChannel.sendMessage(channel, gameMessage);
    }
  }

  private void handlePlayerConnect(GameMessage gameMessage, String token) {
    coreChannel.connectPlayer(gameMessage, token);
  }
}
