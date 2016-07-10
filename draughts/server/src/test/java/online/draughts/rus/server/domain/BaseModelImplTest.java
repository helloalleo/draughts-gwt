package online.draughts.rus.server.domain;

import online.draughts.rus.server.BaseTest;
import online.draughts.rus.shared.dto.MoveDto;
import online.draughts.rus.shared.dto.PlayerDto;
import org.junit.Test;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 08.01.16
 * Time: 19:49
 */
public class BaseModelImplTest extends BaseTest {

  @Test
  public void testUpdate() throws Exception {

  }

  /**
   * Тестирование сохранения и получения полей объекта
   * String
   * int
   * boolean
   * date
   * map
   */
  @Test
  public void testGetSingleResultObject() throws Exception {
    Player player = createPlayer();
    // строка
    String playerName = player.getPlayerName();
    // целое число
    int rating = player.getRating();
    // перечисление
    PlayerDto.AuthProvider authProvider = player.getAuthProvider();
    // булевое
    boolean active = player.isActive();
    // дата
    Date date = player.getRegisterDate();
    // отображение
    Map<Long, Integer> map = player.getFriendUnreadMessagesMap();

    // сохранение объекта
    player.update();
    // сущность сохранилась
    assertNotEquals(0, player.getId());
    // строка сохранилась
    assertEquals(playerName, player.getPlayerName());
    // целое сохранилось
    assertEquals(rating, player.getRating());
    // перечисление сохранилось
    assertEquals(authProvider, player.getAuthProvider());
    // булевое сохранилось
    assertEquals(active, player.isActive());
    // дата сохранилась
    assertEquals(date, player.getRegisterDate());
    // отображение сохранилось
    assertEquals(map, player.getFriendUnreadMessagesMap());

    // находим игрока
    player = Player.getInstance().find(player.getId());
    // игрок найден
    assertNotEquals(0, player.getId());
    // строка сохранена
    assertEquals(playerName, player.getPlayerName());
    // целое сохранено
    assertEquals(rating, player.getRating());
    // перечисление сохранено
    assertEquals(authProvider, player.getAuthProvider());
    // булевое сохранилась
    assertEquals(active, player.isActive());
    // дата сохранилась
    assertEquals(date, player.getRegisterDate());
    // отображение сохранилось
    assertEquals(map, player.getFriendUnreadMessagesMap());
  }

  /**
   * Тестирование сохранения и получения полей объекта
   * set
   * model
   */
  @Test
  public void testGetSingleResultObject2() throws Exception {
    Game game = createGame();
    // множество
    Set<Draught> initialPos = game.getInitialPos();
    // модель
    Player model = game.getPlayerBlack();
    model.update();

    // сохранение объекта
    game.update();
    // сущность сохранилась
    assertNotEquals(0, game.getId());
    // множество сохранилась
    assertEquals(initialPos, game.getInitialPos());
    // модель сохранилось
    assertEquals(model, game.getPlayerBlack());

    // находим игрока
    game = Game.getInstance().find(game.getId());
    // игрок найден
    assertNotEquals(0, game.getId());
    // множество сохранилась
    assertEquals(initialPos, game.getInitialPos());
    // модель сохранилось
    assertEquals(model, game.getPlayerBlack());
  }

  /**
   * Тестирование сохранения перечисления через конвертер
   */
  @Test
  public void testGetSingleResultObject3() {
    Move move = createMove();
    Set<MoveDto.MoveFlag> moveFlagSet = move.getMoveFlags();
    move.update();

    assertNotEquals(0, move.getId());
    assertEquals(moveFlagSet, move.getMoveFlags());

    move = Move.getInstance().find(move.getId());
    assertNotEquals(0, move.getId());
    assertEquals(moveFlagSet, move.getMoveFlags());
  }
}
