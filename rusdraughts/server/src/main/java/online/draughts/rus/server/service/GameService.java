package online.draughts.rus.server.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import online.draughts.rus.server.dao.GameDao;
import online.draughts.rus.server.dao.GameMessageDao;
import online.draughts.rus.server.domain.Friend;
import online.draughts.rus.server.domain.Game;
import online.draughts.rus.server.domain.Move;
import online.draughts.rus.server.domain.Player;
import online.draughts.rus.server.util.Rating;
import online.draughts.rus.shared.dto.GameDto;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 15.10.15
 * Time: 9:48
 */
@Singleton
public class GameService {
  private final Provider<GameDao> gameDaoProvider;
  private final Provider<GameMessageDao> gameMessageDaoProvider;
  private final PlayerService playerService;
  private final FriendService friendService;
  private final GameDao gameDao;

  @Inject
  public GameService(
      Provider<GameDao> gameDaoProvider,
      Provider<GameMessageDao> gameMessageDaoProvider,
      PlayerService playerService,
      FriendService friendService,
      GameDao gameDao) {
    this.gameDaoProvider = gameDaoProvider;
    this.gameMessageDaoProvider = gameMessageDaoProvider;
    this.playerService = playerService;
    this.friendService = friendService;
    this.gameDao = gameDao;
  }

  public List<Game> findRange(int offset, int limit) {
    return gameDao.findRange(offset, limit);
  }

  public List<Game> findUserGames(HttpSession session, int offset, int limit) {
    final Player loggedInUser = playerService.getLoggedInUser(session);
    if (loggedInUser == null) {
      throw new RuntimeException("Not authorized");
    }
    return findUserGames(loggedInUser.getId(), offset, limit);
  }

  public List<Game> findUserGames(Long playerId, int offset, int limit) {
    final Player player = playerService.find(playerId);
    if (player == null) {
      throw new RuntimeException("Player not found");
    }
    return gameDaoProvider.get().findUserGames(playerId, offset, limit);
  }

  public Integer getGamesCount() {
    return gameDaoProvider.get().countAll();
  }

  public Game save(Game game) {
    if (game.getId() == null) {
      return gameDaoProvider.get().save(game);
    }

    updatePlayer(game, game.getPlayerBlack().getId());
    updatePlayer(game, game.getPlayerWhite().getId());
    Game saved = gameDaoProvider.get().save(game);

    Player playerBlack = playerService.find(game.getPlayerBlack().getId());
    Player playerWhite = playerService.find(game.getPlayerWhite().getId());
    boolean playerWhiteIsFriendOfPlayerBlack = friendService.isPlayerFriendOf(playerWhite.getId(), playerBlack.getId());
    if (!playerWhiteIsFriendOfPlayerBlack) {
      Friend friend = new Friend(playerWhite.getId(), playerBlack.getId());
      friend.setFriend(playerWhite);
      friend.setFriendOf(playerBlack);

      friendService.save(friend);
    }
    boolean playerBlackIsFriendOfPlayerWhite = friendService.isPlayerFriendOf(playerBlack.getId(), playerWhite.getId());
    if (!playerBlackIsFriendOfPlayerWhite) {
      Friend friend = new Friend(playerBlack.getId(), playerWhite.getId());
      friend.setFriend(playerBlack);
      friend.setFriendOf(playerWhite);

      friendService.save(friend);
    }
    return saved;
  }

  private void updatePlayer(Game game, Long playerId) {
    Player player = playerService.find(playerId);
    player.setGamePlayed(player.getGamePlayed() + 1);
    final GameDto.GameEnds playEndStatus = game.getPlayEndStatus();
    if (playEndStatus != null) {
      final boolean blackWin = GameDto.GameEnds.BLACK_WIN.equals(playEndStatus);
      final boolean whiteWin = GameDto.GameEnds.WHITE_WIN.equals(playEndStatus);
      final boolean blackSurrender = GameDto.GameEnds.SURRENDER_BLACK.equals(playEndStatus);
      final boolean whiteSurrender = GameDto.GameEnds.SURRENDER_WHITE.equals(playEndStatus);
      final boolean blackLeft = GameDto.GameEnds.BLACK_LEFT.equals(playEndStatus);
      final boolean whiteLeft = GameDto.GameEnds.WHITE_LEFT.equals(playEndStatus);
      if (Objects.equals(game.getPlayerBlack().getId(), player.getId())) {
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
    playerService.save(player);
  }

  public Game find(Long gameId) {
    return gameDaoProvider.get().find(gameId);
  }

  public List<Move> findGameMoves(Long gameId) {
    return gameMessageDaoProvider.get().findGameMoves(gameId);
  }
}
