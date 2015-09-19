package online.shashki.rus.server.service;

import online.shashki.rus.server.dao.Dao;
import online.shashki.rus.server.dao.ShashistDao;
import online.shashki.rus.shared.model.entity.ShashistEntity;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 05.12.14
 * Time: 0:08
 */
@Stateless
public class ShashistService extends BaseService<ShashistEntity> {

  @Inject
  private ShashistDao shashistDao;

  @Override
  protected Dao<ShashistEntity> getDao() {
    return shashistDao;
  }

  public ShashistEntity findByVkUid(String uid) {
    return shashistDao.findByVkUid(uid);
  }

  public ShashistEntity findBySessionId(String sessionId) {
    return shashistDao.findBySessionId(sessionId);
  }
}
