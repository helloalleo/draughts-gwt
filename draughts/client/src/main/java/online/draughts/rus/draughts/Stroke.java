package online.draughts.rus.draughts;

import online.draughts.rus.shared.dto.MoveDto;

import java.util.HashSet;
import java.util.Set;

import static online.draughts.rus.client.util.Utils.format;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.10.15
 * Time: 15:13
 */
public class Stroke {

  public static final String SIMPLE_MOVE_SEP = "-";
  public static final String BEAT_MOVE_SEP = ":";

  private Square takenSquare;
  private Square startSquare;
  private Square endSquare;

  private Set<MoveDto.MoveFlags> moveFlags = new HashSet<>();
  private boolean first;
  private int order;
  private int number;
  private String comment;
  private String title;
  private boolean queen;
  private boolean white;

  public Stroke() {
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

  public String toNotation() {
    final String s = startSquare.toNotation();
    final String e = endSquare.toNotation();
    return format(isSimple() ? s + SIMPLE_MOVE_SEP + e
        : s + BEAT_MOVE_SEP + e);
  }

  public String toNotationLastMove() {
    return endSquare.toNotation();
  }

  public int getOrder() {
    return order;
  }

  public Stroke setOrder(int order) {
    this.order = order;
    return this;
  }

  public boolean isCancel() {
    return moveFlags.contains(MoveDto.MoveFlags.CANCEL_MOVE);
  }

  public boolean isSimple() {
    return moveFlags.contains(MoveDto.MoveFlags.SIMPLE_MOVE);
  }

  public boolean isContinueBeat() {
    return moveFlags.contains(MoveDto.MoveFlags.CONTINUE_BEAT);
  }

  public boolean isStopBeat() {
    return moveFlags.contains(MoveDto.MoveFlags.STOP_BEAT);
  }

  public boolean isStartBeat() {
    return moveFlags.contains(MoveDto.MoveFlags.START_BEAT);
  }

  public void setOnCancelMove() {
    moveFlags.add(MoveDto.MoveFlags.CANCEL_MOVE);
  }

  public void setOnSimpleMove() {
    moveFlags.add(MoveDto.MoveFlags.SIMPLE_MOVE);
  }

  public void setOnContinueBeat() {
    moveFlags.add(MoveDto.MoveFlags.CONTINUE_BEAT);
  }

  public void setOnStopBeat() {
    moveFlags.add(MoveDto.MoveFlags.STOP_BEAT);
  }

  public void setOnStartBeat() {
    moveFlags.add(MoveDto.MoveFlags.START_BEAT);
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

  public Stroke setMoveFlags(Set<MoveDto.MoveFlags> moveFlags) {
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

  public Set<MoveDto.MoveFlags> getMoveFlags() {
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

  public Stroke setQueen(boolean queen) {
    this.queen = queen;
    return this;
  }

  public boolean isQueen() {
    return queen;
  }

  public Stroke setWhite(boolean white) {
    this.white = white;
    return this;
  }

  public boolean isWhite() {
    return white;
  }
}
