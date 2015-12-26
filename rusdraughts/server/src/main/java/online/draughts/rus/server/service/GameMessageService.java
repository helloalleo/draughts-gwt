package online.draughts.rus.server.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import online.draughts.rus.server.domain.GameMessage;
import online.draughts.rus.server.domain.Move;
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
  private final PlayerService playerService;
  private final Mapper mapper;

  @Inject
  GameMessageService(
      Logger logger,
      PlayerService playerService,
      Mapper mapper) {
    this.logger = logger;
    this.playerService = playerService;
    this.mapper = mapper;
  }

  public GameMessage save(GameMessage gameMessage) {
    if (gameMessage == null) {
      return null;
    }

    gameMessage.update();
    gameMessage.flush();
    return gameMessage;
  }

  public List<GameMessageDto> findLastMessages(Integer countLast, Long playerId, Long opponentId) {
    final List<GameMessage> lastMessages = GameMessage.getInstance().findLastMessages(countLast, playerId, opponentId);
    return DozerHelper.map(mapper, lastMessages, GameMessageDto.class);
  }

  public Map<Long, Integer> findUnreadMessages(Long playerId) {
    Player player = playerService.find(playerId);
    return player.getFriendUnreadMessagesMap();
  }

  public Move saveMove(Move move) {
    move.update();
    return move;
  }
}
