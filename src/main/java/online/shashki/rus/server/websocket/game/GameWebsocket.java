package online.shashki.rus.server.websocket.game;

import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import online.shashki.rus.server.service.GameMessageService;
import online.shashki.rus.server.service.GameService;
import online.shashki.rus.server.service.ShashistService;
import online.shashki.rus.server.util.Util;
import online.shashki.rus.server.websocket.game.message.GameMessageDecoder;
import online.shashki.rus.server.websocket.game.message.GameMessageEncoder;
import online.shashki.rus.shared.model.GameMessage;
import online.shashki.rus.shared.model.Shashist;
import online.shashki.rus.shared.model.entity.GameEntity;
import online.shashki.rus.shared.model.entity.GameMessageEntity;
import online.shashki.rus.shared.model.entity.ShashistEntity;
import online.shashki.rus.shared.websocket.message.MessageFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
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
@Singleton
@ServerEndpoint(value = "/ws/game",
    decoders = {GameMessageDecoder.class},
    encoders = {GameMessageEncoder.class}
)
public class GameWebsocket {

  private static Map<Shashist, Session> peers = Collections.synchronizedMap(new HashMap<>());
  private final long MAX_IDLE_TIMEOUT = 1000 * 60 * 15;
  @Inject
  private ShashistService shashistService;
  @Inject
  private GameMessageService gameMessageService;
  @Inject
  private GameService gameService;

  @OnOpen
  public void onOpen(Session session) {
    System.out.println("New connection " + session.getId());
    session.setMaxIdleTimeout(MAX_IDLE_TIMEOUT);
  }

  @OnMessage
  public void onMessage(Session session, GameMessage gameMessage) {
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
      case PLAY_END:
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

  private void handleChatMessage(Session session, GameMessage message) {
    session.getOpenSessions().parallelStream().filter(Session::isOpen).forEach(peer -> {
      sendMessage(peer, message);
    });
  }

  private void handleNewPlayer(GameMessage message, Session session) {
    Shashist shashist = message.getSender();
    if (!peers.isEmpty()) {
      boolean registered = peers.keySet().parallelStream().filter(sh -> sh.getSystemId().equals(shashist.getSystemId())).findAny().isPresent();
      if (registered) {
        return;
      }
    }
    ShashistEntity shashistEntity = shashistService.find(shashist.getId());

    shashistEntity.setLoggedIn(true);
    shashistEntity.setOnline(true);
    shashistService.edit(shashistEntity);

    shashist.setLoggedIn(true);
    shashist.setOnline(true);

    peers.put(shashist, session);
    System.out.println("Register new player: " + shashist.getSystemId() + " " + session.getId());
    updatePlayerList(session);
  }

  @OnClose
  public void onClose(Session session) {
    Shashist shashist = peers.keySet().stream().filter(sh -> peers.get(sh) == session).findFirst().get();
    ShashistEntity shashistEntity = shashistService.find(shashist.getId());

    shashistEntity.setOnline(false);
    shashistEntity.setPlaying(false);
    shashistService.edit(shashistEntity);

    shashist.setOnline(false);
    shashist.setPlaying(false);

    System.out.println("Disconnected: " + shashist.getSystemId() + " " + session.getId());
    peers.values().remove(session);
    updatePlayerList(session);
  }

  @OnError
  public void onError(Throwable throwable) {
    throwable.printStackTrace();
  }

  private void handleChatPrivateMessage(GameMessage message) {
    Shashist receiver = message.getReceiver();
    Shashist shashist = peers.keySet().stream()
        .filter(sh -> sh.getSystemId().equals(receiver.getSystemId())).findFirst().get();
    Session session = peers.get(shashist);
    sendMessage(session, message);

    ShashistEntity shashistReceiver = shashistService.find(message.getReceiver().getId());
    ShashistEntity shashistSender = shashistService.find(message.getSender().getId());
    GameEntity gameEntity = message.getGame() != null ? gameService.find(message.getGame().getId()) : null;

    GameMessageEntity gameMessageEntity = new GameMessageEntity();
    gameMessageEntity.setMessageType(message.getMessageType());
    gameMessageEntity.setData(message.getData());
    gameMessageEntity.setMessage(message.getMessage());

    gameMessageEntity.setGame(gameEntity);
    gameMessageEntity.setMove(message.getMove());

    gameMessageEntity.setReceiver(shashistReceiver);
    gameMessageEntity.setSender(shashistSender);

    gameMessageEntity.setSentDate(new Date());

    gameMessageService.create(gameMessageEntity);
  }

  private void updatePlayerList(Session session) {
    MessageFactory messageFactory = AutoBeanFactorySource.create(MessageFactory.class);
    GameMessage gameMessage = messageFactory.gameMessage().as();
    gameMessage.setMessageType(GameMessage.MessageType.USER_LIST_UPDATE);
    gameMessage.setPlayerList(new ArrayList<>(peers.keySet()));
    session.getOpenSessions().stream().filter(Session::isOpen).forEach(peer -> {
      sendMessage(peer, gameMessage);
    });
  }

  private void sendMessage(Session session, GameMessage message) {
    RemoteEndpoint.Basic remote = session.getBasicRemote();

    if (remote != null) {
      try {
        remote.sendText(Util.serializePlayerMessageToJson(message), true);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
