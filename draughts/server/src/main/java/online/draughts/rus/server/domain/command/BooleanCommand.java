package online.draughts.rus.server.domain.command;

import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 08.01.16
 * Time: 16:39
 */
public class BooleanCommand<T> extends Command<T> {

  @Override
  public void setPropertyForObject(T resultObject, Field field, Object property) {
    Object value = (Boolean) property;
    setProp(resultObject, field.getName(), value);
  }
}
