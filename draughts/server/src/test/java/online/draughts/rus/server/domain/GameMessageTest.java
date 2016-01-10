package online.draughts.rus.server.domain;

import online.draughts.rus.server.BaseTest;
import online.draughts.rus.shared.dto.GameMessageDto;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 12.12.15
 * Time: 13:16
 */
public class GameMessageTest extends BaseTest {

  @Test
  public void testFindLastMessages() throws Exception {
    Player player = createPlayer();
    player.update();

    Player friend = createPlayer();
    friend.update();

    GameMessage gameMessage1 = new GameMessage();
    gameMessage1.setSender(player);
    gameMessage1.setReceiver(friend);
    gameMessage1.setMessage(randomString());
    gameMessage1.setMessageType(GameMessageDto.MessageType.CHAT_PRIVATE_MESSAGE);
    gameMessage1.setSentDate(new Date());
    gameMessage1.update();

    GameMessage gameMessage2 = new GameMessage();
    gameMessage2.setSender(friend);
    gameMessage2.setReceiver(player);
    gameMessage2.setMessage(randomString());
    gameMessage2.setMessageType(GameMessageDto.MessageType.CHAT_PRIVATE_MESSAGE);
    gameMessage2.setSentDate(new Date());
    gameMessage2.update();

    List<GameMessage> gameMessages = GameMessage.getInstance().findAll();
    assertEquals(2, gameMessages.size());
    gameMessages = GameMessage.getInstance().findLastMessages(10, friend.getId(), player.getId());
    for (GameMessage gameMessage : gameMessages) {
      System.out.println(gameMessage.getMessage());
    }
    assertEquals(2, gameMessages.size());
  }
}