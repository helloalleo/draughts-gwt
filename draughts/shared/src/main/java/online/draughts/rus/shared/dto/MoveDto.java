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
  private boolean fixate;

  /**
   * Ещё не перемещенная шашка
   */
  private DraughtDto movingDraught;

  /**
   * Перемещённая шашка
   */
  private DraughtDto movedDraught;

  /**
   * Сбитая шашка
   */
  private DraughtDto takenDraught;

  private Set<MoveFlag> moveFlags = new HashSet<>();

  private String title;

  private String comment;

  private Set<String> hashTags = new HashSet<>();
  private String screenshot;
  private boolean queen;
  private boolean white;

  public MoveDto() {
  }

  public MoveDto(int number, boolean first, boolean queen, boolean white,
                 DraughtDto movingDraught, DraughtDto movedDraught, DraughtDto takenDraught,
                 Set<MoveFlag> moveFlags) {
    this.number = number;
    this.first = first;
    this.queen = queen;
    this.white = white;
    this.moveFlags = moveFlags;

    this.movingDraught = movingDraught;
    this.movedDraught = movedDraught;
    this.takenDraught = takenDraught;
  }

  @GwtIncompatible
  public MoveDto(MoveDto move) {
    this(move.getNumber(), move.isFirst(), move.isQueen(), move.isWhite(),
        move.getMovingDraught(), move.getMovedDraught(),
        move.getTakenDraught(), move.getMoveFlags());
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

  public boolean isFixate() {
    return fixate;
  }

  public void setFixate(boolean fixate) {
    this.fixate = fixate;
  }

  public DraughtDto getTakenDraught() {
    return takenDraught;
  }

  public MoveDto setTakenDraught(DraughtDto takenDraught) {
    this.takenDraught = takenDraught;
    return this;
  }

  public DraughtDto getMovingDraught() {
    return movingDraught;
  }

  public MoveDto setMovingDraught(DraughtDto movingDraught) {
    this.movingDraught = movingDraught;
    return this;
  }

  public DraughtDto getMovedDraught() {
    return movedDraught;
  }

  public MoveDto setMovedDraught(DraughtDto movedDraught) {
    this.movedDraught = movedDraught;
    return this;
  }

  public Set<MoveFlag> getMoveFlags() {
    return moveFlags;
  }

  public MoveDto setMoveFlags(Set<MoveFlag> moveFlags) {
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
        ", movingDraught='" + movingDraught + '\'' +
        ", movedDraught='" + movedDraught + '\'' +
        ", takenDraught='" + takenDraught + '\'' +
        ", moveFlags=" + moveFlags +
        '}';
  }

  public int getMoveOrder() {
    return moveOrder;
  }

  public void setMoveOrder(int order) {
    this.moveOrder = order;
  }

  public void setScreenshot(String screenshot) {
    this.screenshot = screenshot;
  }

  public String getScreenshot() {
    return screenshot;
  }

  public boolean isQueen() {
    return queen;
  }

  public void setQueen(boolean queen) {
    this.queen = queen;
  }

  public boolean isWhite() {
    return white;
  }

  public void setWhite(boolean white) {
    this.white = white;
  }

  public enum MoveFlag {
    CANCEL_MOVE, // ход отменяется
    SIMPLE_MOVE, // ход без взятия
    CONTINUE_BEAT, // продолжить брать
    START_BEAT, // начало взятия
    STOP_BEAT // конец взятие
  }
}
