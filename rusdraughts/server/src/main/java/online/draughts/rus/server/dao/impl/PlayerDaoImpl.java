package online.draughts.rus.server.dao.impl;

import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.google.inject.persist.Transactional;
import com.googlecode.objectify.cmd.Query;
import online.draughts.rus.server.dao.PlayerDao;
import online.draughts.rus.server.domain.Player;

import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 05.12.14
 * Time: 0:19
 */
public class PlayerDaoImpl extends DaoImpl<Player> implements PlayerDao {

  @Inject
  public PlayerDaoImpl(TypeLiteral<Player> type) {
    super(type);
  }

  @Override
  @Transactional
  public Player findByVkId(String vkId) {
    return ofy().load().type(getEntityClass())
        .filter("vkId", vkId)
        .first()
        .now();
  }


  @Override
  @Transactional
  public Player findBySessionId(String sessionId) {
    return ofy().load().type(getEntityClass())
        .filter("sessionId", sessionId)
        .first()
        .now();
  }

  @Override
  @Transactional
  public Player findByFbId(String fbId) {
    return ofy().load().type(getEntityClass())
        .filter("fbId", fbId)
        .first()
        .now();
  }

  @Override
  @Transactional
  public Player findByGoogleSub(String sub) {
    return ofy().load().type(getEntityClass())
        .filter("googleSub", sub)
        .first()
        .now();
  }

  @Override
  public List<Player> findAll() {
    return ofy().load().type(getEntityClass())
        .filter("active", true)
        .order("-online")
        .list();
  }

  @Override
  public List<Player> findOnline() {
    Query<Player> active = ofy().load().type(getEntityClass())
        .filter("active", true);

    List<Player> online = active
        .filter("online", true)
        .list();

    List<Player> playing  = active
        .filter("playing", true)
        .list();

    online.addAll(playing);
    return online;
  }
}
