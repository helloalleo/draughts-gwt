package online.draughts.rus.server.service;

import com.google.inject.Inject;
import online.draughts.rus.server.BaseTest;
import online.draughts.rus.server.domain.Friend;
import online.draughts.rus.server.domain.Player;
import online.draughts.rus.server.guice.DatabaseModule;
import online.draughts.rus.server.guice.DbModule;
import org.jukito.JukitoRunner;
import org.jukito.UseModules;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 08.12.15
 * Time: 14:39
 */
@RunWith(JukitoRunner.class)
@UseModules({DatabaseModule.class, DbModule.class})
public class FriendServiceTest extends BaseTest {

  @Inject
  private PlayerService playerService;

  @Inject
  private FriendService friendService;

  @Before
  public void register() {
  }

  @Test
  public void testFindFriends() throws Exception {
    Player playerWhite = createPlayer();
    playerWhite = playerService.save(playerWhite);
    Player playerBlack = createPlayer();
    playerBlack = playerService.save(playerBlack);
    Friend friend = new Friend();
    friend.setMe(playerWhite);
    friend.setFriendOf(playerBlack);

    friend = friendService.save(friend);
    assertNotNull(friend.getId());

    friend = new Friend();
    friend.setMe(playerWhite);
    friend.setFriendOf(playerBlack);

    friend = friendService.save(friend);
    assertNotNull(friend.getId());

    List<Friend> friends = friendService.findFriends(playerWhite.getId());
    assertEquals(1, friends.size());
  }

  @Test
  public void testFindById() throws Exception {

  }

  @Test
  public void testIsPlayerFriendOf() throws Exception {

  }
}