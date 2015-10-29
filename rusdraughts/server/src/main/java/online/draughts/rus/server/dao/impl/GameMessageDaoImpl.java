package online.draughts.rus.server.dao.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.persist.Transactional;
import online.draughts.rus.server.dao.GameMessageDao;
import online.draughts.rus.shared.model.GameMessage;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 28.12.14
 * Time: 21:55
 */
public class GameMessageDaoImpl extends DaoImpl<GameMessage> implements GameMessageDao {

  private final Provider<EntityManager> entityManagerProvider;

  @Inject
  public GameMessageDaoImpl(TypeLiteral<GameMessage> type, Provider<EntityManager> entityManagerProvider) {
    super(type);
    this.entityManagerProvider = entityManagerProvider;
  }

  @Override
  protected EntityManager getEntityManager() {
    return entityManagerProvider.get();
  }

  @Override
  @Transactional
  public List<GameMessage> findLastMessages(int countLast, Long playerId, Long opponentId) {
    Query query = getEntityManager().createQuery("SELECT m " +
            " FROM GameMessage m " +
            " JOIN FETCH m.sender " +
            " JOIN FETCH m.receiver " +
            " WHERE ((m.sender.id = :senderId AND m.receiver.id = :receiverId) " +
            " OR (m.receiver.id = :senderId AND m.sender.id = :receiverId)) " +
            " AND m.messageType = :messageType " +
            " ORDER BY m.sentDate DESC");
    query.setParameter("senderId", playerId);
    query.setParameter("receiverId", opponentId);
    query.setParameter("messageType", GameMessage.MessageType.CHAT_PRIVATE_MESSAGE);

    query.setMaxResults(countLast);

    List list = query.getResultList();
    Collections.reverse(list);
    return list;
  }

  @Override
  @Transactional
  public void removeMessagesByGameId(Long gameId) {
    String hql = "DELETE FROM GameMessage gm " +
        " WHERE gm.game.id = :gameId";
    Query query = getEntityManager().createQuery(hql);
    query.setParameter("gameId", gameId);
    query.executeUpdate();
  }

  @Override
  public List<GameMessage> findGameMessagesByGameId(Long gameId) {
    String hql = "SELECT gm " +
        " FROM GameMessage gm " +
        " WHERE gm.game.id = :gameId";
    Query query = getEntityManager().createQuery(hql);
    query.setParameter("gameId", gameId);
    return query.getResultList();
  }
}