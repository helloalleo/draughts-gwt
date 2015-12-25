package online.draughts.rus.server.domain;

import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 23.12.15
 * Time: 13:43
 */
public class Position extends ModelImpl<Position> {

  private int row;
  private int col;

  public Position() {
    super(Position.class);
  }

  public Position(int row, int col) {
    super(Position.class);
    this.row = row;
    this.col = col;
  }

  public int getRow() {
    return row;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public int getCol() {
    return col;
  }

  public void setCol(int col) {
    this.col = col;
  }

  public String serializeToString() {
    return serializeToString(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Position position = (Position) o;
    return Objects.equals(row, position.row) &&
        Objects.equals(col, position.col);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), row, col);
  }

  public Position fromString(String json) {
    return fromString(json, Position.class);
  }
}
