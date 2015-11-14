package online.draughts.rus.server.websocket.game;

import com.google.inject.Inject;
import online.draughts.rus.server.config.CustomConfigurator;
import online.draughts.rus.server.service.GameMessageService;
import online.draughts.rus.server.service.GameService;
import online.draughts.rus.server.service.PlayerService;
import online.draughts.rus.server.utils.Utils;
import online.draughts.rus.server.websocket.game.message.GameMessageDecoder;
import online.draughts.rus.server.websocket.game.message.GameMessageEncoder;
import online.draughts.rus.shared.model.Game;
import online.draughts.rus.shared.model.GameMessage;
import online.draughts.rus.shared.model.Move;
import online.draughts.rus.shared.model.Player;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.12.14
 * Time: 12:05
 */
@ServerEndpoint(value = "/ws/game",
    decoders = {GameMessageDecoder.class},
    encoders = {GameMessageEncoder.class},
    configurator = CustomConfigurator.class
)
public class ServerWebsocket {

  private static Map<Player, Session> peers = Collections.synchronizedMap(new HashMap<Player, Session>());
  private final long MAX_IDLE_TIMEOUT = 1000 * 60 * 15;
  private PlayerService playerService;
  private GameMessageService gameMessageService;
  private GameService gameService;

  @Inject
  ServerWebsocket(PlayerService playerService,
                  GameService gameService,
                  GameMessageService gameMessageService) {
    this.playerService = playerService;
    this.gameService = gameService;
    this.gameMessageService = gameMessageService;
  }

  @OnOpen
  public void onOpen(Session session) {
    System.out.println("New connection " + session.getId());
    session.setMaxIdleTimeout(MAX_IDLE_TIMEOUT);
  }

  @OnMessage
  public void onMessage(Session session, GameMessage gameMessage) {
    if (gameMessage == null) {
      return;
    }

    System.out.println(gameMessage.toString());
    switch (gameMessage.getMessageType()) {
      case PLAYER_REGISTER:
        handleNewPlayer(gameMessage, session);
        break;
      case CHAT_MESSAGE:
        handleChatMessage(session, gameMessage);
        break;
      case USER_LIST_UPDATE:
        updatePlayerList(session);
        break;
      case PLAY_INVITE:
      case PLAY_REJECT_INVITE:
      case PLAY_ALREADY_PLAYING:
      case PLAY_START:
      case PLAY_MOVE:
      case PLAY_PROPOSE_DRAW:
      case PLAY_CANCEL_MOVE:
      case PLAY_CANCEL_MOVE_RESPONSE:
      case PLAY_CALLBACK:
      case CHAT_PRIVATE_MESSAGE:
      case NOTIFICATION_ADDED_TO_FAVORITE:
        handleChatPrivateMessage(gameMessage);
        break;
      case PLAY_END:
      case PLAY_SURRENDER:
      case PLAY_ACCEPT_DRAW:
        handleGameOver(gameMessage);
        break;
    }
  }

  private void handleGameOver(GameMessage gameMessage) {
    Game game = gameMessage.getGame();
    if (game == null) {
      return;
    }

    Player black = playerService.find(game.getPlayerBlack().getId());
    Player white = playerService.find(game.getPlayerWhite().getId());

    black.setPlaying(false);
    white.setPlaying(false);

    playerService.saveOrCreateOnServer(black);
    playerService.saveOrCreateOnServer(white);

    handleChatPrivateMessage(gameMessage);
  }

  private void handleChatMessage(Session session, GameMessage message) {
    for (Session s : session.getOpenSessions()) {
      if (s.isOpen()) {
        sendMessage(s, message);
      }
    }
  }

  private void handleNewPlayer(GameMessage message, Session session) {
    Player player = message.getSender();
    final Long playerId = player.getId();
    if (!peers.isEmpty() && playerId != null) {
      for (Player sh : peers.keySet()) {
        if (playerId.equals(sh.getId())) {
          return;
        }
      }
    }

    player = playerService.find(playerId);

    player.setOnline(true);
    playerService.saveOrCreateOnServer(player);

    peers.put(player, session);
    System.out.println("Register new player: " + player.getId() + " " + session.getId());
    updatePlayerList(session);
  }

  @OnClose
  public void onClose(Session session) {
    Player player = null;
    for (Player sh : peers.keySet()) {
      if (peers.get(sh).equals(session)) {
        player = sh;
        break;
      }
    }
    if (player == null) {
      return;
    }
    player = playerService.find(player.getId());

    if (player.isPlaying()) {
      Game game = gameService.findUserGames(player.getId(), 0, 1).get(0);
      final boolean isPlayerHasWhiteColor = game.getPlayerWhite().getId().equals(player.getId());
      GameMessage gameMessage = new GameMessage();
      final Player secondPlayer = isPlayerHasWhiteColor ? game.getPlayerBlack() : game.getPlayerWhite();

      secondPlayer.setPlaying(false);
      playerService.saveOrCreateOnServer(secondPlayer);

      gameMessage.setReceiver(secondPlayer);
      gameMessage.setMessageType(GameMessage.MessageType.PLAY_END);
      Session receiverSession = peers.get(secondPlayer);
      sendMessage(receiverSession, gameMessage);

      gameMessage = new GameMessage();
      gameMessage.setSender(player);
      gameMessage.setReceiver(secondPlayer);
      gameMessage.setMessageType(GameMessage.MessageType.PLAY_CALLBACK);
      sendMessage(receiverSession, gameMessage);
    }

    player.setOnline(false);
    player.setPlaying(false);
    playerService.saveOrCreateOnServer(player);

    System.out.println("Disconnected: " + player.getId() + " " + session.getId());
    peers.remove(player);
    updatePlayerList(session);
  }

  @OnError
  public void onError(Throwable throwable) {
    throwable.printStackTrace();
  }

  private void handleChatPrivateMessage(GameMessage message) {
    Player receiver = message.getReceiver();
    if (receiver == null) {
      return;
    }
    Player player = null;
    for (Player p : peers.keySet()) {
      if (receiver.getId().equals(p.getId())) {
        player = p;
        break;
      }
    }
    if (player == null) {
      return;
    }
    Session session = peers.get(player);
    if (session == null) {
      return;
    }

    saveGameMessage(message);
    sendMessage(session, message);
  }

  private void saveGameMessage(GameMessage message) {
    Player playerReceiver = playerService.find(message.getReceiver().getId());
    Player playerSender = playerService.find(message.getSender().getId());
    Game game = message.getGame() != null ? gameService.find(message.getGame().getId()) : null;

    GameMessage gameMessage = new GameMessage();

    gameMessage.setMessageType(message.getMessageType());
    gameMessage.setData(message.getData());
    gameMessage.setMessage(message.getMessage());

    gameMessage.setGame(game);

    if (message.getMove() != null) {
      gameMessage.setMove(new Move(message.getMove()));
      gameMessage.getMove().setGameMessage(gameMessage);
    }

    gameMessage.setReceiver(playerReceiver);
    gameMessage.setSender(playerSender);

    gameMessage.setSentDate(new Date());

    gameMessageService.saveOrCreate(gameMessage);
  }

  private void updatePlayerList(Session session) {
    GameMessage gameMessage = new GameMessage();
    gameMessage.setMessageType(GameMessage.MessageType.USER_LIST_UPDATE);
    List<Player> playerList = playerService.findLoggedIn();
    gameMessage.setPlayerList(playerList);
    gameMessageService.saveOrCreate(gameMessage);
    for (Session s : peers.values()) {
      if (s.isOpen()) {
        sendMessage(s, gameMessage);
      }
    }
  }

  private void sendMessage(Session session, GameMessage message) {
    RemoteEndpoint.Basic remote = session.getBasicRemote();

    if (remote != null) {
      try {
        remote.sendText(Utils.serializeGameMessageToJson(message), true);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}