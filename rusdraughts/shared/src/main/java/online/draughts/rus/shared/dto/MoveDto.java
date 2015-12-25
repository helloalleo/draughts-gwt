package online.draughts.rus.shared.dto;


import com.google.gwt.core.shared.GwtIncompatible;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.09.15
 * Time: 21:45
 */
public class MoveDto extends BaseDto {

  private int number; // номер пары хода
  private int moveOrder;
  private boolean first; // первый ход в паре ходов. Например, ee-aa в ee-aa bb-cc

  private DraughtDto startPos;

  private DraughtDto endPos;

  private DraughtDto takenPos;

  private Set<MoveFlags> moveFlags = new HashSet<>();

  private String title;

  private String comment;

  private Set<String> hashTags = new HashSet<>();

  public MoveDto() {
  }

  public MoveDto(int number, boolean first, DraughtDto startPos, DraughtDto endPos, DraughtDto takenPos,
                 Set<MoveFlags> moveFlags) {
    this.number = number;
    this.first = first;
    this.moveFlags = moveFlags;

    this.startPos = startPos;
    this.endPos = endPos;
    this.takenPos = takenPos;
  }

  @GwtIncompatible
  public MoveDto(MoveDto move) {
    this(move.getNumber(), move.isFirst(), move.getStartPos(), move.getEndPos(),
        move.getTakenPos(), move.getMoveFlags());
  }

  public int getNumber() {
    return number;
  }

  public MoveDto setNumber(int number) {
    this.number = number;
    return this;
  }

  public boolean isFirst() {
    return first;
  }

  public MoveDto setFirst(boolean first) {
    this.first = first;
    return this;
  }

  public DraughtDto getTakenPos() {
    return takenPos;
  }

  public MoveDto setTakenPos(DraughtDto takenPos) {
    this.takenPos = takenPos;
    return this;
  }

  public DraughtDto getStartPos() {
    return startPos;
  }

  public MoveDto setStartPos(DraughtDto startPos) {
    this.startPos = startPos;
    return this;
  }

  public DraughtDto getEndPos() {
    return endPos;
  }

  public MoveDto setEndPos(DraughtDto endPos) {
    this.endPos = endPos;
    return this;
  }

  public Set<MoveFlags> getMoveFlags() {
    return moveFlags;
  }

  public MoveDto setMoveFlags(Set<MoveFlags> moveFlags) {
    this.moveFlags = moveFlags;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public MoveDto setTitle(String title) {
    this.title = title;
    return this;
  }

  public String getComment() {
    return comment;
  }

  public MoveDto setComment(String comment) {
    this.comment = comment;
    return this;
  }

  public Set<String> getHashTags() {
    return hashTags;
  }

  public MoveDto setHashTags(Set<String> hashTags) {
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

  public int getMoveOrder() {
    return moveOrder;
  }

  public void setMoveOrder(int order) {
    this.moveOrder = order;
  }

  public enum MoveFlags {
    CANCEL_MOVE, // ход отменяется
    SIMPLE_MOVE, // ход без взятия
    CONTINUE_BEAT, // продолжить брать
    START_BEAT, // начало взятия
    STOP_BEAT // конец взятие
  }
}
