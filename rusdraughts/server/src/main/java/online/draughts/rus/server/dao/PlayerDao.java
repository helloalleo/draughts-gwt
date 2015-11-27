package online.draughts.rus.server.dao;

import online.draughts.rus.shared.model.Friend;
import online.draughts.rus.shared.model.Player;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 05.12.14
 * Time: 0:18
 */
public interface PlayerDao extends Dao<Player> {

  Player findByVkId(String uid);

  Player findBySessionId(String sessionId);

  Player findById(Long playerId);

  Player findByFbId(String user_id);

  Player findByGoogleSub(String sub);

  List<Friend> findFriends(Long playerId);

  List<Player> findAll();

  List<Player> findOnline();
}
