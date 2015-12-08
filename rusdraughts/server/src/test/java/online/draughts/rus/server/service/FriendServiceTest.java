package online.draughts.rus.server.service;

import com.google.inject.Inject;
import com.googlecode.objectify.ObjectifyService;
import online.draughts.rus.server.BaseTest;
import online.draughts.rus.server.domain.Friend;
import online.draughts.rus.server.domain.Player;
import online.draughts.rus.server.guice.DatabaseModule;
import online.draughts.rus.server.guice.DbModule;
import online.draughts.rus.server.objectify.EmbeddedDataStore;
import online.draughts.rus.server.objectify.ObjectifyRegistrar;
import org.jukito.JukitoRunner;
import org.jukito.UseModules;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.Closeable;
import java.io.IOException;
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
  @Rule
  public EmbeddedDataStore store = new EmbeddedDataStore();
  private Closeable closeable;

  @Inject
  private PlayerService playerService;

  @Inject
  private FriendService friendService;

  @Before
  public void register() {
    closeable = ObjectifyService.begin();

    ObjectifyRegistrar.registerDataModel();
  }


  @After
  public void tearDown() throws IOException {
    closeable.close();
  }

  @Test
  public void testFindFriends() throws Exception {
    Player playerWhite = createPlayer();
    playerWhite = playerService.save(playerWhite);
    Player playerBlack = createPlayer();
    playerBlack = playerService.save(playerBlack);
    Friend friend = new Friend(playerWhite.getId(), playerBlack.getId());
    friend.setFriend(playerWhite);
    friend.setFriendOf(playerBlack);

    friend = friendService.save(friend);
    assertNotNull(friend.getId());

    friend = new Friend(playerWhite.getId(), playerBlack.getId());
    friend.setFriend(playerWhite);
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