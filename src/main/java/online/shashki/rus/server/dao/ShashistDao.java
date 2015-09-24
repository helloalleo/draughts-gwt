package online.shashki.rus.server.dao;

import online.shashki.rus.shared.model.Shashist;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 05.12.14
 * Time: 0:18
 */
public interface ShashistDao extends Dao<Shashist> {

  public Shashist findByVkUid(String uid);

  Shashist findBySessionId(String sessionId);

}
