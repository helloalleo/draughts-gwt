package online.draughts.rus.shared.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 05.12.15
 * Time: 19:08
 */
public class BaseDto implements Serializable {

  private Long id;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BaseDto baseDto = (BaseDto) o;
    return Objects.equals(id, baseDto.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
