package online.shashki.rus.server.rpc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import online.shashki.rus.client.service.ProfileRpcService;
import online.shashki.rus.server.service.ShashistService;
import online.shashki.rus.server.utils.AuthUtils;
import online.shashki.rus.shared.model.Shashist;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 05.12.14
 * Time: 20:03
 */
public class ProfileRpcServiceImpl extends RemoteServiceServlet implements ProfileRpcService {

  @Inject
  private ShashistService shashistService;

  @Override
  public Boolean isAuthenticated() {
    return AuthUtils.isAuthenticated(getThreadLocalRequest().getSession());
  }

  @Override
  public Shashist getProfile(Long profileId) {
    if (profileId == null) {
      return null;
    }
    return shashistService.find(profileId);
  }

  @Override
  public Shashist getCurrentProfile() {
    HttpSession session = getThreadLocalRequest().getSession();
    if (session != null) {
      return shashistService.findBySessionId(session.getId());
    }
    return null;
  }

  @Override
  public void saveProfile(Shashist profile) {
    if (!isAuthenticated()) {
      return;
    }
    if (profile != null) {
      Shashist shashist = shashistService.findById(profile.getId());

      if (shashist != null) {
        shashist.updateSerializable(profile);
        shashistService.edit(shashist);
      }
    }
  }

  @Override
  public boolean isCookieValid(String cookie) {
    return !(cookie == null || cookie.isEmpty())
        && cookie.equals(getThreadLocalRequest().getSession().getId());
  }
}