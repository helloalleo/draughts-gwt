package online.draughts.rus.server.domain;

import com.google.appengine.api.datastore.*;
import online.draughts.rus.server.annotation.MapKey;
import online.draughts.rus.server.annotation.MapValue;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 09.12.15
 * Time: 18:33
 */
public abstract class BaseModelImpl<T extends BaseModel> implements BaseModel<T> {

  private Logger logger = Logger.getLogger(BaseModelImpl.class);

  private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  private Class<T> entityClass;
  private String entityName;

  private Entity entity;

  public BaseModelImpl(Class<T> entityClass) {
    this.entityClass = entityClass;
    this.entityName = entityClass.getSimpleName();
    this.entity = new Entity(entityClass.getSimpleName());
  }


  public String getEntityName() {
    return entityName;
  }

  protected DatastoreService getDatastore() {
    return datastore;
  }

  protected abstract void setId(long id);

  protected abstract long getId();

  private <E extends Model> Key getKeyProperty(E value) {
    if (value == null || value.getId() == 0) {
      return null;
    }
    return KeyFactory.createKey(entityClass.getSimpleName(), value.getId());
  }

  // ********* Datastore Methods ********* //

  private T getResultObject(Entity entity) {
    T resultObject;
    try {
      resultObject = entityClass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      logger.error(e.getMessage(), e);
      return null;
    }
    try {
      for (Field field : entityClass.getDeclaredFields()) {
        final String fieldName = field.getName();
        final String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        final Method resultObjectSetMethod;
        Object property = entity.getProperty(fieldName);
        if (property == null) {
          continue;
        }
        Object value = null;
        if (property instanceof Long && field.getType().equals(int.class)) {
          value = ((Long) property).intValue();
        } else if (field.getType().isEnum()) {
          value = getValueFromEnum(field, property);
        } else if (property instanceof Boolean && field.getType().equals(boolean.class)) {
          value = ((Boolean) property).booleanValue();
        } else if (field.getType().isInstance(new HashMap<>())) {
          value = getValueFromMap(field, property);
        } else {
          value = field.getType().cast(property);
        }
        resultObjectSetMethod = resultObject.getClass().getMethod(methodName, field.getType());
        resultObjectSetMethod.invoke(resultObject, value);
      }
      resultObject.getClass().getSuperclass().getDeclaredMethod("setId", long.class)
          .invoke(resultObject, entity.getKey().getId());
    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
      logger.error(e.getMessage(), e);
    }
    return resultObject;
  }

  private Object getValueFromEnum(Field field, Object property) {
    for (Object eConst : field.getType().getEnumConstants()) {
      if (eConst.toString().equals(property.toString())) {
        return eConst;
      }
    }
    return null;
  }

  @Override
  public void update() {
    if (0 != getId()) {
      Entity entity = new Entity(this.entity.getKind(), getId());
      for (String propKey : this.entity.getProperties().keySet()) {
        entity.setProperty(propKey, this.entity.getProperty(propKey));
      }
      this.entity = entity;
    }

    for (Field field : entityClass.getDeclaredFields()) {
      try {
        final String fieldName = field.getName();
        final String methodType = field.getType().equals(Boolean.class) || field.getType().equals(boolean.class)
            ? "is" : "get";
        final String methodName = methodType + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Object value = entityClass.getMethod(methodName).invoke(this);
        if (value == null) {
          continue;
        }
        if (value.getClass().isEnum()) {
          entity.setProperty(fieldName, ((Enum) value).name());
        } else if (value.getClass().isInstance(new HashMap<>())) {
          List<String> stringified = getStringifyMap((HashMap) value);
          entity.setProperty(fieldName, stringified);
        } else {
          entity.setProperty(fieldName, value);
        }
      } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
        logger.error(e.getMessage(), e);
      }
    }
    datastore.put(entity);
    setId(entity.getKey().getId());
  }

  private Map<Object, Object> getValueFromMap(Field field, Object value) {
    List<String> stringList = (List<String>) value;
    Map resultMap = new HashMap();
    Class mapKeyClass = field.getAnnotation(MapKey.class).value();
    Class mapValueClass = field.getAnnotation(MapValue.class).value();
    for (String s : stringList) {
      String[] keyValue = s.split(",");
      Object key = castByAnnotationType(mapKeyClass, keyValue[0]);
      Object val = castByAnnotationType(mapValueClass, keyValue[1]);
      resultMap.put(key, val);
    }
    return resultMap;
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

  private List<String> getStringifyMap(Map map) {
    List<String> resultList = new ArrayList<>(map.size());
    for (Object o : map.keySet()) {
      final Object value = map.get(o);
      if (null == value) {
        continue;
      }
      resultList.add(o.toString() + "," + value.toString());
    }
    return resultList;
  }

  protected T getSingleResultObject(PreparedQuery preparedQuery) {
    Entity entity = preparedQuery.asSingleEntity();
    if (null == entity) {
      return null;
    }
    return getResultObject(entity);
  }

  protected List<T> getListResult(PreparedQuery preparedQuery) {
    Iterable<Entity> entities = preparedQuery.asIterable();

    List<T> resultObjects = new ArrayList<>();
    for (Entity e : entities) {
      resultObjects.add(getResultObject(e));
    }
    return resultObjects;
  }

  public List<T> findAll() {
    Query query = new Query(getEntityName());
    PreparedQuery preparedQuery = datastore.prepare(query);
    return getListResult(preparedQuery);
  }

  public T find(long id) {
    if (0 == id) {
      return null;
    }
    Query query = new Query(getEntityName());
    query.setFilter(new Query.FilterPredicate(Entity.KEY_RESERVED_PROPERTY,
        Query.FilterOperator.EQUAL,
        KeyFactory.createKey(getEntityName(), id)));
    PreparedQuery preparedQuery = datastore.prepare(query);
    return getSingleResultObject(preparedQuery);
  }
}
