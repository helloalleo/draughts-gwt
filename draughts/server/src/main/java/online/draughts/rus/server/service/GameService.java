package online.draughts.rus.server.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import online.draughts.rus.server.config.Config;
import online.draughts.rus.server.domain.*;
import online.draughts.rus.server.util.CloudStoreUtils;
import online.draughts.rus.server.util.Rating;
import online.draughts.rus.shared.dto.GameDto;
import online.draughts.rus.shared.dto.PlayerDto;
import online.draughts.rus.shared.util.DozerHelper;
import org.apache.log4j.Logger;
import org.dozer.Mapper;

import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
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

  private final Logger logger = Logger.getLogger(GameService.class);

  private final PlayerService playerService;
  private final FriendService friendService;
  private final Mapper mapper;

  @Inject
  public GameService(
      PlayerService playerService,
      FriendService friendService,
      Mapper mapper) {
    this.playerService = playerService;
    this.friendService = friendService;
    this.mapper = mapper;
  }

  public List<GameDto> findRange(int offset, int limit) {
    return DozerHelper.map(mapper, Game.getInstance().findRange(offset, limit), GameDto.class);
  }

  public List<GameDto> findUserGames(HttpSession session, int offset, int limit) {
    final Player loggedInUser = playerService.getLoggedInUser(session);
    if (loggedInUser == null) {
      throw new RuntimeException("Not authorized");
    }
    List<Game> games = findUserGames(loggedInUser.getId(), offset, limit);
    return DozerHelper.map(mapper, games, GameDto.class);
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

  public GameDto save(GameDto gameDto) {
    Game game = mapper.map(gameDto, Game.class);
    save(game);
    return mapper.map(game, GameDto.class);
  }

  public Game save(Game game) {
    if (game.getId() == 0) {
      Player playerBlack = game.getPlayerBlack();
      Player playerWhite = game.getPlayerWhite();
      List<Game> gamesBlack = Game.getInstance().findByPlayerId(playerBlack.getId());
      List<Game> gamesWhite = Game.getInstance().findByPlayerId(playerWhite.getId());
      int totalGamesBlack = gamesBlack.size();
      int totalGamesWhite = gamesWhite.size();
      if (Math.min(totalGamesBlack, totalGamesWhite) > Config.LIMIT_GAMES_OVER_REMOVE) {
        removeGame(gamesBlack, totalGamesBlack);
        removeGame(gamesWhite, totalGamesWhite);
      }
      game.update();
      return game;
    }

    try {
      final String endGameScreenshot = game.getEndGameScreenshot();
      if (null != endGameScreenshot) {
        final String path = Config.GAMES_ENDS_PATH + game.getId() + ".png";
        CloudStoreUtils.saveFileToCloud(path, DatatypeConverter.parseBase64Binary(endGameScreenshot.split(",")[1]));
        game.setEndGameScreenshotUrl(path);
        // не сохраняем скриншот в дб так как сохранили его в облако
        game.setEndGameScreenshot(null);

        // игра закончена, сохраняем статистику и друзей
        Player playerWhite = game.getPlayerWhite();
        Player playerBlack = game.getPlayerBlack();
//        Player playerBlack = updatePlayerStat(game, game.getPlayerBlack().getId());
//        Player playerWhite = updatePlayerStat(game, game.getPlayerWhite().getId());
        boolean playerWhiteIsFriendOfPlayerBlack = friendService.isPlayerFriendOf(playerWhite.getId(), playerBlack.getId());
        saveFriend(playerWhite, playerBlack, playerWhiteIsFriendOfPlayerBlack);
        boolean playerBlackIsFriendOfPlayerWhite = friendService.isPlayerFriendOf(playerBlack.getId(), playerWhite.getId());
        saveFriend(playerBlack, playerWhite, playerBlackIsFriendOfPlayerWhite);
      }
      final String currentStateScreenshot = game.getCurrentStateScreenshot();
      if (null != currentStateScreenshot) {
        // если сохраняем текущую игру, тогда сохраняем ее как новую
        game.setId(0);
        final String path = Config.GAME_CURRENT_STATE_PATH + game.getId() + "/"
            + currentStateScreenshot.hashCode() + ".png";
        CloudStoreUtils.saveFileToCloud(path, DatatypeConverter.parseBase64Binary(currentStateScreenshot.split(",")[1]));
        game.setCurrentStateScreenshotUrl(path);
        game.setPlayFinishDate(new Date());
        game.setGameSnapshot(true);
      }
    } catch (GeneralSecurityException | IOException e) {
      logger.error("An error occured while storing file " + e.getMessage(), e);
    }

    game.update();
    return game;
  }

  private void removeGame(List<Game> games, int totalGames) {
    if (totalGames > Config.LIMIT_GAMES_OVER_REMOVE) {
      List<Game> toBeDeleted = games.subList(Config.LIMIT_GAMES_OVER_REMOVE, games.size() - 1);
      for (Game game : toBeDeleted) {
        game.setDeleted(true);
        game.update();
      }
    }
  }

  private void saveFriend(Player playerBlack, Player playerWhite, boolean playerBlackIsFriendOfPlayerWhite) {
    if (!playerBlackIsFriendOfPlayerWhite) {
      Friend friend = new Friend();
      friend.setMe(playerBlack);
      friend.setFriendOf(playerWhite);

      friendService.save(friend);
    }
  }

  public PlayerDto updatePlayersStat(long gameId, long playerId) {
    return mapper.map(updatePlayerStat(gameId, playerId), PlayerDto.class);
  }

  private Player updatePlayerStat(long gameId, long playerId) {
    Game game = Game.getInstance().find(gameId);
    Player player = playerService.find(playerId);
    player.setGamePlayed(player.getGamePlayed() + 1);
    final GameDto.GameEnd playEndStatus = game.getPlayEndStatus();
    if (null != playEndStatus) {
      boolean whiteWon = false, blackWon = false, white = false;
      final boolean blackWin = GameDto.GameEnd.BLACK_WIN.equals(playEndStatus);
      final boolean whiteWin = GameDto.GameEnd.WHITE_WIN.equals(playEndStatus);
      final boolean blackSurrender = GameDto.GameEnd.SURRENDER_BLACK.equals(playEndStatus);
      final boolean whiteSurrender = GameDto.GameEnd.SURRENDER_WHITE.equals(playEndStatus);
      final boolean blackLeft = GameDto.GameEnd.BLACK_LEFT.equals(playEndStatus);
      final boolean whiteLeft = GameDto.GameEnd.WHITE_LEFT.equals(playEndStatus);
      Player opponent;
      // если игрок играет черными
      if (Objects.equals(game.getPlayerBlack().getId(), player.getId())) {
        opponent = playerService.find(game.getPlayerWhite().getId());
        if (blackWin || whiteLeft || whiteSurrender) {
          player.setGameWon(player.getGameWon() + 1);
          blackWon = true;
          whiteWon = false;
          opponent.setGameLost(opponent.getGameLost() + 1);
        } else if (whiteWin || blackSurrender) {
          player.setGameLost(player.getGameLost() + 1);
          blackWon = false;
          whiteWon = true;
          opponent.setGameWon(opponent.getGameWon() + 1);
        }
      } else {
        // если играет белыми
        opponent = playerService.find(game.getPlayerBlack().getId());
        if (whiteWin || blackLeft || blackSurrender) {
          player.setGameWon(player.getGameWon() + 1);
          whiteWon = true;
          blackWon = false;
          white = true;
          opponent.setGameLost(opponent.getGameLost() + 1);
        } else if (blackWin || whiteSurrender) {
          player.setGameLost(player.getGameLost() + 1);
          whiteWon = false;
          blackWon = true;
          white = true;
          opponent.setGameWon(opponent.getGameWon() + 1);
        }
      }
      boolean draw = !whiteWon && !blackWon;
      if (draw) {
        player.setGameDraw(opponent.getGameDraw() + 1);
        opponent.setGameDraw(opponent.getGameDraw() + 1);
      }
      playerService.save(opponent);
      player.setRating(Rating.calcPlayerRating(player.getRating(), white, blackWon, whiteWon, draw));
    }
    return playerService.save(player);
  }

  public Game find(Long gameId) {
    return Game.getInstance().find(gameId);
  }

  public GameDto findDto(Long gameId) {
    if (null == gameId) {
      return null;
    }
    final Game source = find(gameId);
    if (null != source) {
      return mapper.map(source, GameDto.class);
    }
    return null;
  }

  public List<Move> findGameMoves(Long gameId) {
    return GameMessage.getInstance().findGameMoves(gameId);
  }
}
