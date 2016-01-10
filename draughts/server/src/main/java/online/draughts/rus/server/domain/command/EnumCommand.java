package online.draughts.rus.server.domain.command;

import com.google.appengine.api.datastore.Entity;

import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.01.16
 * Time: 22:58
 */
public class EnumCommand<T> extends Command<T> {

  @Override
  public void setPropertyForObject(T resultObject, Field field, Object property) {
    Object value = null;
    for (Object eConst : field.getType().getEnumConstants()) {
      if (eConst.toString().equals(property.toString())) {
        value = eConst;
        break;
      }
    }
    setProp(resultObject, field.getName(), value);
  }

  @Override
  public void updateFieldOfDatastoreEntity(Entity entity, Field field, Object value) {
    super.updateFieldOfDatastoreEntity(entity, field, ((Enum) value).name());
  }
}
