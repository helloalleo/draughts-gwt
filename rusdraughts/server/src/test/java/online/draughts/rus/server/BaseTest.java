package online.draughts.rus.server;

import online.draughts.rus.server.domain.Draught;
import online.draughts.rus.server.domain.Game;
import online.draughts.rus.server.domain.Move;
import online.draughts.rus.server.domain.Player;
import online.draughts.rus.server.objectify.EmbeddedDataStore;
import online.draughts.rus.shared.dto.MoveDto;
import online.draughts.rus.shared.dto.PlayerDto;
import org.junit.Rule;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

public class BaseTest {

  @Rule
  public EmbeddedDataStore store = new EmbeddedDataStore();

  private SecureRandom random = new SecureRandom();

  public String randomString() {
    return new BigInteger(130, random).toString(32);
  }

  protected Player createPlayer() {
    Player player = new Player();
    player.setActive(false);
    player.setRating(random.nextInt());
    player.setAuthProvider(PlayerDto.AuthProvider.FACEBOOK);
    player.setPlayerName(randomString());
    player.setRegisterDate(new Date());
    Map<Long, Integer> friendUnreadMessages = new HashMap<>();
    friendUnreadMessages.put(1L, 1);
    player.setFriendUnreadMessagesMap(friendUnreadMessages);
    return player;
  }

  protected Game createGame() {
    Game game = new Game();
    Set<Draught> initialPos = new HashSet<>();
    initialPos.add(new Draught(0, 0, true, false));
    game.setInitialPos(initialPos);
    game.setPlayerBlack(createPlayer());
    return game;
  }

  protected Move createMove() {
    Move move = new Move();
    Set<MoveDto.MoveFlags> moveFlagsSet = new HashSet<>();
    moveFlagsSet.add(MoveDto.MoveFlags.CANCEL_MOVE);
    move.setMoveFlags(moveFlagsSet);
    move.setMovedDraught(new Draught());
    move.setMovingDraught(new Draught());
    return move;
  }
}
