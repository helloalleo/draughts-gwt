package online.draughts.rus.server.domain;

import com.google.appengine.api.datastore.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 09.12.15
 * Time: 18:33
 */
public abstract class BaseModelImpl<T> implements BaseModel<T> {

  private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  private Class<T> entityClass;
  private String entityName;

  private Entity entity;

  public BaseModelImpl(Class<T> entityClass) {
    this.entityClass = entityClass;
    this.entityName = entityClass.getSimpleName();
    this.entity = new Entity(entityClass.getSimpleName());
  }

  public void update() {
    datastore.put(entity);
    setId(entity.getKey().getId());
  }

  protected abstract void setId(long id);

  protected Entity getEntiy() {
    return entity;
  }

  public <E extends Model> void setProperty(String name, E value) {
    setProperty(name, value, false);
  }

  public <E extends Model> void setIndexedProperty(String name, E value) {
    setProperty(name, value, true);
  }

  private <E extends Model> void setProperty(String name, E value, boolean indexed) {
    if (value == null || value.getId() == 0) {
      return;
    }
    Key key = KeyFactory.createKey(entityClass.getSimpleName(), value.getId());
    if (indexed) {
      entity.setIndexedProperty(name, key);
    } else {
      entity.setProperty(name, key);
    }
  }

  protected T getSingleResultObject(PreparedQuery pq) throws IllegalAccessException, InstantiationException {
    for (Entity entity1 : pq.asIterable()) {
      System.out.println(entity1);
    }
    Entity entity = pq.asSingleEntity();
    if (null == entity) {
      return null;
    }
    T resultObject = entityClass.newInstance();
    for (Field field : entityClass.getDeclaredFields()) {
      final String fieldName = field.getName();
      final String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
      final Method resultObjectSetMethod;
      try {
        Object property = entity.getProperty(fieldName);
        if (property == null) {
          continue;
        }
        Object value = null;
        System.out.println(field);
        if (property instanceof Long && field.getType().equals(int.class)) {
          value = ((Long) property).intValue();
        } else if (field.getType().isEnum()) {
          System.out.println(property);
          for (Object eConst : field.getType().getEnumConstants()) {
            if (eConst.toString().equals(property.toString())) {
              value = eConst;
              break;
            }
          }
        } else if (property instanceof Boolean && field.getType().equals(boolean.class)) {
          value = ((Boolean) property).booleanValue();
        } else {
          value = field.getType().cast(property);
        }
        resultObjectSetMethod = resultObject.getClass().getMethod(methodName, field.getType());
        resultObjectSetMethod.invoke(resultObject, value);
      } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return resultObject;
  }

  public String getEntityName() {
    return entityName;
  }

  protected DatastoreService getDatastore() {
    return datastore;
  }
}
