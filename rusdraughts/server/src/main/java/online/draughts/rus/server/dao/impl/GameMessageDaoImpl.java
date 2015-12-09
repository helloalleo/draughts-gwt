package online.draughts.rus.server.dao.impl;

import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.google.inject.persist.Transactional;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import online.draughts.rus.server.dao.GameMessageDao;
import online.draughts.rus.server.domain.GameMessage;
import online.draughts.rus.server.domain.Move;
import online.draughts.rus.server.domain.Player;
import online.draughts.rus.shared.dto.GameMessageDto;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 28.12.14
 * Time: 21:55
 */
public class GameMessageDaoImpl extends DaoImpl<GameMessage> implements GameMessageDao {

  @Inject
  public GameMessageDaoImpl(TypeLiteral<GameMessage> type) {
    super(type);
  }

  @Override
  @Transactional
  public List<GameMessage> findLastMessages(int countLast, Long playerId, Long opponentId) {
//    Query query = getEntityManager().createQuery("SELECT m " +
//            " FROM GameMessage m " +
//            " JOIN FETCH m.sender " +
//            " JOIN FETCH m.receiver " +
//            " WHERE ((m.sender.id = :senderId AND m.receiver.id = :receiverId) " +
//            " OR (m.receiver.id = :senderId AND m.sender.id = :receiverId)) " +
//            " AND m.messageType = :messageType " +
//            " ORDER BY m.sentDate DESC");
//    query.setParameter("senderId", playerId);
//    query.setParameter("receiverId", opponentId);
//    query.setParameter("messageType", GameMessage.MessageType.CHAT_PRIVATE_MESSAGE);
//
//    query.setMaxResults(countLast);
//
//    List list = query.getResultList();
//    Collections.reverse(list);
//    return list;

    Query<GameMessage> query =ofy().load().type(getEntityClass())
        .filter("messageType", GameMessageDto.MessageType.CHAT_PRIVATE_MESSAGE)
        .order("sentDate")
        .orderKey(true);

    List<GameMessage> playerMessages = query
        .filter("sender", Key.create(Player.class, playerId))
        .filter("receiver", Key.create(Player.class, opponentId))
        .list();

    List<GameMessage> friendMessages = query
        .filter("sender", Key.create(Player.class, opponentId))
        .filter("receiver", Key.create(Player.class, playerId))
        .list();

    playerMessages.addAll(friendMessages);
    return playerMessages;
  }

  @Override
  public List<GameMessage> findGameMessagesByGameId(Long gameId) {
//    String hql = "SELECT gm " +
//        " FROM GameMessage gm " +
//        " WHERE gm.game.id = :gameId";
//    Query query = getEntityManager().createQuery(hql);
//    query.setParameter("gameId", gameId);
//    return query.getResultList();

    return ofy().load().type(getEntityClass())
        .filter("game.id", gameId)
        .list();
  }

  @Override
  public List<Move> findGameMoves(Long gameId) {
//    String hql = "SELECT gm.move " +
//        " FROM GameMessage gm " +
//        " WHERE gm.game.id = :gameId " +
//        " ORDER BY gm.sentDate";
//    Query query = getEntityManager().createQuery(hql);
//    query.setParameter("gameId", gameId);
//    return query.getResultList();

    List<GameMessage> gameMessages = ofy().load().type(getEntityClass())
        .filter("game.id", gameId)
        .order("sentDate")
        .orderKey(true)
        .list();
    List<Move> moves = new ArrayList<>(gameMessages.size());
    for (GameMessage gameMessage : gameMessages) {
      moves.add(gameMessage.getMove());
    }
    return moves;
  }

  @Override
  public Move saveMove(Move move) {
    return ofy().load().key(ofy().save().entity(move).now()).now();
  }
}
