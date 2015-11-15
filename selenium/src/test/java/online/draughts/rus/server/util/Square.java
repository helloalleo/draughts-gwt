package online.draughts.rus.server.util;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 15.11.15
 * Time: 14:43
 */
public class Square {
  private static final int DESK_CELLS = 8;
  private int row;
  private int col;

  public Square(int row, int col) {
    this.row = row;
    this.col = col;
  }

  public Square mirror() {
    int col = DESK_CELLS - 1 - this.col;
    int row = DESK_CELLS - 1 - this.row;
    return new Square(row, col);
  }

  @Override
  public String toString() {
    return "Square{" +
        "row=" + row +
        ", col=" + col +
        '}';
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
}
