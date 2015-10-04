package online.shashki.rus.server.dao;

import online.shashki.rus.shared.model.GameMessage;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 31.12.14
 * Time: 18:04
 */
public interface GameMessageDao extends Dao<GameMessage> {

  List<GameMessage> findLastMessages(int countLast, Long playerId, Long opponentId);
}
