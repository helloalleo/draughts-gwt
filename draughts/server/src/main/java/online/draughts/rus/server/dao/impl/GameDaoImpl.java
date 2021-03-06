//package online.draughts.rus.server.dao.impl;
//
//import com.google.inject.Inject;
//import com.google.inject.TypeLiteral;
//import com.google.inject.persist.Transactional;
//import com.googlecode.objectify.Key;
//import online.draughts.rus.server.dao.GameDao;
//import online.draughts.rus.server.domain.Game;
//import online.draughts.rus.server.domain.Player;
//
//import java.util.List;
//
//import static com.googlecode.objectify.ObjectifyService.ofy;
//
///**
// * Created with IntelliJ IDEA.
// * User: alekspo
// * Date: 31.12.14
// * Time: 17:34
// */
//public class GameDaoImpl extends DaoImpl<Game> implements GameDao {
//
//  @Inject
//  public GameDaoImpl(TypeLiteral<Game> type) {
//    super(type);
//  }
//
//  @Override
//  @Transactional
//  public List<Game> findRange(int start, int length) {
////    String hql = "SELECT g " +
////        " FROM Game g " +
////        " JOIN FETCH g.playerWhite " +
////        " JOIN FETCH g.playerBlack " +
////        " WHERE g.playFinishDate IS NOT NULL " +
////        " ORDER BY g.playFinishDate DESC";
////    Query query = getEntityManager().createQuery(hql);
////    query.setFirstResult(start);
////    query.setMaxResults(length);
////    return query.getResultList();
//    return ofy().load().type(getEntityClass())
//        .filter("playFinishDate !=", null)
//        .order("-playFinishDate").orderKey(true)
//        .offset(start).limit(length)
//        .list();
//  }
//
//  @Override
//  public List<Game> findUserGames(Long userId, int start, int length) {
////    String hql = "SELECT g " +
////        " FROM Game g " +
////        " JOIN FETCH g.playerWhite white " +
////        " JOIN FETCH g.playerBlack black " +
////        " WHERE (white.id = :userId " +
////        "    OR black.id = :userId) " +
////        "   AND g.playStartDate IS NOT NULL" +
////        " ORDER BY g.playStartDate DESC";
////    Query query = getEntityManager().createQuery(hql);
////    query.setParameter("userId", userId);
////    query.setFirstResult(start);
////    query.setMaxResults(length);
////    return query.getResultList();
//
//    List<Game> gamesBlack = ofy().load().type(getEntityClass())
//        .filter("playStateDate !=", null)
//        .order("-playStartDate")
//        .filter("playerBlack", Key.create(Player.class, userId))
//        .list();
//
//    List<Game> gamesWhite = ofy().load().type(getEntityClass())
//        .filter("playStateDate !=", null)
//        .order("-playStartDate")
//        .filter("playerWhite", Key.create(Player.class, userId))
//        .list();
//
//    gamesBlack.addAll(gamesWhite);
//    return gamesBlack;
//  }
//}
