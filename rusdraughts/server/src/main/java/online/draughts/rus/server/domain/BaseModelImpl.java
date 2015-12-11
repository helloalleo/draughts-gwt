package online.draughts.rus.server.domain;

import com.google.appengine.api.datastore.*;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
      }
      resultObject.getClass().getSuperclass().getDeclaredMethod("setId", long.class)
          .invoke(resultObject, entity.getKey().getId());
    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
      logger.error(e.getMessage(), e);
    }
    return resultObject;
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
    datastore.put(entity);
    setId(entity.getKey().getId());
  }

  protected T getSingleResultObject(PreparedQuery pq) {
    Entity entity = pq.asSingleEntity();
    if (null == entity) {
      return null;
    }
    return getResultObject(entity);
  }

  public List<T> findAll() {
    Query query = new Query(getEntityName());
    PreparedQuery preparedQuery = datastore.prepare(query);
    Iterable<Entity> entities = preparedQuery.asIterable();

    List<T> resultObjects = new ArrayList<>();
    for (Entity e : entities) {
      resultObjects.add(getResultObject(e));
    }
    return resultObjects;
  }
}
