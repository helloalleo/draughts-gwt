package online.draughts.rus.server.channel;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import no.eirikb.gwtchannelapi.server.ChannelServer;
import online.draughts.rus.server.domain.Game;
import online.draughts.rus.server.domain.GameMessage;
import online.draughts.rus.server.domain.Move;
import online.draughts.rus.server.domain.Player;
import online.draughts.rus.server.service.GameMessageService;
import online.draughts.rus.server.service.GameService;
import online.draughts.rus.server.service.MailService;
import online.draughts.rus.server.service.PlayerService;
import online.draughts.rus.server.util.AuthUtils;
import online.draughts.rus.server.util.Utils;
import online.draughts.rus.shared.dto.GameMessageDto;
import online.draughts.rus.shared.util.StringUtils;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.12.14
 * Time: 12:05
 */
@Singleton
public class ServerChannel extends ChannelServer {

  private final CoreChannel coreChannel;
  private final PlayerService playerService;
  private final GameMessageService gameMessageService;
  private final GameService gameService;
  private final Provider<Boolean> authProvider;
  private final Logger logger = Logger.getLogger(ServerChannel.class);
  private final MailService mailService;

  @Inject
  ServerChannel(CoreChannel coreChannel,
                PlayerService playerService,
                GameService gameService,
                GameMessageService gameMessageService,
                @Named(AuthUtils.AUTHENTICATED) Provider<Boolean> authProvider,
                MailService mailService) {
    this.coreChannel = coreChannel;
    this.playerService = playerService;
    this.gameService = gameService;
    this.gameMessageService = gameMessageService;
    this.authProvider = authProvider;
    this.mailService = mailService;
  }

  @Override
  public void onJoin(String token, String channelName) {
    coreChannel.joinPlayer(token, channelName);
  }

  @Override
  public void onMessage(String token, String channel, String message) {
    if (!authProvider.get()) {
      logger.info("Unauthorized access");
      return;
    }
    if (StringUtils.isEmpty(message)) {
      logger.info("Empty message");
      return;
    }
    logger.info("New message: " + message);
    GameMessage gameMessage = Utils.deserializeFromJson(message, GameMessage.class);
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
      case PLAY_INVITE:
      case PLAY_REJECTED_INVITE:
      case PLAY_DID_NOT_RESPONSE:
      case PLAY_ALREADY_PLAYING:
      case PLAY_START:
      case PLAY_OPPONENT_MOVE:
      case PLAY_PROPOSE_DRAW:
      case PLAY_CANCEL_MOVE:
      case PLAY_CANCEL_MOVE_RESPONSE:
      case CHAT_PRIVATE_MESSAGE:
      case NOTIFICATION_ADDED_TO_FAVORITE:
        handleChatPrivateMessage(gameMessage);
        break;
      case PLAY_END:
      case PLAY_SURRENDER:
      case PLAY_ACCEPT_DRAW:
      case PLAY_TIMEOUT:
        handleGameOver(gameMessage);
        break;
      case CHANNEL_CLOSE:
        handleClose(channel);
        break;
    }
  }

  private void handleGameOver(GameMessage gameMessage) {
    long gameId = gameMessage.getGame().getId();
    Game game = gameService.find(gameId);

    Player black = playerService.find(game.getPlayerBlack().getId());
    Player white = playerService.find(game.getPlayerWhite().getId());

    black.setPlaying(false);
    white.setPlaying(false);

    playerService.save(black);
    playerService.save(white);

    handleChatPrivateMessage(gameMessage);
  }

  private void handlePlayerConnect(GameMessage gameMessage, String token) {
    coreChannel.connectPlayer(gameMessage, token);
  }

  public void handleClose(String channel) {
    String token = coreChannel.getToken(channel);
    if (token == null) {
      return;
    }
    Player player = playerService.find(Long.valueOf(channel));

    if (player.isPlaying()) {
      Game game = gameService.findUserGames(player.getId(), 0, 1).get(0);
      final boolean isPlayerHasWhiteColor = game.getPlayerWhite().getId() == player.getId();
      GameMessage gameMessage = new GameMessage();
      final long secondPlayerId = isPlayerHasWhiteColor ? game.getPlayerBlack().getId() : game.getPlayerWhite().getId();
      Player secondPlayer = playerService.find(secondPlayerId);

      secondPlayer.setPlaying(false);
      playerService.save(secondPlayer);

      gameMessage.setReceiver(secondPlayer);
      gameMessage.setMessageType(GameMessageDto.MessageType.PLAY_END);
      coreChannel.sendMessage(String.valueOf(secondPlayer.getId()), gameMessage);
    }

    player.setOnline(false);
    player.setPlaying(false);
    playerService.save(player);

    GameMessage gameMessage = new GameMessage();
    gameMessage.setReceiver(player);
    gameMessage.setMessageType(GameMessageDto.MessageType.CHANNEL_CLOSE);
    coreChannel.sendMessage(String.valueOf(player.getId()), gameMessage);

    coreChannel.disconnectChannel(channel);
  }

  private void handleChatPrivateMessage(GameMessage message) {
    long receiverId = message.getReceiver().getId();

    final String receiverChannel = String.valueOf(receiverId);

    Player receiver = playerService.find(receiverId);
    message = saveGameMessage(message);
    if (GameMessageDto.MessageType.CHAT_PRIVATE_MESSAGE.equals(message.getMessageType())) {
      mailService.sendNotification(message);
    }
    if (receiver.isOnline()) {
      coreChannel.sendMessage(receiverChannel, message);
    }
  }

  private GameMessage saveGameMessage(GameMessage message) {
    message.setSentDate(new Date());

    Move move = null;
    if (null != message.getMove()) {
       move = gameMessageService.saveMove(message.getMove());
    }
    message.setMove(move);
    message = gameMessageService.save(message);

    return message;
  }

  public void updatePlayerList() {
    coreChannel.updatePlayerList();
  }
}