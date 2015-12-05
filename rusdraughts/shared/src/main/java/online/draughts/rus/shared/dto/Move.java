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
public class Move {

  private int number; // номер пары хода
  private int moveOrder;
  private boolean first; // первый ход в паре ходов. Например, ee-aa в ee-aa bb-cc

  private String startPos;

  private String endPos;

  private String takenPos;

  private Set<MoveFlags> moveFlags = new HashSet<>();

  private String title;

  private String comment;

  private Set<String> hashTags = new HashSet<>();

  public Move() {
  }

  public Move(int number, boolean first, String startPos, String endPos, String takenPos,
              Set<MoveFlags> moveFlags) {
    this.number = number;
    this.first = first;
    this.moveFlags = moveFlags;

    this.startPos = startPos;
    this.endPos = endPos;
    this.takenPos = takenPos;
  }

  @GwtIncompatible
  public Move(Move move) {
    this(move.getNumber(), move.isFirst(), move.getStartPos(), move.getEndPos(),
        move.getTakenPos(), move.getMoveFlags());
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
