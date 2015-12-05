package online.draughts.rus.shared.dto;

import java.io.Serializable;

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
}
