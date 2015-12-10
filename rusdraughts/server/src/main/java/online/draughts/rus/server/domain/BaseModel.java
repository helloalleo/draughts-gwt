package online.draughts.rus.server.domain;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 23.10.15
 * Time: 19:21
 */
public interface BaseModel<T> extends Serializable {

  void update();
}
