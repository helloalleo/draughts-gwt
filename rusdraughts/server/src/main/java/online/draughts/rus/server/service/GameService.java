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
import online.draughts.rus.shared.util.DozerHelper;
import org.dozer.Mapper;

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
  private final Mapper mapper;

  @Inject
  public GameService(
      Provider<GameDao> gameDaoProvider,
      Provider<GameMessageDao> gameMessageDaoProvider,
      PlayerService playerService,
      FriendService friendService,
      GameDao gameDao,
      Mapper mapper) {
    this.gameDaoProvider = gameDaoProvider;
    this.gameMessageDaoProvider = gameMessageDaoProvider;
    this.playerService = playerService;
    this.friendService = friendService;
    this.gameDao = gameDao;
    this.mapper = mapper;
  }

  public List<GameDto> findRange(int offset, int limit) {
    final List<Game> games = gameDao.findRange(offset, limit);
    return DozerHelper.map(mapper, games, GameDto.class);
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
    Game mapped = mapper.map(game, Game.class);
    if (game.getId() == null) {
      return gameDaoProvider.get().save(mapped);
    }

    updatePlayer(mapped, mapped.getPlayerBlack().getId());
    updatePlayer(mapped, mapped.getPlayerWhite().getId());
    Game saved = gameDaoProvider.get().save(mapped);

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
    final Game.GameEnds playEndStatus = game.getPlayEndStatus();
    if (playEndStatus != null) {
      final boolean blackWin = Game.GameEnds.BLACK_WIN.equals(playEndStatus);
      final boolean whiteWin = Game.GameEnds.WHITE_WIN.equals(playEndStatus);
      final boolean blackSurrender = Game.GameEnds.SURRENDER_BLACK.equals(playEndStatus);
      final boolean whiteSurrender = Game.GameEnds.SURRENDER_WHITE.equals(playEndStatus);
      final boolean blackLeft = Game.GameEnds.BLACK_LEFT.equals(playEndStatus);
      final boolean whiteLeft = Game.GameEnds.WHITE_LEFT.equals(playEndStatus);
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
