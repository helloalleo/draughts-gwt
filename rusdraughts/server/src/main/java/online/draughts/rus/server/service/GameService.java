package online.draughts.rus.server.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import online.draughts.rus.server.dao.GameDao;
import online.draughts.rus.server.utils.Rating;
import online.draughts.rus.shared.model.Friend;
import online.draughts.rus.shared.model.Game;
import online.draughts.rus.shared.model.Move;
import online.draughts.rus.shared.model.Player;
import online.draughts.rus.shared.model.key.FriendId;
import online.draughts.rus.shared.util.StringUtils;

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
  private static final String NOTATION_SEP = "<br>";
  private static final String BEAT_SEP = ":";
  private static final String MOVE_SEP = " ";
  private static final String COUNT_SEP = ". ";

  private final Provider<GameDao> gameDaoProvider;
  private final PlayerService playerService;
  private final FriendService friendService;
  private final GameDao gameDao;
  private final GameMessageService gameMessageService;

  @Inject
  public GameService(Provider<GameDao> gameDaoProvider,
                     PlayerService playerService,
                     FriendService friendService,
                     GameDao gameDao,
                     GameMessageService gameMessageService) {
    this.gameDaoProvider = gameDaoProvider;
    this.playerService = playerService;
    this.friendService = friendService;
    this.gameDao = gameDao;
    this.gameMessageService = gameMessageService;
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
      game = gameDaoProvider.get().edit(game);
      createGameNotation(game);
      game = gameDaoProvider.get().edit(game);

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

  private void createGameNotation(Game game) {
    List<Move> moves = findGameMoves(game.getId());
    StringBuilder notation = new StringBuilder(moves.size());
    int pos = 0;
    for (Move move : moves) {
      Stroke stroke = new Stroke(move);
      boolean opponentMove = !move.isFirst();
      if (stroke.isSimple()) {
        if (stroke.isFirst()) {
          notation.append(stroke.getNumber())
              .append(COUNT_SEP)
              .append(wrapStroke(stroke.toNotation(opponentMove), move, pos));
        } else {
          notation.append(MOVE_SEP)
              .append(wrapStroke(stroke.toNotation(opponentMove), move, pos))
              .append(NOTATION_SEP);
        }
      } else { // взята одна или более шашек
        if (stroke.isStartBeat()) {
          if (stroke.isFirst()) {
            notation.append(stroke.getNumber())
                .append(COUNT_SEP)
                .append(wrapStroke(stroke.toNotation(opponentMove), move, pos));
          } else {
            if (stroke.isContinueBeat()) {
              notation.append(MOVE_SEP)
                  .append(wrapStroke(stroke.toNotation(opponentMove), move, pos));
            } else {
              notation.append(MOVE_SEP)
                  .append(wrapStroke(stroke.toNotation(opponentMove), move, pos))
                  .append(NOTATION_SEP);
            }
          }
        } else if (stroke.isStopBeat()) {
          if (stroke.isFirst()) {
            notation.append(BEAT_SEP)
                .append(wrapStroke(stroke.toNotationLastMove(), move, pos))
                .append(MOVE_SEP);
          } else {
            notation.append(BEAT_SEP)
                .append(wrapStroke(stroke.toNotationLastMove(), move, pos))
                .append(NOTATION_SEP);
          }
        } else if (stroke.isContinueBeat()) {
          notation.append(BEAT_SEP)
              .append(wrapStroke(stroke.toNotationLastMove(), move, pos));
        }
      }
      pos++;
    }
    game.setNotation(notation.toString());
  }

  private String wrapStroke(String notationStep, Move move, int pos) {
    return "<span id='" + move.getGameMessage().getGame().getId() + ":" + pos + "' "
        + "data='" + move.getId() + "'>"
        + notationStep
        + "</span>";
  }

  private static class Square {
    private static final String TO_SEND_SEP = ",";
    private static final int ROWS = 8;
    private static final int COLS = 8;
    private String[] alph = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};

    private final int row;
    private final int col;

    public Square(int row, int col) {
      this.row = row;
      this.col = col;
    }

    public static Square fromString(String toSendStr) {
      if (StringUtils.isEmpty(toSendStr)) {
        return null;
      }
      final String[] toSendArr = toSendStr.split(TO_SEND_SEP);
      final Integer row = Integer.valueOf(toSendArr[0]);
      final Integer col = Integer.valueOf(toSendArr[1]);
      return new Square(row, col);
    }

    public String toNotation(boolean normal) {
      if (normal) {
        return alph[col] + String.valueOf(ROWS - row);
      }
      return alph[COLS - 1 - col] + String.valueOf(row + 1);
    }
  }

  private class Stroke extends Move {
    public static final String SIMPLE_MOVE_SEP = "-";
    public static final String BEAT_MOVE_SEP = ":";

    private Square startSquare;
    private Square endSquare;
    private Square takenSquare;

    public Stroke(Move move) {
      setFirst(move.isFirst());
      setNumber(move.getNumber());
      setMoveFlags(move.getMoveFlags());
      setStartSquare(Square.fromString(move.getStartPos()));
      setEndSquare(Square.fromString(move.getEndPos()));
      setTakenSquare(Square.fromString(move.getTakenPos()));
    }

    public Square getTakenSquare() {
      return takenSquare;
    }

    public Stroke setTakenSquare(Square takenSquare) {
      this.takenSquare = takenSquare;
      return this;
    }

    public Square getStartSquare() {
      return startSquare;
    }

    public Stroke setStartSquare(Square startSquare) {
      this.startSquare = startSquare;
      return this;
    }

    public Square getEndSquare() {
      return endSquare;
    }

    public Stroke setEndSquare(Square endSquare) {
      this.endSquare = endSquare;
      return this;
    }

    public String toNotation(boolean isWhite) {
      String notation;
      if (isFirst() && isWhite) {
        notation = getNotation(true);
      } else if (!isFirst() && isWhite) {
        notation = getNotation(true);
      } else if (isFirst()) {
        notation = getNotation(false);
      } else {
        notation = getNotation(false);
      }
      return notation;
    }

    private String getNotation(boolean normal) {
      final String s = startSquare.toNotation(normal);
      final String e = endSquare.toNotation(normal);
      return isSimple() ? s + SIMPLE_MOVE_SEP + e
          : s + BEAT_MOVE_SEP + e;
    }

    public String toNotationLastMove() {
      return endSquare.toNotation(isFirst());
    }


    public boolean isCancel() {
      return getMoveFlags().contains(Move.MoveFlags.CANCEL_MOVE);
    }

    public boolean isSimple() {
      return getMoveFlags().contains(Move.MoveFlags.SIMPLE_MOVE);
    }

    public boolean isContinueBeat() {
      return getMoveFlags().contains(Move.MoveFlags.CONTINUE_BEAT);
    }

    public boolean isStopBeat() {
      return getMoveFlags().contains(Move.MoveFlags.STOP_BEAT);
    }

    public boolean isStartBeat() {
      return getMoveFlags().contains(Move.MoveFlags.START_BEAT);
    }
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

  public Game find(Long gameId) {
    return gameDaoProvider.get().find(gameId);
  }

  public List<Move> findGameMoves(Long gameId) {
    return gameDaoProvider.get().findGameMoves(gameId);
  }
}
