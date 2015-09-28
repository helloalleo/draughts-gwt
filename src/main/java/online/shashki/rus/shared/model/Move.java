package online.shashki.rus.shared.model;

import com.google.gwt.user.client.rpc.GwtTransient;
import online.shashki.rus.client.utils.SHLog;
import online.shashki.rus.shashki.Square;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.09.15
 * Time: 21:45
 */
@Entity
@Table(name = "move")
public class Move extends PersistableObjectImpl {

  @GwtTransient
  @JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  @Transient
  private static final String SIMPLE_MOVE_SEP = "-";
  @GwtTransient
  @JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  @Transient
  private static final String BEAT_MOVE_SEP = ":";

  private int number;
  private boolean first; // первый ход в паре ходов. Например, ee-aa в ee-aa bb-cc

  @OneToOne
  @JoinColumn(name = "message_id", nullable = false, updatable = false)
  private GameMessage gameMessage;

  @Column(name = "start_pos")
  private String startPos;
  @GwtTransient
  @JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  @Transient
  private Square startSquare;
  @Column(name = "end_pos")
  private String endPos;
  @GwtTransient
  @JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  @Transient
  private Square endSquare;
  @Column(name = "taken_pos")
  private String takenPos;
  @GwtTransient
  @JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  @Transient
  private Square takenSquare;

  @Column(name = "move_flags")
  @ElementCollection
  private Set<MoveFlags> moveFlags = new HashSet<>();

  public Move() {
  }

  public Move(int number, boolean first, GameMessage gameMessage, String startPos, String endPos, String takenPos,
              Set<MoveFlags> moveFlags) {
    this.number = number;
    this.first = first;
    this.gameMessage = gameMessage;
    this.startPos = startPos;
    this.endPos = endPos;
    this.takenPos = takenPos;
    this.moveFlags = moveFlags;
  }

  public Move(Move move) {
    this(move.getNumber(), move.isFirst(), move.getGameMessage(), move.getStartPos(), move.getEndPos(),
        move.getTakenPos(), move.getMoveFlags());
  }

  public GameMessage getGameMessage() {
    return gameMessage;
  }

  public Move setGameMessage(GameMessage gameMessage) {
    this.gameMessage = gameMessage;
    return this;
  }

  public int getNumber() {
    return number;
  }

  public Move setNumber(int number) {
    this.number = number;
    return this;
  }

  public boolean isFirst() {
    return first;
  }

  public Move setFirst(boolean first) {
    this.first = first;
    return this;
  }

  public String getTakenPos() {
    return takenPos;
  }

  public Move setTakenSquare(Square takenSquare) {
    this.takenSquare = takenSquare;
    if (takenSquare != null) {
      this.takenPos = takenSquare.toSend();
    }
    return this;
  }

  public Square getTakenSquare() {
    return takenSquare;
  }

  public void setTakenPos(String takenPos) {
    this.takenPos = takenPos;
  }

  public String getStartPos() {
    return startPos;
  }

  public void setStartPos(String startPos) {
    this.startPos = startPos;
  }

  public Square getStartSquare() {
    return startSquare;
  }

  public Move setStartSquare(Square startSquare) {
    this.startSquare = startSquare;
    if (startSquare != null) {
      this.startPos = startSquare.toSend();
    }
    return this;
  }

  public String getEndPos() {
    return endPos;
  }

  public void setEndPos(String endPos) {
    this.endPos = endPos;
  }

  public Square getEndSquare() {
    return endSquare;
  }

  public Move setEndSquare(Square endSquare) {
    this.endSquare = endSquare;
    if (endSquare != null) {
      this.endPos = endSquare.toSend();
    }
    return this;
  }

  @JsonIgnore
  public boolean isCancel() {
    return moveFlags.contains(MoveFlags.CANCEL_MOVE);
  }

  public void setOnCancelMove() {
    moveFlags.add(MoveFlags.CANCEL_MOVE);
  }

  public void setOffCancelMove() {
    moveFlags.remove(MoveFlags.CANCEL_MOVE);
  }

  @JsonIgnore
  public boolean isSimple() {
    return moveFlags.contains(MoveFlags.SIMPLE_MOVE);
  }

  public void setOnSimpleMove() {
    moveFlags.add(MoveFlags.SIMPLE_MOVE);
  }

  public void setOffSimpleMove() {
    moveFlags.remove(MoveFlags.SIMPLE_MOVE);
  }

  @JsonIgnore
  public boolean isContinueBeat() {
    return moveFlags.contains(MoveFlags.CONTINUE_BEAT);
  }

  public void setOnContinueBeat() {
    moveFlags.add(MoveFlags.CONTINUE_BEAT);
  }

  public void turnOffContinueBeat() {
    moveFlags.remove(MoveFlags.CONTINUE_BEAT);
  }

  @JsonIgnore
  public boolean isStopBeat() {
    return moveFlags.contains(MoveFlags.STOP_BEAT);
  }

  public void setOnStopBeat() {
    moveFlags.add(MoveFlags.STOP_BEAT);
  }

  public void setOffStopBeat() {
    moveFlags.remove(MoveFlags.STOP_BEAT);
  }

  @JsonIgnore
  public boolean isStartBeat() {
    return moveFlags.contains(MoveFlags.START_BEAT);
  }

  public void setOnStartBeat() {
    moveFlags.add(MoveFlags.START_BEAT);
  }

  public void setOffStartBeat() {
    moveFlags.remove(MoveFlags.START_BEAT);
  }

  public Set<MoveFlags> getMoveFlags() {
    return moveFlags;
  }

  public void setMoveFlags(Set<MoveFlags> moveFlags) {
    this.moveFlags = moveFlags;
  }

  public String toNotation(boolean isWhite) {
    String notation;
    if (first && isWhite) {
      SHLog.log("FIRST WHITE");
      notation = getNotation(true);
    } else if (!first && isWhite){
      SHLog.log("SECOND WHITE");
      notation = getNotation(true);
    } else if (first) {
      SHLog.log("FIRST BLACK");
      notation = getNotation(false);
    } else {
      SHLog.log("SECOND BLACK");
      notation = getNotation(false);
    }
    return notation;
  }

  private String getNotation(boolean normal) {
    final String s = startSquare.toNotation(normal);
    final String e = endSquare.toNotation(normal);
    return isSimple() ? s + SIMPLE_MOVE_SEP + e
        : s + BEAT_MOVE_SEP + e;
  }

  public String toNotationLastMove() {
    return endSquare.toNotation(first);
  }

  @Override
  public String toString() {
    return "Move{" +
        "number=" + number +
        ", first=" + first +
        ", gameMessage=" + gameMessage +
        ", startPos='" + startPos + '\'' +
        ", startSquare=" + startSquare +
        ", endPos='" + endPos + '\'' +
        ", endSquare=" + endSquare +
        ", takenPos='" + takenPos + '\'' +
        ", takenSquare=" + takenSquare +
        ", moveFlags=" + moveFlags +
        '}';
  }

  /**
   * Отражает ход на доске не изменяя сам объект
   *
   * @return moveDto отраженный объект
   */
  public Move mirror() {
    Move move = new Move();

    if (startSquare != null) {
      move.setStartSquare(startSquare.mirror());
    } else {
      final Square square = Square.getFromPos(startPos);
      if (square != null) {
        move.setStartSquare(square.mirror());
      }
    }
    if (endSquare != null) {
      move.setEndSquare(endSquare.mirror());
    } else {
      final Square square = Square.getFromPos(endPos);
      if (square != null) {
        move.setEndSquare(square.mirror());
      }
    }
    if (takenSquare != null) {
      move.setTakenSquare(takenSquare.mirror());
    } else {
      final Square square = Square.getFromPos(takenPos);
      if (square != null) {
        move.setTakenSquare(square.mirror());
      }
    }
    move.setMoveFlags(moveFlags);
    move.setFirst(first);
    move.setNumber(number);
    return move;
  }

  enum MoveFlags {
    CANCEL_MOVE, // ход отменяется
    SIMPLE_MOVE, // ход без взятия
    CONTINUE_BEAT, // продолжить брать
    START_BEAT, // начало взятия
    STOP_BEAT // конец взятие
  }
}
