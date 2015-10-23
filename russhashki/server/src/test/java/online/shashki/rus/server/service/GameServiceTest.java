package online.shashki.rus.server.service;

import com.google.inject.Inject;
import online.shashki.rus.server.BaseTest;
import online.shashki.rus.server.guice.DatabaseModule;
import online.shashki.rus.server.guice.DbModule;
import online.shashki.rus.shared.model.Game;
import online.shashki.rus.shared.model.Player;
import org.jukito.JukitoRunner;
import org.jukito.UseModules;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

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
    game = gameService.saveOrCreate(game);
    assertNotNull(game);
    game = gameService.saveOrCreate(game);
    assertNotNull(game);
  }

  @Test
  public void testFind() throws Exception {

  }
}