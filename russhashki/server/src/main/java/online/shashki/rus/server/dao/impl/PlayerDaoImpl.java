package online.shashki.rus.server.dao.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import online.shashki.rus.server.dao.PlayerDao;
import online.shashki.rus.shared.model.Player;

import javax.persistence.EntityManager;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 05.12.14
 * Time: 0:19
 */
public class PlayerDaoImpl extends DaoImpl<Player> implements PlayerDao {

  private final Provider<EntityManager> entityManagerProvider;
  private final Logger log;

  @Inject
  public PlayerDaoImpl(TypeLiteral<Player> type, Provider<EntityManager> entityManagerProvider, Logger log) {
    super(type);
    this.entityManagerProvider = entityManagerProvider;
    this.log = log;
  }

  @Override
  protected EntityManager getEntityManager() {
    return entityManagerProvider.get();
  }

  @Override
  public Player findByVkId(String vkId) {
    try {
      return findByParam("Player", new String[]{"vkId"}, new String[]{vkId});
    } catch (IllegalArgumentException e) {
      log.severe(e.getLocalizedMessage());
      return null;
    }
  }

  @Override
  public Player findBySessionId(String sessionId) {
    try {
      return findByParam("Player", new String[]{"sessionId"}, new String[]{sessionId});
    } catch (IllegalArgumentException e) {
      log.severe(e.getLocalizedMessage());
      return null;
    }
  }

  @Override
  public Player findById(Long playerId) {
    try {
      return findByParam("Player", new String[]{"id"}, new Long[]{playerId});
    } catch (IllegalArgumentException e) {
      log.severe(e.getLocalizedMessage());
      return null;
    }
  }

  @Override
  public Player findByFbId(String user_id) {
    try {
      return findByParam("Player", new String[]{"fbId"}, new String[]{user_id});
    } catch (IllegalArgumentException e) {
      log.severe(e.getLocalizedMessage());
      return null;
    }
  }

  @Override
  public Player findByGoogleSub(String sub) {
    try {
      return findByParam("Player", new String[]{"googleSub"}, new String[]{sub});
    } catch (IllegalArgumentException e) {
      log.severe(e.getLocalizedMessage());
      return null;
    }
  }
}
