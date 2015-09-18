package online.shashki.ru.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 02.09.15
 * Time: 17:02
 */
public interface Move extends IsSerializable {

  int getNumber();

  Move setNumber(int number);

  boolean isFirst();

  Move setFirst(boolean first);

  String getStartPos();

//  Move setStartSquare(Square startSquare);

  String getEndPos();

//  Move setEndSquare(Square endSquare);

  String getTakenPos();

//  Move setTakenSquare(Square takenSquare);

  Set<MoveFlags> getMoveFlags();

  enum MoveFlags {
    CANCEL_MOVE, // ход отменяется
    SIMPLE_MOVE, // ход без взятия
    CONTINUE_BEAT, // продолжить брать
    START_BEAT, // начало взятия
    STOP_BEAT // конец взятие
  }
}
