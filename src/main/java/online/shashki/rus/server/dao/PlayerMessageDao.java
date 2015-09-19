package online.shashki.rus.server.dao;

import online.shashki.rus.shared.model.entity.GameMessageEntity;
import online.shashki.rus.shared.model.GameMessage;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 28.12.14
 * Time: 21:53
 */
public interface PlayerMessageDao extends Dao<GameMessageEntity> {
  List<GameMessage> findLastMessages(int countLast, Long playerId, Long opponentId);
}
