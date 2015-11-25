package online.draughts.rus.server.resource;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import online.draughts.rus.server.service.GameMessageService;
import online.draughts.rus.shared.model.GameMessage;
import online.draughts.rus.shared.resource.GameMessagesResource;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 24.11.15
 * Time: 19:53
 */
@RequestScoped
public class GameMessagesResourceImpl implements GameMessagesResource {

  private final GameMessageService gameMessageService;

  @Inject
  public GameMessagesResourceImpl(
      GameMessageService gameMessageService) {
    this.gameMessageService = gameMessageService;
  }

  @Override
  public List<GameMessage> findLastMessages(Integer countLast, Long playerId, Long opponentId) {
    return gameMessageService.findLastMessages(countLast, playerId, opponentId);
  }
}
