package online.draughts.rus.server.domain.command;

import com.google.appengine.api.datastore.Entity;
import online.draughts.rus.server.annotation.Transient;

import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 08.01.16
 * Time: 17:54
 */
public class Commander<T> {

  private Command<T> command;

  public Commander(Command<T> command) {
    this.command = command;
  }

  public void setProperty(T resultObject, Field field, Object property) {
    if (field.isAnnotationPresent(Transient.class)) {
      return;
    }
    command.setPropertyForObject(resultObject, field, property);
  }

  public void updateField(Entity entity, Field field, Object value) {
    if (field.isAnnotationPresent(Transient.class)) {
      return;
    }
    command.updateFieldOfDatastoreEntity(entity, field, value);
  }
}
