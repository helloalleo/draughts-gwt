package online.draughts.rus.server.objectify;

import com.googlecode.objectify.ObjectifyService;
import online.draughts.rus.server.domain.Friend;
import online.draughts.rus.server.domain.Player;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 08.12.15
 * Time: 16:00
 */
public class ObjectifyRegistrar {

  public static void registerDataModel() {
    ObjectifyService.register(Friend.class);
    ObjectifyService.register(Player.class);
  }
}
