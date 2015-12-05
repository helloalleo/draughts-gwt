package online.draughts.rus.server.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import online.draughts.rus.server.dao.GameMessageDao;
import online.draughts.rus.shared.model.GameMessage;
import online.draughts.rus.shared.model.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Singleton
public class GameMessageService {
  private Logger logger;
  private final Provider<GameMessageDao> gameMessageDaoProvider;
  private final PlayerService playerService;

  @Inject
  GameMessageService(
      Logger logger,
      Provider<GameMessageDao> gameDaoProvider,
      PlayerService playerService) {
    this.logger = logger;
    this.gameMessageDaoProvider = gameDaoProvider;
    this.playerService = playerService;
  }

  public GameMessage saveOrCreate(GameMessage gameMessage) {
    if (gameMessage == null) {
      return null;
    }

    logger.info("New message: " + gameMessage.toString());
    gameMessageDaoProvider.get().save(gameMessage);
    return gameMessage;
  }

  public List<GameMessage> findGameMessagesByGameId(Long gameId) {
    return gameMessageDaoProvider.get().findGameMessagesByGameId(gameId);
  }

  public List<GameMessage> findLastMessages(Integer countLast, Long playerId, Long opponentId) {
    return gameMessageDaoProvider.get().findLastMessages(countLast, playerId, opponentId);
  }

  public Map<Long, Integer> findUnreadMessages(Long playerId) {
    Player player = playerService.find(playerId);
    return new HashMap<>(player.getFriendUnreadMessagesMap());
  }
}
