package online.draughts.rus.server.util;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 15.11.15
 * Time: 14:43
 */
public class Stroke {

  public static final String SIMPLE_MOVE_SEP = "-";
  public static final String BEAT_MOVE_SEP = ":";

  private Square takenSquare;
  private Square startSquare;
  private Square endSquare;

  private Set<Move.MoveFlags> moveFlags = new HashSet<>();
  private boolean first;
  private int order;
  private int number;
  private String comment;
  private String title;

  public Stroke() {
  }

  /**
   * Отражает ход на доске не изменяя сам объект
   *
   * @return moveDto отраженный объект
   */
  public Stroke flip() {
    Stroke stroke = new Stroke();

    stroke.setStartSquare(startSquare.mirror());
    stroke.setEndSquare(endSquare.mirror());
    if (takenSquare != null) {
      stroke.setTakenSquare(takenSquare.mirror());
    }
    stroke.setMoveFlags(moveFlags);
    stroke.setFirst(first);
    stroke.setNumber(number);
    return stroke;
  }

  public Square getTakenSquare() {
    return takenSquare;
  }

  public Stroke setTakenSquare(Square takenSquare) {
    this.takenSquare = takenSquare;
    return this;
  }

  public Square getStartSquare() {
    return startSquare;
  }

  public Stroke setStartSquare(Square startSquare) {
    this.startSquare = startSquare;
    return this;
  }

  public Square getEndSquare() {
    return endSquare;
  }

  public Stroke setEndSquare(Square endSquare) {
    this.endSquare = endSquare;
    return this;
  }

  public int getOrder() {
    return order;
  }

  public Stroke setOrder(int order) {
    this.order = order;
    return this;
  }

  public boolean isCancel() {
    return moveFlags.contains(Move.MoveFlags.CANCEL_MOVE);
  }

  public boolean isSimple() {
    return moveFlags.contains(Move.MoveFlags.SIMPLE_MOVE);
  }

  public boolean isContinueBeat() {
    return moveFlags.contains(Move.MoveFlags.CONTINUE_BEAT);
  }

  public boolean isStopBeat() {
    return moveFlags.contains(Move.MoveFlags.STOP_BEAT);
  }

  public boolean isStartBeat() {
    return moveFlags.contains(Move.MoveFlags.START_BEAT);
  }

  public void setOnCancelMove() {
    moveFlags.add(Move.MoveFlags.CANCEL_MOVE);
  }

  public void setOnSimpleMove() {
    moveFlags.add(Move.MoveFlags.SIMPLE_MOVE);
  }

  public void setOnContinueBeat() {
    moveFlags.add(Move.MoveFlags.CONTINUE_BEAT);
  }

  public void setOnStopBeat() {
    moveFlags.add(Move.MoveFlags.STOP_BEAT);
  }

  public void setOnStartBeat() {
    moveFlags.add(Move.MoveFlags.START_BEAT);
  }

  public Stroke setMoveFlags(Set<Move.MoveFlags> moveFlags) {
    this.moveFlags = moveFlags;
    return this;
  }

  public Stroke setFirst(boolean first) {
    this.first = first;
    return this;
  }

  public Stroke setNumber(int number) {
    this.number = number;
    return this;
  }

  public int getNumber() {
    return number;
  }

  public boolean isFirst() {
    return first;
  }

  public Set<Move.MoveFlags> getMoveFlags() {
    return moveFlags;
  }

  @Override
  public String toString() {
    return "Stroke{" +
        "takenSquare=" + takenSquare +
        ", startSquare=" + startSquare +
        ", endSquare=" + endSquare +
        ", moveFlags=" + moveFlags +
        ", first=" + first +
        ", number=" + number +
        '}';
  }

  public Stroke setComment(String comment) {
    this.comment = comment;
    return this;
  }

  public String getComment() {
    return comment;
  }

  public Stroke setTitle(String title) {
    this.title = title;
    return this;
  }

  public String getTitle() {
    return title;
  }
}
