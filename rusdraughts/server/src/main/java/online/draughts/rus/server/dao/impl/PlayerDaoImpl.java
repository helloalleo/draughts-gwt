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
        .iterator().next();
//    try {
//      return (Player) findByParam("Player", new String[]{"vkId"}, new String[]{vkId});
//    } catch (IllegalArgumentException e) {
//      log.severe(e.getLocalizedMessage());
//      return null;
//    }
  }

  @Override
  @Transactional
  public Player findBySessionId(String sessionId) {
    return ofy().load().type(getEntityClass())
        .filter("sessionId", sessionId)
        .iterator().next();
//    try {
//      return (Player) findByParam("Player", new String[]{"sessionId"}, new String[]{sessionId});
//    } catch (IllegalArgumentException e) {
//      log.severe(e.getLocalizedMessage());
//      return null;
//    }
  }

//  @Override
//  @Transactional
//  public Player findById(Long playerId) {
////    try {
////      return (Player) findByParam("Player", new String[]{"id"}, new Long[]{playerId});
////    } catch (IllegalArgumentException e) {
////      log.severe(e.getLocalizedMessage());
////      return null;
////    }
//    return ofy().load().type(getEntityClass())
//        .filter("vkId", vkId)
//        .iterator().next();
//  }

  @Override
  @Transactional
  public Player findByFbId(String fbId) {
    return ofy().load().type(getEntityClass())
        .filter("fbId", fbId)
        .iterator().next();
//    try {
//      return (Player) findByParam("Player", new String[]{"fbId"}, new String[]{user_id});
//    } catch (IllegalArgumentException e) {
//      log.severe(e.getLocalizedMessage());
//      return null;
//    }
  }

  @Override
  @Transactional
  public Player findByGoogleSub(String sub) {
    return ofy().load().type(getEntityClass())
        .filter("googleSub", sub)
        .iterator().next();

//    try {
//      return (Player) findByParam("Player", new String[]{"googleSub"}, new String[]{sub});
//    } catch (IllegalArgumentException e) {
//      log.severe(e.getLocalizedMessage());
//      return null;
//    }
  }


  @Override
  public List<Player> findAll() {
    return ofy().load().type(getEntityClass()).filter("active", true).list();

//    String hql = "SELECT p " +
//        " FROM Player p " +
//        " WHERE p.banned = false" +
//        " ORDER BY p.online DESC";
//    Query query = getEntityManager().createQuery(hql);
//    return (List<Player>) query.getResultList();
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

//  @Override
//  public List<Player> findOnline() {
////    String hql = "SELECT p " +
////        " FROM Player p " +
////        " WHERE (p.online = true " +
////        "    OR p.playing = true)" +
////        "  AND p.banned = false";
////    Query query = getEntityManager().createQuery(hql);
////    return (List<Player>) query.getResultList();
//
//  }
}
