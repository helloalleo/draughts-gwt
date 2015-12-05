package online.draughts.rus.server.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import online.draughts.rus.server.dao.GameMessageDao;
import online.draughts.rus.server.domain.GameMessage;
import online.draughts.rus.server.domain.Player;
import online.draughts.rus.shared.dto.GameMessageDto;
import online.draughts.rus.shared.util.DozerHelper;
import org.dozer.Mapper;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Singleton
public class GameMessageService {
  private Logger logger;
  private final Provider<GameMessageDao> gameMessageDaoProvider;
  private final PlayerService playerService;
  private final Mapper mapper;

  @Inject
  GameMessageService(
      Logger logger,
      Provider<GameMessageDao> gameDaoProvider,
      PlayerService playerService,
      Mapper mapper) {
    this.logger = logger;
    this.gameMessageDaoProvider = gameDaoProvider;
    this.playerService = playerService;
    this.mapper = mapper;
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

  public List<GameMessageDto> findLastMessages(Integer countLast, Long playerId, Long opponentId) {
    final List<GameMessage> lastMessages = gameMessageDaoProvider.get().findLastMessages(countLast, playerId, opponentId);
    return DozerHelper.map(mapper, lastMessages, GameMessageDto.class);
  }

  public Map<Long, Integer> findUnreadMessages(Long playerId) {
    Player player = playerService.find(playerId);
    return player.getFriendUnreadMessagesMap();
  }
}
