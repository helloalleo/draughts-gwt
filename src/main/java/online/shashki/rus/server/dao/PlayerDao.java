package online.shashki.rus.server.dao;

import online.shashki.rus.shared.model.Player;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 05.12.14
 * Time: 0:18
 */
public interface PlayerDao extends Dao<Player> {

  Player findByVkUid(String uid);

  Player findBySessionId(String sessionId);

  Player findById(Long shashistId);
}
