package online.shashki.rus.shashki;

import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.shared.core.types.Color;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * Profile: alekspo
 * Date: 07.12.13
 * Time: 21:07
 */
public class Square implements Serializable {

  @JsonIgnore
  private Rectangle shape;
  @JsonIgnore
  private static final String TO_SEND_SEP = ",";
  private int row;
  private int col;
  @JsonIgnore
  private final Color boardBackground = new Color(65, 133, 132);
  private boolean occupied;
  @JsonIgnore
  private Draught occupant;
  @JsonIgnore
  private String[] alph = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};

  public Square(int row, int col) {
    this.shape = new Rectangle(0, 0);
    this.row = row;
    this.col = col;

    this.occupied = false;
    this.occupant = null;

    this.shape.setFillColor(boardBackground);
  }

  public Square() {
  }

  public static Square fromString(String toSendStr) {
    if (toSendStr == null || toSendStr.isEmpty()) {
      return null;
    }
    final String[] toSendArr = toSendStr.split(TO_SEND_SEP);
    final Integer row = Integer.valueOf(toSendArr[0]);
    final Integer col = Integer.valueOf(toSendArr[1]);
    return new Square(row, col);
  }

  public Rectangle getShape() {
    return shape;
  }

  public static boolean isValid(int row, int col) {
    return row % 2 != col % 2;
  }

  /**
   * Get the piece that occupies this Square
   *
   * @return The piece that occupies this Square, if any
   */
  public Draught getOccupant() {
    if (this.isOccupied()) {
      return occupant;
    }

    return null;
  }

  /**
   * Set the occupant of this Square
   *
   * @param visitor The Piece that should now reside here
   */
  public void setOccupant(Draught visitor) {
    if (visitor != null) {
      this.occupant = visitor;
      this.occupied = true;
    } else {
      this.occupant = null;
      this.occupied = false;
    }
  }

  /**
   * Return whether or not this Square is occupied
   *
   * @return Whether or not this Square is selected
   */
  public boolean isOccupied() {
    return occupied;
  }

  public String toString() {
    return "row=" + String.valueOf(row) + " col=" + String.valueOf(col)
        + "; x=" + String.valueOf(shape.getX()) + " y=" + String.valueOf(shape.getY());
  }

  public String toSend() {
    return String.valueOf(row) + TO_SEND_SEP + String.valueOf(col);
  }

  public String toNotation(boolean first) {
    if (first) {
      return alph[col] + String.valueOf(BoardBackgroundLayer.ROWS - row);
    }
    return alph[BoardBackgroundLayer.COLS - 1 - col] + String.valueOf(row + 1);
  }

  /**
   * Возвращает true если square на той же диагонале что и эта
   *
   * @param square
   * @return
   */
  public boolean isOnLine(Square square) {
    // равенство абсолютной велечины строки и колонки говорит что мы на одной диагонали
    if (Math.abs(this.getRow() - square.getRow()) == Math.abs(this.getCol() - square.getCol())) {
      // на главной диагонали
      if (this.getRow() - square.getRow() < 0 && this.getCol() - square.getCol() > 0
          || this.getRow() - square.getRow() > 0 && this.getCol() - square.getCol() < 0) {
        return true;
      }
      // на побочной (математической главной)
      if (this.getRow() - square.getRow() < 0 && this.getCol() - square.getCol() < 0
          || this.getRow() - square.getRow() > 0 && this.getCol() - square.getCol() > 0) {
        return true;
      }
    }
    return false;
  }

  /**
   * Get the row of the game board that this square represents
   *
   * @return The row on the game board represented by this Square
   */
  public int getRow() {
    return row;
  }

  /**
   * Get the column of the game board that this square represents
   *
   * @return The column on the game board represented by this Square
   */
  public int getCol() {
    return col;
  }

  public boolean isBetween(Square from, Square to) {
    return isBetween(this.getRow(), from.getRow(), to.getRow())
        && isBetween(this.getCol(), from.getCol(), to.getCol()) || isBetween(this.getRow(), from.getRow(), to.getRow())
        && isBetween(this.getCol(), to.getCol(), from.getCol()) || isBetween(this.getRow(), to.getRow(), from.getRow())
        && isBetween(this.getCol(), to.getCol(), from.getCol()) || isBetween(this.getRow(), to.getRow(), from.getRow())
        && isBetween(this.getCol(), from.getCol(), to.getCol());
  }

  private static boolean isBetween(int value, int min, int max) {
    return ((value > min) && (value < max));
  }

  public void updateShape(int side, int rows, int cols, double offsetX, int offsetY) {
    double x = ((double) col) * side / ((double) rows) + offsetX;
    double y = ((double) row) * side / ((double) cols) + offsetY;
    shape.setX(x);
    shape.setY(y);

    double squareSize = side / (double) rows;
    shape.setWidth(squareSize);
    shape.setHeight(squareSize);
//    this.setStrokeWidth(strokeLineWidth);
  }

  public boolean contains(double x, double y) {
    return (Math.abs(x - shape.getX()) < shape.getWidth())
        && (Math.abs(y - shape.getY()) < shape.getHeight());
  }

  public double getCenterX() {
    return shape.getX() + shape.getWidth() / 2;
  }

  public double getCenterY() {
    return shape.getY() + shape.getHeight() / 2;
  }

  public Square mirror() {
    int row = BoardBackgroundLayer.ROWS - 1 - this.row;
    int col = BoardBackgroundLayer.COLS - 1 - this.col;

    return new Square(row, col);
  }

  public void setAlpha(double alpha) {
    shape.setAlpha(alpha);
  }

  public double getAlpha() {
    return shape.getAlpha();
  }
}
