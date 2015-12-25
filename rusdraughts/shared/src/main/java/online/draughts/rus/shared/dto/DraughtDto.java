package online.draughts.rus.shared.dto;

import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 22.12.15
 * Time: 20:58
 */
public class DraughtDto extends BaseDto {

  private PositionDto pos;
  private boolean white;
  private boolean queen;

  public DraughtDto() {
  }

  public DraughtDto(PositionDto pos, boolean white, boolean queen) {
    this.pos = pos;
    this.white = white;
    this.queen = queen;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    DraughtDto that = (DraughtDto) o;
    return Objects.equals(white, that.white) &&
        Objects.equals(queen, that.queen) &&
        Objects.equals(pos, that.pos);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), pos, white, queen);
  }

  public PositionDto getPos() {
    return pos;
  }

  public void setPos(PositionDto pos) {
    this.pos = pos;
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
}
