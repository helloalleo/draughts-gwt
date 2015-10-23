package online.shashki.rus.server.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import online.shashki.rus.server.dao.GameDao;
import online.shashki.rus.shared.model.Friend;
import online.shashki.rus.shared.model.Game;
import online.shashki.rus.shared.model.Player;
import online.shashki.rus.shared.model.key.FriendId;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 15.10.15
 * Time: 9:48
 */
@Singleton
public class GameService {

  private final Provider<GameDao> gameDaoProvider;
  private final PlayerService playerService;
  private final FriendService friendService;
  private final GameDao gameDao;

  @Inject
  public GameService(Provider<GameDao> gameDaoProvider,
                     PlayerService playerService,
                     FriendService friendService,
                     GameDao gameDao) {
    this.gameDaoProvider = gameDaoProvider;
    this.playerService = playerService;
    this.friendService = friendService;
    this.gameDao = gameDao;
  }

  @Transactional
  public List<Game> findRange(int offset, int limit) {
    return gameDao.findRange(offset, limit);
  }

  @Transactional
  public List<Game> findUserGames(HttpSession session, int offset, int limit) {
    final Player loggedInUser = playerService.getLoggedInUser(session);
    if (loggedInUser == null) {
      throw new RuntimeException("Not authorized");
    }
    return gameDaoProvider.get().findUserGames(loggedInUser.getId(), offset, limit);
  }

  public Long getGamesCount() {
    return gameDaoProvider.get().countAll();
  }

  public Game saveOrCreate(Game game) {
    if (game.getId() == null) {
      gameDaoProvider.get().create(game);
    } else {
      gameDaoProvider.get().edit(game);
      Player player;
      if (game.getPlayerWhite() != null) {
        player = playerService.find(game.getPlayerWhite().getId());
      } else {
        return null;
      }
      Player friendOf;
      if (game.getPlayerBlack() != null) {
        friendOf = playerService.find(game.getPlayerBlack().getId());
      } else {
        return null;
      }
      final FriendId friendPk = new FriendId()
          .setFriend(player)
          .setFriendOf(friendOf);
      Friend friend = new Friend()
          .setPk(friendPk);
      friendService.saveOrCreate(friend);
    }
    return game;
  }

  public Game find(Long gameId) {
    return gameDaoProvider.get().find(gameId);
  }
}
