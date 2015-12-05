package online.draughts.rus.server.channel;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.common.base.Splitter;
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
import online.draughts.rus.shared.channel.Chunk;
import online.draughts.rus.shared.dto.GameMessageDto;
import online.draughts.rus.shared.util.StringUtils;
import org.dozer.Mapper;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.12.14
 * Time: 12:05
 */
@Singleton
public class ServerChannel extends ChannelServer {

  private static Map<String, String> channelTokenPeers = Collections.synchronizedMap(new HashMap<String, String>());
  private final PlayerService playerService;
  private final GameMessageService gameMessageService;
  private final GameService gameService;
  private final Provider<Boolean> authProvider;
  private final Logger logger;
  private final MailService mailService;
  private final Mapper mapper;

  @Inject
  ServerChannel(PlayerService playerService,
                GameService gameService,
                GameMessageService gameMessageService,
                @Named(AuthUtils.AUTHENTICATED) Provider<Boolean> authProvider,
                Logger logger,
                MailService mailService,
                Mapper mapper) {
    this.playerService = playerService;
    this.gameService = gameService;
    this.gameMessageService = gameMessageService;
    this.authProvider = authProvider;
    this.mailService = mailService;
    this.logger = logger;
    this.mapper = mapper;
  }

  @Override
  public void onJoin(String token, String channelName) {
    channelTokenPeers.put(channelName, token);
  }

  @Override
  public void onMessage(String token, String channelName, String message) {
    if (!authProvider.get()) {
      throw new RuntimeException("Access denied");
    }
    if (StringUtils.isEmpty(message)) {
      return;
    }
    GameMessageDto gameMessageDto = null;
    try {
      gameMessageDto = (GameMessageDto) Utils.deserializeFromJson(message, GameMessageDto.class);
    } catch (IOException e) {
      logger.severe(e.getLocalizedMessage());
    }
    if (gameMessageDto == null) {
      return;
    }

    GameMessage gameMessage = mapper.map(gameMessageDto, GameMessage.class);
    // обработка сообщений
    switch (gameMessage.getMessageType()) {
      case PLAYER_REGISTER:
        handlePlayerConnect(gameMessage, token);
        break;
      case USER_LIST_UPDATE:
        updatePlayerList();
        break;
      case PLAY_INVITE:
      case PLAY_REJECT_INVITE:
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
        handleGameOver(gameMessage);
        break;
      case CHANNEL_CLOSE:
        onClose(channelName);
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

  private void handlePlayerConnect(GameMessage message, String token) {
    Player player = playerService.find(message.getSender().getId());

    player.setOnline(true);
    playerService.save(player);

    final String channel = String.valueOf(player.getId());
    channelTokenPeers.put(channel, token);
    updatePlayerList();

//    if (player.getUnreadMessages() != 0) {
//      Queue<GameMessage> messageQueue = playerChatMessageQueue.get(playerId);
//      for (GameMessage gameMessage : messageQueue) {
//        sendMessage(channel, gameMessage);
//      }
//    }
  }

  public void onClose(String chanelName) {
    String token = channelTokenPeers.get(chanelName);
    if (token == null) {
      return;
    }
    Player player = playerService.find(Long.valueOf(chanelName));

    if (player.isPlaying()) {
      Game game = gameService.findUserGames(player.getId(), 0, 1).get(0);
      final boolean isPlayerHasWhiteColor = game.getPlayerWhite().getId() == player.getId();
      GameMessage gameMessage = new GameMessage();
      final long secondPlayerId = isPlayerHasWhiteColor ? game.getPlayerBlack().getId() : game.getPlayerWhite().getId();
      Player secondPlayer = playerService.find(secondPlayerId);

      secondPlayer.setPlaying(false);
      playerService.save(secondPlayer);

      gameMessage.setReceiver(secondPlayer);
      gameMessage.setMessageType(GameMessage.MessageType.PLAY_END);
      sendMessage(String.valueOf(secondPlayer.getId()), gameMessage);
    }

    player.setOnline(false);
    player.setPlaying(false);
    playerService.save(player);

    channelTokenPeers.remove(chanelName);
    updatePlayerList();
  }

  private void handleChatPrivateMessage(GameMessage message) {
    long receiverId = message.getReceiver().getId();
    long senderId = message.getSender().getId();

    final String receiverChannel = String.valueOf(receiverId);

    Player receiver = playerService.find(receiverId);
    message = saveGameMessage(message);
    if (GameMessage.MessageType.CHAT_PRIVATE_MESSAGE.equals(message.getMessageType())) {
      mailService.sendNotification(message, senderId, receiver);
    }
    if (receiver.isOnline()) {
      sendMessage(receiverChannel, message);
    }
  }

  private GameMessage saveGameMessage(GameMessage message) {
    Player playerReceiver = playerService.find(message.getReceiver().getId());
    Player playerSender = playerService.find(message.getSender().getId());
    GameMessage gameMessage = new GameMessage();

    if (message.getGame() != null) {
      Game game = gameService.find(message.getGame().getId());
      gameMessage.setGame(game);
    }

    gameMessage.setMessageType(message.getMessageType());
    gameMessage.setData(message.getData());
    gameMessage.setMessage(message.getMessage());


    if (message.getMove() != null) {
      gameMessage.setMove(new Move(message.getMove()));
      gameMessage.getMove().setGameMessage(gameMessage);
    }

    gameMessage.setReceiver(playerReceiver);
    gameMessage.setSender(playerSender);

    gameMessage.setSentDate(new Date());

    gameMessageService.saveOrCreate(gameMessage);
    return gameMessage;
  }

  private void updatePlayerList() {
    GameMessage gameMessage = new GameMessage();
    gameMessage.setMessageType(GameMessage.MessageType.USER_LIST_UPDATE);
    List<Player> playerList = playerService.findAll();
    gameMessage.setPlayerList(playerList);
    gameMessageService.saveOrCreate(gameMessage);
    for (String channelName : channelTokenPeers.keySet()) {
      sendMessage(channelName, gameMessage);
    }
  }

  private void sendMessage(String channel, GameMessage message) {
    GameMessageDto dto = mapper.map(message, GameMessageDto.class);
    final String serialized = Utils.serializeToJson(dto);
    List<String> chunks = Splitter.fixedLength(1024 * 31).splitToList(serialized);
    for (int i = 0; i < chunks.size(); i++) {
      send(channel, Utils.serializeToJson(new Chunk(chunks.size(), i + 1, chunks.get(i))));
    }
  }

  private void send(String channel, String chunk) {
    ChannelServiceFactory.getChannelService().sendMessage(new ChannelMessage(channel, chunk));
  }
}
