package online.shashki.rus.shared.model;

import com.google.gwt.user.client.rpc.GwtTransient;
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
  @Transient
  private static final String SIMPLE_MOVE_SEP = "-";
  @GwtTransient
  @JsonIgnore
  @Transient
  private static final String BEAT_MOVE_SEP = ":";

  private int number;
  private boolean first; // первый ход в паре ходов. Например, ee-aa в ee-aa bb-cc

  @OneToOne
  @JoinColumn(name = "move_id", nullable = false, updatable = false)
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

  public GameMessage getGameMessage() {
    return gameMessage;
  }

  public Move setGameMessage(GameMessage gameMessage) {
    this.gameMessage = gameMessage;
    return this;
  }

  public Move setStartPos(String startPos) {
    this.startPos = startPos;
    return this;
  }

  public Move setEndPos(String endPos) {
    this.endPos = endPos;
    return this;
  }

  public Move setTakenPos(String takenPos) {
    this.takenPos = takenPos;
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
    return this;
  }

  public Square getTakenSquare() {
    return takenSquare;
  }

  public String getStartPos() {
    return startPos;
  }

  public Square getStartSquare() {
    return startSquare;
  }

  public Move setStartSquare(Square startSquare) {
    this.startSquare = startSquare;
    return this;
  }

  public String getEndPos() {
    return endPos;
  }

  public Square getEndSquare() {
    return endSquare;
  }

  public Move setEndSquare(Square endSquare) {
    this.endSquare = endSquare;
    return this;
  }

  public boolean isCancel() {
    return moveFlags.contains(MoveFlags.CANCEL_MOVE);
  }

  public void setOnCancelMove() {
    moveFlags.add(MoveFlags.CANCEL_MOVE);
  }

  public void setOffCancelMove() {
    moveFlags.remove(MoveFlags.CANCEL_MOVE);
  }

  public boolean isSimple() {
    return moveFlags.contains(MoveFlags.SIMPLE_MOVE);
  }

  public void setOnSimpleMove() {
    moveFlags.add(MoveFlags.SIMPLE_MOVE);
  }

  public void setOffSimpleMove() {
    moveFlags.remove(MoveFlags.SIMPLE_MOVE);
  }

  public boolean isContinueBeat() {
    return moveFlags.contains(MoveFlags.CONTINUE_BEAT);
  }

  public void setOnContinueBeat() {
    moveFlags.add(MoveFlags.CONTINUE_BEAT);
  }

  public void turnOffContinueBeat() {
    moveFlags.remove(MoveFlags.CONTINUE_BEAT);
  }

  public boolean isStopBeat() {
    return moveFlags.contains(MoveFlags.STOP_BEAT);
  }

  public void setOnStopBeat() {
    moveFlags.add(MoveFlags.STOP_BEAT);
  }

  public void setOffStopBeat() {
    moveFlags.remove(MoveFlags.STOP_BEAT);
  }

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

  public String toNotation() {
    return isSimple() ? startSquare.toNotation(first) + SIMPLE_MOVE_SEP + endSquare.toNotation(first)
        : startSquare.toNotation(first) + BEAT_MOVE_SEP + endSquare.toNotation(first);
  }

  public String toNotationLastMove() {
    return endSquare.toNotation(first);
  }

  @Override
  public String toString() {
    return "Move{" +
        "number=" + number +
        ", first=" + first +
        ", startSquare=" + startSquare +
        ", endSquare=" + endSquare +
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

    if (move.getStartSquare() != null) {
      move.setStartSquare(startSquare.mirror());
    }
    if (move.getEndSquare() != null) {
      move.setEndSquare(endSquare.mirror());
    }
    if (move.getTakenSquare() != null) {
      move.setTakenSquare(takenSquare.mirror());
    }
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
