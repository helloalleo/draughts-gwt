package online.draughts.rus.server.domain.command;

import com.google.appengine.api.datastore.Entity;

import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.01.16
 * Time: 22:48
 */
public interface Invoker<T> {

  void setPropertyForObject(T resultObject, Field field, Object property);
  void updateFieldOfDatastoreEntity(Entity entity, Field field, Object value);
}
