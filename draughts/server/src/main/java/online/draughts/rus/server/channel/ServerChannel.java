package online.draughts.rus.server.channel;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
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
import online.draughts.rus.shared.dto.GameDto;
import online.draughts.rus.shared.dto.GameMessageDto;
import online.draughts.rus.shared.util.StringUtils;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.12.14
 * Time: 12:05
 */
@Singleton
public class ServerChannel {

  private final Logger logger = Logger.getLogger(ServerChannel.class);

  private final CoreChannel coreChannel;
  private final PlayerService playerService;
  private final GameMessageService gameMessageService;
  private final GameService gameService;
  private final Provider<Boolean> authProvider;
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

  public void onJoin(String token, String channelName) {
    logger.info("A new player joined: " + channelName);
  }

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
      case USER_LIST_UPDATE:
        updatePlayerList();
        break;
      case PLAY_INVITE:
      case PLAY_REJECTED_INVITE:
      case PLAY_DID_NOT_RESPONSE:
      case PLAY_ALREADY_PLAYING:
      case PLAY_START:
      case PLAY_OPPONENT_MOVE:
      case PLAY_SAVE_MOVE:
      case PLAY_PROPOSE_DRAW:
      case PLAY_CANCEL_MOVE:
      case PLAY_CANCEL_MOVE_RESPONSE:
      case CHAT_PRIVATE_MESSAGE:
      case NOTIFICATION_ADDED_TO_FAVORITE:
        handleChatPrivateMessage(gameMessage);
        break;
      case PLAY_GAME_UPDATE:
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
    if (GameMessageDto.GAME_UPDATE.equals(gameMessage.getData())) {
      handleChatPrivateMessage(gameMessage);
      return;
    }
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

  public void handleClose(String channel) {
    Player player = playerService.find(Long.valueOf(channel));

    if (player.isPlaying()) {
      List<Game> userGames = gameService.findUserGames(player.getId(), 0, 1);
      if (!userGames.isEmpty()) {
        Game game = userGames.get(0);
        final boolean isPlayerHasWhiteColor = game.getPlayerWhite().getId() == player.getId();
        game.setPlayEndStatus(isPlayerHasWhiteColor ? GameDto.GameEnd.WHITE_LEFT : GameDto.GameEnd.BLACK_LEFT);
        GameMessage gameMessage = new GameMessage();
        gameMessage.setGame(game);
        final long secondPlayerId = isPlayerHasWhiteColor ? game.getPlayerBlack().getId() : game.getPlayerWhite().getId();
        Player secondPlayer = playerService.find(secondPlayerId);

        secondPlayer.setPlaying(false);
        playerService.save(secondPlayer);

        gameMessage.setReceiver(secondPlayer);
        gameMessage.setMessageType(GameMessageDto.MessageType.PLAY_GAME_UPDATE);
        gameMessage.setData(GameMessageDto.GAME_END);
        coreChannel.sendMessage(String.valueOf(secondPlayer.getId()), gameMessage);
      }
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
    if (message.getReceiver() == null || message.getSender() == null) {
      mailService.sendToAdmins(message);
      return;
    }
    long receiverId = message.getReceiver().getId();

    final String receiverChannel = String.valueOf(receiverId);

    Player receiver = playerService.find(receiverId);
    message = saveGameMessage(message);
    final boolean chatPrivateMessage = GameMessageDto.MessageType.CHAT_PRIVATE_MESSAGE.equals(message.getMessageType());
    if (chatPrivateMessage) {
      mailService.sendNotification(message);
    }
    if (receiver.isOnline()) {
      coreChannel.sendMessage(receiverChannel, message);
      if (GameMessageDto.MessageType.PLAY_INVITE.equals(message.getMessageType())) {
        Map<String, String> inviteToPlay = new HashMap<>();
        inviteToPlay.put("ru", "Вас позвали играть в шашки!");
        inviteToPlay.put("en", "You have a new challenge!");
        Utils.sendPushNotification(receiver.getNotificationsUserId(), inviteToPlay);
      }
    }
    if (!receiver.isOnline() && chatPrivateMessage) {
      Map<String, String> newMessage = new HashMap<>();
      newMessage.put("ru", "Вам новое сообщение!");
      newMessage.put("en", "You have a new message!");
      Utils.sendPushNotification(receiver.getNotificationsUserId(), newMessage);
    }
  }

  private GameMessage saveGameMessage(GameMessage message) {
    message.setSentDate(new Date());

    if (null != message.getMove()) {
      Move move = gameMessageService.saveMove(message.getMove());
      message.setMove(move);
    }
    message = gameMessageService.save(message);

    return message;
  }

  private void updatePlayerList() {
    coreChannel.updatePlayerList();
  }
}
