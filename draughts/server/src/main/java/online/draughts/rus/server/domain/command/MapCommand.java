package online.draughts.rus.server.domain.command;

import com.google.appengine.api.datastore.Entity;
import online.draughts.rus.server.annotation.MapKey;
import online.draughts.rus.server.annotation.MapValue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 08.01.16
 * Time: 15:56
 */
public class MapCommand<T> extends Command<T> {

  /**
   * Функция задания свойства property полю field объекта resultObject
   * @param resultObject модифицируемый объект
   * @param field поле модификации
   * @param property свойство - список строк
   */
  @Override
  public void setPropertyForObject(T resultObject, Field field, Object property) {
    if (!(field.isAnnotationPresent(MapKey.class) && field.isAnnotationPresent(MapValue.class))) {
      throw new RuntimeException("Map must be annotated with @MapKey and @MapValue");
    }
    List<String> stringList = (List<String>) property;
    Map value = new HashMap();
    Class mapKeyClass = field.getAnnotation(MapKey.class).value();
    Class mapValueClass = field.getAnnotation(MapValue.class).value();
    for (String s : stringList) {
      String[] keyValue = s.split(",");
      Object key = castByAnnotationType(mapKeyClass, keyValue[0]);
      Object val = castByAnnotationType(mapValueClass, keyValue[1]);
      value.put(key, val);
    }
    setProp(resultObject, field.getName(), value);
  }

  private Object castByAnnotationType(Class mapKeyClass, String keyValue) {
    if (mapKeyClass.isInstance((long) 0)) {
      return Long.valueOf(keyValue);
    } else if (mapKeyClass.isInstance((int) 0)) {
      return Integer.valueOf(keyValue);
    } else {
      return mapKeyClass.cast(keyValue);
    }
  }

  @Override
  public void updateFieldOfDatastoreEntity(Entity entity, Field field, Object value) {
    HashMap map = (HashMap) value;
    List<String> resultList = new ArrayList<>(map.size());
    for (Object o : map.keySet()) {
      final Object val = map.get(o);
      if (null == val) {
        continue;
      }
      resultList.add(o.toString() + "," + val.toString());
    }
    super.updateFieldOfDatastoreEntity(entity, field, resultList);
  }
}
