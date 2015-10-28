package online.shashki.rus.server.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import online.shashki.rus.server.dao.GameDao;
import online.shashki.rus.server.utils.Rating;
import online.shashki.rus.shared.model.Friend;
import online.shashki.rus.shared.model.Game;
import online.shashki.rus.shared.model.Move;
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
    return findUserGames(loggedInUser.getId(), offset, limit);
  }

  @Transactional
  public List<Game> findUserGames(Long playerId, int offset, int limit) {
    return gameDaoProvider.get().findUserGames(playerId, offset, limit);
  }

  public Long getGamesCount() {
    return gameDaoProvider.get().countAll();
  }

  public Game saveOrCreate(Game game) {
    if (game.getId() == null) {
      gameDaoProvider.get().create(game);
    } else {
      updatePlayer(game, game.getPlayerBlack().getId());
      updatePlayer(game, game.getPlayerWhite().getId());
      gameDaoProvider.get().edit(game);

      Player playerBlack = playerService.find(game.getPlayerBlack().getId());
      Player playerWhite = playerService.find(game.getPlayerWhite().getId());
      boolean playerWhiteIsPlayerBlack = friendService.isPlayerFriendOf(playerWhite.getId(), playerBlack.getId());
      if (!playerWhiteIsPlayerBlack) {
        FriendId friendPk = new FriendId()
            .setFriend(playerWhite)
            .setFriendOf(playerBlack);
        Friend friend = new Friend()
            .setPk(friendPk);
        friendService.saveOrCreate(friend);
      }
      boolean playerBlackIsPlayerWhite = friendService.isPlayerFriendOf(playerBlack.getId(), playerWhite.getId());
      if (!playerBlackIsPlayerWhite) {
        FriendId friendPk = new FriendId()
            .setFriend(playerBlack)
            .setFriendOf(playerWhite);
        Friend friend = new Friend()
            .setPk(friendPk);
        friendService.saveOrCreate(friend);
      }
    }
    return game;
  }

  private void updatePlayer(Game game, Long playerId) {
    Player player = playerService.find(playerId);
    player.setGamePlayed(player.getGamePlayed() + 1);
    final Game.GameEnds playEndStatus = game.getPlayEndStatus();
    if (playEndStatus != null) {
      final boolean blackWin = Game.GameEnds.BLACK_WIN.equals(playEndStatus);
      final boolean whiteWin = Game.GameEnds.WHITE_WIN.equals(playEndStatus);
      final boolean blackSurrender = Game.GameEnds.SURRENDER_BLACK.equals(playEndStatus);
      final boolean whiteSurrender = Game.GameEnds.SURRENDER_WHITE.equals(playEndStatus);
      final boolean blackLeft = Game.GameEnds.BLACK_LEFT.equals(playEndStatus);
      final boolean whiteLeft = Game.GameEnds.WHITE_LEFT.equals(playEndStatus);
      if (game.getPlayerBlack().getId().equals(player.getId())) {
        if (blackWin || whiteLeft || whiteSurrender) {
          player.setGameWin(player.getGameWin() + 1);
        } else if (whiteWin || blackLeft || blackSurrender) {
          player.setGameLose(player.getGameLose() + 1);
        } else {
          player.setGameDraw(player.getGameDraw() + 1);
        }
      } else {
        if (blackWin || whiteLeft || whiteSurrender) {
          player.setGameLose(player.getGameLose() + 1);
        } else if (whiteWin || blackLeft || blackSurrender) {
          player.setGameWin(player.getGameWin() + 1);
        } else {
          player.setGameDraw(player.getGameDraw() + 1);
        }
      }
    }
    Rating.calcPlayerRating(player);
    playerService.saveOrCreate(player);
  }

  public boolean removeGame(Game game) {
    gameDao.remove(game);
    return true;
  }

  public Game find(Long gameId) {
    return gameDaoProvider.get().find(gameId);
  }

  public List<Move> findGameMoves(Long gameId) {
    return gameDaoProvider.get().findGameMoves(gameId);
  }
}
