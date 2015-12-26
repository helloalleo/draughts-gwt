package online.draughts.rus.shared.dto;

import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 22.12.15
 * Time: 20:58
 */
public class DraughtDto extends BaseDto {

  private int row;
  private int col;
  private boolean white;
  private boolean queen;

  public DraughtDto() {
  }

  public DraughtDto(int row, int col, boolean white, boolean queen) {
    this.row = row;
    this.col = col;

    this.white = white;
    this.queen = queen;
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

  public boolean isWhite() {
    return white;
  }

  public void setWhite(boolean white) {
    this.white = white;
  }

  public boolean isQueen() {
    return queen;
  }

  public void setQueen(boolean queen) {
    this.queen = queen;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    DraughtDto that = (DraughtDto) o;
    return Objects.equals(row, that.row) &&
        Objects.equals(col, that.col) &&
        Objects.equals(white, that.white);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), row, col, white);
  }
}
