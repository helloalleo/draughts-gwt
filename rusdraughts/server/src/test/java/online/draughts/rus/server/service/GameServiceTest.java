package online.draughts.rus.server.service;

import com.google.inject.Inject;
import online.draughts.rus.server.BaseTest;
import online.draughts.rus.server.guice.DatabaseModule;
import online.draughts.rus.server.guice.DbModule;
import online.draughts.rus.shared.model.Game;
import online.draughts.rus.shared.model.GameMessage;
import online.draughts.rus.shared.model.Move;
import online.draughts.rus.shared.model.Player;
import org.jukito.JukitoRunner;
import org.jukito.UseModules;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 23.10.15
 * Time: 20:30
 */
@RunWith(JukitoRunner.class)
@UseModules({DatabaseModule.class, DbModule.class})
public class GameServiceTest extends BaseTest {

  @Inject
  private GameService gameService;

  @Inject
  private PlayerService playerService;

  @Before
  public void setUp() throws Exception {

  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void testFindRange() throws Exception {
    List<Game> games = gameService.findRange(0, 2);
    assertEquals(2, games.size());
  }

  @Test
  public void testFindUserGames() throws Exception {
  }

  @Test
  public void testSaveOrCreate() throws Exception {
    Player playerWhite = new Player();
    playerWhite.setPlayerName(randomString());
    playerWhite = playerService.saveOrCreateOnServer(playerWhite);

    Player playerBlack = new Player();
    playerBlack.setPlayerName(randomString());
    playerBlack = playerService.saveOrCreateOnServer(playerBlack);

    Game game = new Game(playerWhite, playerBlack, Game.GameEnds.BLACK_LEFT, new Date(), new Date(), "", "");
    // create
    game = gameService.saveOrCreate(game);
    assertNotNull(game);

    GameMessage gameMessage = new GameMessage();
    gameMessage.setGame(game);
    gameMessage.setMessageType(GameMessage.MessageType.PLAY_MOVE);
    Move move = new Move(1, true, gameMessage, "3,4", "4,5", null,
        new HashSet<Move.MoveFlags>() {{
          add(Move.MoveFlags.SIMPLE_MOVE);
        }});
    move.setGameMessage(gameMessage);
    gameMessage.setMove(move);
    game.getGameMessages().add(gameMessage);
    game = gameService.saveOrCreate(game);

    gameMessage = new GameMessage();
    gameMessage.setMessageType(GameMessage.MessageType.PLAY_MOVE);
    gameMessage.setGame(game);
    move = new Move(2, false, gameMessage, "4,3", "2,3", null,
        new HashSet<Move.MoveFlags>() {{
          add(Move.MoveFlags.SIMPLE_MOVE);
        }});
    move.setGameMessage(gameMessage);
    gameMessage.setMove(move);
    game.getGameMessages().add(gameMessage);
    game = gameService.saveOrCreate(game);

    final List<GameMessage> gameMessages = Arrays.asList(game.getGameMessages().toArray(new GameMessage[0])).stream().sorted(((o1, o2) -> o1.getId().compareTo(o2.getId()))).collect(Collectors.toList());
    final GameMessage gameMessage1 = gameMessages.get(0);
    final GameMessage gameMessage2 = gameMessages.get(1);
    assertEquals("1. <span id='" + game.getId() + ":0' data='" + gameMessage1.getMove().getId() + "'>d4-c5</span> <span id='" + game.getId() + ":1' data='" + gameMessage2.getMove().getId() + "'>d4-d6</span><br>", game.getNotation());

    // save
    playerBlack = playerService.find(playerBlack.getId());
    assertEquals(1, playerBlack.getGameLose());
    playerWhite = playerService.find(playerWhite.getId());
    assertEquals(1, playerWhite.getGameWin());
  }

  @Test
  public void testFind() throws Exception {

  }
}