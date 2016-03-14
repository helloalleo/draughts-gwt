package online.draughts.rus.server.service;

import com.google.inject.Inject;
import online.draughts.rus.server.BaseTest;
import online.draughts.rus.server.domain.GameMessage;
import online.draughts.rus.server.domain.Player;
import online.draughts.rus.server.guice.DatabaseModule;
import org.jukito.JukitoRunner;
import org.jukito.UseModules;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 13.12.15
 * Time: 14:20
 */
@RunWith(JukitoRunner.class)
@UseModules({DatabaseModule.class})
public class MailServiceTest extends BaseTest {

  @Inject
  private MailService mailService;

  @Test
  public void testSendNotification() throws Exception {
    Player sender = createPlayer();
    sender.update();
    Player receiver = createPlayer();
    receiver.setEmail(randomString());
    receiver.update();

    GameMessage gameMessage = new GameMessage();
    gameMessage.setSender(sender);
    gameMessage.setReceiver(receiver);
    gameMessage.update();

    boolean success = mailService.sendNotification(gameMessage);
    assertTrue(success);
  }
}