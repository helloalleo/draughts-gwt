package online.draughts.rus.server.domain.command;

import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.01.16
 * Time: 22:58
 */

/**
 * Устанавливает свойство field обеъекта resultObject в целое значение value
 * @param <T> тип объекта, чье поле модифицируем
 */
public class IntCommand<T> extends Command<T> {

  @Override
  public void setPropertyForObject(T resultObject, Field field, Object property) {
    Object value = ((Long) property).intValue();
    setProp(resultObject, field.getName(), value);
  }
}
