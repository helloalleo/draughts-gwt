package online.draughts.rus.server.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import online.draughts.rus.server.domain.*;
import online.draughts.rus.server.util.Rating;
import online.draughts.rus.shared.dto.GameDto;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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
  private final PlayerService playerService;
  private final FriendService friendService;

  @Inject
  public GameService(
      PlayerService playerService,
      FriendService friendService) {
    this.playerService = playerService;
    this.friendService = friendService;
  }

  public List<Game> findRange(int offset, int limit) {
    return Game.getInstance().findRange(offset, limit);
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
    return Game.getInstance().findUserGames(playerId, offset, limit);
  }

  public Integer getGamesCount() {
    throw new RuntimeException("Not Implemented");
  }

  public Game save(Game game) {
    if (game.getId() == 0) {
      game.update();
      return game;
    }

    Player playerBlack = updatePlayerStat(game, game.getPlayerBlack().getId());
    Player playerWhite = updatePlayerStat(game, game.getPlayerWhite().getId());
    game.update();

    boolean playerWhiteIsFriendOfPlayerBlack = friendService.isPlayerFriendOf(playerWhite.getId(), playerBlack.getId());
    if (!playerWhiteIsFriendOfPlayerBlack) {
      Friend friend = new Friend();
      friend.setMe(playerWhite);
      friend.setFriendOf(playerBlack);

      friendService.save(friend);
    }
    boolean playerBlackIsFriendOfPlayerWhite = friendService.isPlayerFriendOf(playerBlack.getId(), playerWhite.getId());
    if (!playerBlackIsFriendOfPlayerWhite) {
      Friend friend = new Friend();
      friend.setMe(playerBlack);
      friend.setFriendOf(playerWhite);

      friendService.save(friend);
    }
    return game;
  }

  private Player updatePlayerStat(Game game, long playerId) {
    Player player = playerService.find(playerId);
    player.setGamePlayed(player.getGamePlayed() + 1);
    final GameDto.GameEnds playEndStatus = game.getPlayEndStatus();
    if (null != playEndStatus) {
      boolean whiteWon = false, blackWon = false, white = false, draw = false;
      final boolean blackWin = GameDto.GameEnds.BLACK_WIN.equals(playEndStatus);
      final boolean whiteWin = GameDto.GameEnds.WHITE_WIN.equals(playEndStatus);
      final boolean blackSurrender = GameDto.GameEnds.SURRENDER_BLACK.equals(playEndStatus);
      final boolean whiteSurrender = GameDto.GameEnds.SURRENDER_WHITE.equals(playEndStatus);
      final boolean blackLeft = GameDto.GameEnds.BLACK_LEFT.equals(playEndStatus);
      final boolean whiteLeft = GameDto.GameEnds.WHITE_LEFT.equals(playEndStatus);
      Player opponent;
      // если игрок играет черными
      if (Objects.equals(game.getPlayerBlack().getId(), player.getId())) {
        opponent = playerService.find(game.getPlayerWhite().getId());
        if (blackWin || whiteLeft || whiteSurrender) {
          player.setGameWon(player.getGameWon() + 1);
          blackWon = true;
          opponent.setGameLost(opponent.getGameLost() + 1);
        }
      } else {
        // если играет белыми
        opponent = playerService.find(game.getPlayerBlack().getId());
        if (whiteWin || blackLeft || blackSurrender) {
          player.setGameWon(player.getGameWon() + 1);
          whiteWon = true;
          white = true;
          opponent.setGameLost(opponent.getGameLost() + 1);
        }
      }
      if (!whiteWon && !blackWon) {
        player.setGameDraw(opponent.getGameDraw() + 1);
        opponent.setGameDraw(opponent.getGameDraw() + 1);
      }
      playerService.save(opponent);
      player.setRating(Rating.calcPlayerRating(player.getRating(), white, blackWon, whiteWon));
    }
    return playerService.save(player);
  }

  public Game find(Long gameId) {
    return Game.getInstance().find(gameId);
  }

  public List<Move> findGameMoves(Long gameId) {
    return GameMessage.getInstance().findGameMoves(gameId);
  }
}
