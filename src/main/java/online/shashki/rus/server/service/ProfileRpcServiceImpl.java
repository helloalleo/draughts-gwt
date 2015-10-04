package online.shashki.rus.server.service;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import online.shashki.rus.client.service.ProfileRpcService;
import online.shashki.rus.server.dao.ShashistDao;
import online.shashki.rus.server.utils.AuthUtils;
import online.shashki.rus.shared.model.Shashist;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 05.12.14
 * Time: 20:03
 */
public class ProfileRpcServiceImpl extends RemoteServiceServlet implements ProfileRpcService {

  @Inject
  private ShashistDao shashistDao;

  @Override
  public Boolean isAuthenticated() {
    if (getThreadLocalRequest() != null) {
      return AuthUtils.isAuthenticated(getThreadLocalRequest().getSession());
    }
    return false;
  }

  @Override
  public Shashist find(Long profileId) {
    if (profileId == null) {
      return null;
    }
    return shashistDao.find(profileId);
  }

  @Override
  public Shashist getCurrentProfile() {
    if (getThreadLocalRequest() == null) {
      return null;
    }
    HttpSession session = getThreadLocalRequest().getSession();
    if (session != null) {
      final Shashist bySessionId = shashistDao.findBySessionId(session.getId());
      System.out.println(bySessionId);
      return bySessionId;
    }
    return null;
  }

  @Override
  public Shashist save(Shashist profile) {
    return save(profile, false);
  }

  public Shashist save(Shashist profile, boolean onServer) {
    final Shashist currentProfile = getCurrentProfile();
    if (currentProfile == null && profile != null && profile.getId() == null) {
      shashistDao.create(profile);
      return profile;
    }

    if (!(isAuthenticated() || onServer)) {
      return null;
    }

    if (profile == null) {
      return null;
    }

    Shashist shashist = shashistDao.findById(profile.getId());

    if (shashist != null) {
      shashist.updateSerializable(profile);
      shashistDao.edit(shashist);
      return shashist;
    }
    return null;
  }

  @Override
  public boolean isCookieValid(String cookie) {
    return !(cookie == null || cookie.isEmpty())
        && cookie.equals(getThreadLocalRequest().getSession().getId());
  }

  public List<Shashist> findAll() {
    return shashistDao.findAll();
  }

  public Shashist findByVkUid(String vkUid) {
    return shashistDao.findByVkUid(vkUid);
  }

  public Shashist findBySessionId(String sessionId) {
    return shashistDao.findBySessionId(sessionId);
  }
}