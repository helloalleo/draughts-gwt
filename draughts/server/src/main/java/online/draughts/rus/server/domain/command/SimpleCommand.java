package online.draughts.rus.server.domain.command;

import com.google.appengine.api.datastore.Entity;

import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.01.16
 * Time: 22:58
 */
public class SimpleCommand<T> extends Command<T> {

  @Override
  public void setPropertyForObject(T resultObject, Field field, Object property) {
    Object value = field.getType().cast(property);
    setProp(resultObject, field.getName(), value);
  }

  @Override
  public void updateFieldOfDatastoreEntity(Entity entity, Field field, Object value) {
    super.updateFieldOfDatastoreEntity(entity, field, value);
  }
}
