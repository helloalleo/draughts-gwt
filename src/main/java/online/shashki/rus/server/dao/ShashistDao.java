package online.shashki.rus.server.dao;

import online.shashki.rus.shared.model.entity.ShashistEntity;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 05.12.14
 * Time: 0:18
 */
public interface ShashistDao extends Dao<ShashistEntity> {

  public ShashistEntity findByVkUid(String uid);

  ShashistEntity findBySessionId(String sessionId);

}
