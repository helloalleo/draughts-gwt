package online.draughts.rus.server.util;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 15.11.15
 * Time: 14:39
 */
public class Move {
  public enum MoveFlags {
    CANCEL_MOVE, // ход отменяется
    SIMPLE_MOVE, // ход без взятия
    CONTINUE_BEAT, // продолжить брать
    START_BEAT, // начало взятия
    STOP_BEAT // конец взятие
  }
}
