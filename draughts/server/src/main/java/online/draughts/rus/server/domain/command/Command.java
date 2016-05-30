package online.draughts.rus.server.domain.command;

import com.google.appengine.api.datastore.Entity;
import online.draughts.rus.server.annotation.Index;
import online.draughts.rus.server.annotation.Text;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.01.16
 * Time: 23:00
 */
public abstract class Command<T> implements Invoker<T> {

  protected Logger logger = Logger.getLogger(Command.class);

  protected void setProp(T resultObject, String fieldName, Object value) {
    try {
      Field field = resultObject.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(resultObject, value);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      logger.error(e.getMessage(), e);
    }
  }

  /**
   * обновляем поле сущности из датасторе
   * @param entity сущность
   * @param field поле
   * @param value значение
   */
  @Override
  public void updateFieldOfDatastoreEntity(Entity entity, Field field, Object value) {
    // текст не индексируется
    if (field.isAnnotationPresent(Index.class)) {
      entity.setIndexedProperty(field.getName(), value);
    } else {
      if (field.isAnnotationPresent(Text.class)) {
        entity.setProperty(field.getName(), new com.google.appengine.api.datastore.Text(value.toString()));
      } else {
        entity.setProperty(field.getName(), value);
      }
    }
  }
}
