package online.shashki.rus.server.service;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import online.shashki.rus.client.service.GameMessageRpcService;
import online.shashki.rus.server.dao.GameMessageDao;
import online.shashki.rus.shared.model.GameMessage;

import javax.inject.Inject;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 30.12.14
 * Time: 9:31
 */
public class GameMessageRpcServiceImpl extends RemoteServiceServlet implements GameMessageRpcService {

  @Inject
  private GameMessageDao gameMessageDao;

  @Override
  public List<GameMessage> getLastPlayerMessages(int countLast, Long playerId, Long opponentId) {
    return gameMessageDao.findLastMessages(countLast, playerId, opponentId);
  }

  public void save(GameMessage gameMessage) {
    if (gameMessage == null) {
      return;
    }
    if (gameMessage.getId() == null) {
      gameMessageDao.create(gameMessage);
    } else {
      gameMessageDao.edit(gameMessage);
    }
  }
}