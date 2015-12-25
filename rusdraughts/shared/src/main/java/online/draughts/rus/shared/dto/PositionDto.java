package online.draughts.rus.shared.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 23.12.15
 * Time: 13:32
 */
public class PositionDto extends BaseDto {

  @JsonIgnore
  private static final String DELIMITER = ",";
  private int row;
  private int col;

  public PositionDto() {
  }

  public PositionDto(int row, int col) {
    this.row = row;
    this.col = col;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    PositionDto that = (PositionDto) o;
    return Objects.equals(row, that.row) &&
        Objects.equals(col, that.col);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), row, col);
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

  public static PositionDto fromString(String toSendStr) {
    final String[] toSendArr = toSendStr.split(DELIMITER);
    final Integer row = Integer.valueOf(toSendArr[0]);
    final Integer col = Integer.valueOf(toSendArr[1]);
    return new PositionDto(row, col);
  }
}
