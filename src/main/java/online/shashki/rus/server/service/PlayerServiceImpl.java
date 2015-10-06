package online.shashki.rus.server.service;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import online.shashki.rus.client.service.PlayerService;
import online.shashki.rus.server.dao.PlayerDao;
import online.shashki.rus.server.utils.AuthUtils;
import online.shashki.rus.shared.model.Player;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 05.12.14
 * Time: 20:03
 */
@Singleton
public class PlayerServiceImpl extends RemoteServiceServlet implements PlayerService {

  private final PlayerDao playerDao;

  @Inject
  public PlayerServiceImpl(PlayerDao playerDao) {
    this.playerDao = playerDao;
  }

  @Override
  public Boolean isAuthenticated() {
    if (getThreadLocalRequest() != null) {
      return AuthUtils.isAuthenticated(getThreadLocalRequest().getSession());
    }
    return false;
  }

  @Override
  public Player find(Long profileId) {
    if (profileId == null) {
      return null;
    }
    return playerDao.find(profileId);
  }

  @Override
  public Player getCurrentProfile() {
    if (getThreadLocalRequest() == null) {
      return null;
    }
    HttpSession session = getThreadLocalRequest().getSession();
    if (session != null) {
      final Player bySessionId = playerDao.findBySessionId(session.getId());
      System.out.println(bySessionId);
      return bySessionId;
    }
    return null;
  }

  @Override
  public Player save(Player profile) {
    return save(profile, false);
  }

  public Player save(Player profile, boolean onServer) {
    final Player currentProfile = getCurrentProfile();
    if (currentProfile == null && profile != null && profile.getId() == null) {
      playerDao.create(profile);
      return profile;
    }

    if (!(isAuthenticated() || onServer)) {
      return null;
    }

    if (profile == null) {
      return null;
    }

    Player player = playerDao.findById(profile.getId());

    if (player != null) {
      player.updateSerializable(profile);
      playerDao.edit(player);
      return player;
    }
    return null;
  }

  @Override
  public boolean isCookieValid(String cookie) {
    return !(cookie == null || cookie.isEmpty())
        && cookie.equals(getThreadLocalRequest().getSession().getId());
  }

  public List<Player> findAll() {
    return playerDao.findAll();
  }

  public Player findByVkUid(String vkUid) {
    return playerDao.findByVkUid(vkUid);
  }

  public Player findBySessionId(String sessionId) {
    return playerDao.findBySessionId(sessionId);
  }
}