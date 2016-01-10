package online.draughts.rus.server.domain.command;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import online.draughts.rus.server.domain.BaseModel;
import online.draughts.rus.server.domain.Model;

import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.01.16
 * Time: 22:58
 */
public class ModelCommand<T> extends Command<T> {

  @Override
  public void setPropertyForObject(T resultObject, Field field, Object property) {
    try {
      Object value = BaseModel.class.cast(field.getType().newInstance()).find((Key) property);
      setProp(resultObject, field.getName(), value);
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void updateFieldOfDatastoreEntity(Entity entity, Field field, Object value) {
    super.updateFieldOfDatastoreEntity(entity, field, getKeyProperty((Model) value));
  }

  private <E extends Model> Key getKeyProperty(E value) {
    if (value == null || value.getId() == 0) {
      return null;
    }
    return KeyFactory.createKey(value.getClass().getSimpleName(), value.getId());
  }
}
