package online.shashki.rus.server.websocket.game;

import com.google.inject.Inject;
import online.shashki.rus.server.guice.CustomConfigurator;
import online.shashki.rus.server.service.GameMessageService;
import online.shashki.rus.server.rest.GamesResourceImpl;
import online.shashki.rus.server.service.PlayerService;
import online.shashki.rus.server.utils.Utils;
import online.shashki.rus.server.websocket.game.message.GameMessageDecoder;
import online.shashki.rus.server.websocket.game.message.GameMessageEncoder;
import online.shashki.rus.shared.model.Game;
import online.shashki.rus.shared.model.GameMessage;
import online.shashki.rus.shared.model.Move;
import online.shashki.rus.shared.model.Player;

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
public class GameWebsocket {

  private static Map<Player, Session> peers = Collections.synchronizedMap(new HashMap<Player, Session>());
  private final long MAX_IDLE_TIMEOUT = 1000 * 60 * 15;
  private PlayerService playerService;
  private GameMessageService gameMessageService;
  private GamesResourceImpl gameResource;

  @Inject
  GameWebsocket(GamesResourceImpl gameResource,
                        PlayerService playerService,
                        GameMessageService gameMessageService) {
    this.gameResource = gameResource;
    this.playerService = playerService;
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
      case PLAY_END:
      case PLAY_MOVE:
      case PLAY_SURRENDER:
      case PLAY_PROPOSE_DRAW:
      case PLAY_ACCEPT_DRAW:
      case PLAY_CANCEL_MOVE:
      case PLAY_CANCEL_MOVE_RESPONSE:
      case CHAT_PRIVATE_MESSAGE:
        handleChatPrivateMessage(gameMessage);
        break;
    }
  }

  private void handlePlayMoveMessage(GameMessage gameMessage) {

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
    playerService.saveOrCreate(null, player, true);

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

    player.setOnline(false);
    player.setPlaying(false);
    playerService.saveOrCreate(null, player, true);

    System.out.println("Disconnected: " + player.getId() + " " + session.getId());
    peers.values().remove(session);
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
    for (Player sh : peers.keySet()) {
      if (receiver.getId().equals(sh.getId())) {
        player = sh;
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
    Game game = message.getGame() != null ? gameResource.game(message.getGame().getId()) : null;

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
    List<Player> playerList = playerService.findAll();
    gameMessage.setPlayerList(playerList);
    gameMessageService.saveOrCreate(gameMessage);
    for (Session s : session.getOpenSessions()) {
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
