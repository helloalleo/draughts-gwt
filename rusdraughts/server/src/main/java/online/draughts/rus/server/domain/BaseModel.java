package online.draughts.rus.server.domain;

import com.google.appengine.api.datastore.Key;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 23.10.15
 * Time: 19:21
 */
public interface BaseModel<T> extends Serializable {

  void update();

  void flush();

  List<T> findAll();

  T find(long id);

  T find(Key key);
}
