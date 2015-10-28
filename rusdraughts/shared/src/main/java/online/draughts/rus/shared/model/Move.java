package online.draughts.rus.shared.model;

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

  private int number;
  private boolean first; // первый ход в паре ходов. Например, ee-aa в ee-aa bb-cc

  @OneToOne
  @JoinColumn(name = "message_id", nullable = false, updatable = false)
  private GameMessage gameMessage;

  @Column(name = "start_pos")
  private String startPos;

  @Column(name = "end_pos")
  private String endPos;

  @Column(name = "taken_pos")
  private String takenPos;

  @Column(name = "move_flags")
  @ElementCollection
  private Set<MoveFlags> moveFlags = new HashSet<>();

  private String title;

  private String comment;

  @Column(name = "hash_tags")
  @ElementCollection
  private Set<String> hashTags = new HashSet<>();

  public Move() {
  }

  public Move(int number, boolean first, GameMessage gameMessage, String startPos, String endPos, String takenPos,
              Set<MoveFlags> moveFlags) {
    this.number = number;
    this.first = first;
    this.moveFlags = moveFlags;

    this.gameMessage = gameMessage;

    this.startPos = startPos;
    this.endPos = endPos;
    this.takenPos = takenPos;
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

  public Move setTakenPos(String takenPos) {
    this.takenPos = takenPos;
    return this;
  }

  public String getStartPos() {
    return startPos;
  }

  public Move setStartPos(String startPos) {
    this.startPos = startPos;
    return this;
  }

  public String getEndPos() {
    return endPos;
  }

  public Move setEndPos(String endPos) {
    this.endPos = endPos;
    return this;
  }

  public Set<MoveFlags> getMoveFlags() {
    return moveFlags;
  }

  public Move setMoveFlags(Set<MoveFlags> moveFlags) {
    this.moveFlags = moveFlags;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public Move setTitle(String title) {
    this.title = title;
    return this;
  }

  public String getComment() {
    return comment;
  }

  public Move setComment(String comment) {
    this.comment = comment;
    return this;
  }

  public Set<String> getHashTags() {
    return hashTags;
  }

  public Move setHashTags(Set<String> hashTags) {
    this.hashTags = hashTags;
    return this;
  }

  @Override
  public String toString() {
    return "Move{" +
        "number=" + number +
        ", first=" + first +
        ", startPos='" + startPos + '\'' +
        ", endPos='" + endPos + '\'' +
        ", takenPos='" + takenPos + '\'' +
        ", moveFlags=" + moveFlags +
        '}';
  }


 public enum MoveFlags {
    CANCEL_MOVE, // ход отменяется
    SIMPLE_MOVE, // ход без взятия
    CONTINUE_BEAT, // продолжить брать
    START_BEAT, // начало взятия
    STOP_BEAT // конец взятие
  }
}
