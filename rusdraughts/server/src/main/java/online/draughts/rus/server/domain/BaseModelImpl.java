package online.draughts.rus.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.AsyncMemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import online.draughts.rus.server.annotation.*;
import online.draughts.rus.server.annotation.Index;
import online.draughts.rus.server.annotation.Text;
import online.draughts.rus.server.util.Utils;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 09.12.15
 * Time: 18:33
 */
public abstract class BaseModelImpl<T extends BaseModel> implements BaseModel<T> {

  @JsonIgnore
  private Logger logger = Logger.getLogger(BaseModelImpl.class);

  @JsonIgnore
  private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  @JsonIgnore
  private AsyncMemcacheService memcache = MemcacheServiceFactory.getAsyncMemcacheService();
  @JsonIgnore
  private Class<T> entityClass;
  @JsonIgnore
  private String entityName;

  @JsonIgnore
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
    return KeyFactory.createKey(value.getClass().getSimpleName(), value.getId());
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
        } else if (Map.class.isAssignableFrom(field.getType())) {
          value = getValueFromMap(field, property);
        } else if (field.getType().isInstance(new HashSet<>())) {
          if (field.isAnnotationPresent(Enumerated.class)) {
            value = getValueFromSetOfEnum(field, property);
          } else if (field.isAnnotationPresent(KeySet.class)) {
            value = getValueFromSetOfModel(field, property);
          }
        } else if (property instanceof Key) {
          value = BaseModel.class.cast(field.getType().newInstance()).find((Key) property);
        } else if (property instanceof com.google.appengine.api.datastore.Text) {
          value = ((com.google.appengine.api.datastore.Text) property).getValue();
        } else {
          value = field.getType().cast(property);
        }
        resultObjectSetMethod = resultObject.getClass().getMethod(methodName, field.getType());
        resultObjectSetMethod.invoke(resultObject, value);
      }
      resultObject.getClass().getSuperclass().getDeclaredMethod("setId", long.class)
          .invoke(resultObject, entity.getKey().getId());
    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
      logger.error(e.getMessage(), e);
    }
    return resultObject;
  }

  private Object getValueFromSetOfModel(Field field, Object property) {
    if (!field.isAnnotationPresent(KeySet.class)) {
      throw new RuntimeException("Iterable field " + field.getName() + " of Model must be annotated by @KeySet");
    }
    Set<String> inputSet = new HashSet<>((List<String>) property);
    Set<Object> resultSet = new HashSet<>(inputSet.size());
    Class modelClass = field.getAnnotation(KeySet.class).value();
    for (String s : inputSet) {
      try {
        resultSet.add(((Model) modelClass.newInstance()).fromString(s));
      } catch (InstantiationException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return resultSet;
  }

  private Object getValueFromSetOfEnum(Field field, Object property) {
    if (!field.isAnnotationPresent(Enumerated.class)) {
      throw new RuntimeException("Iterable field " + field.getName() + " of Enum must be annotated by @Enumerated");
    }
    Set<String> inputSet = new HashSet<>((List<String>) property);
    Set<Enum> resultSet = new HashSet<>(inputSet.size());
    Class enumClass = field.getAnnotation(Enumerated.class).value();
    for (String s : inputSet) {
      resultSet.add(Enum.valueOf(enumClass, s));
    }
    return resultSet;
  }

  public T getByKey(Key property) {
    try {
      return getResultObject(datastore.get(property));
    } catch (EntityNotFoundException e) {
      return null;
    }
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
          updateProperty(entity, field, ((Enum) value).name());
        } else if (value instanceof Map) {
          List<String> stringified = getStringifiedMap((HashMap) value);
          updateProperty(entity, field, stringified);
        } else if (value instanceof Set && !((Set) value).isEmpty()
            && ((Set) value).iterator().next().getClass().isEnum()) {
          Set<String> stringified = getStringifiedSetOfEnum((Set) value);
          updateProperty(entity, field, stringified);
        } else if (value instanceof Set && !((Set) value).isEmpty()
            && ((Set) value).iterator().next() instanceof Model) {
          Set<String> stringified = getStringifiedSetOfModel((Set) value);
          updateProperty(entity, field, stringified);
        } else if (value instanceof Model) {
          updateProperty(entity, field, getKeyProperty((Model) value));
        } else {
          updateProperty(entity, field, value);
        }
      } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
        logger.error(e.getMessage(), e);
      }
    }
    datastore.put(entity);
    setId(entity.getKey().getId());
  }

  private Set<String> getStringifiedSetOfModel(Set value) {
    Set<String> resultSet = new HashSet<>(value.size());
    for (Object o : value) {
      resultSet.add(Model.class.cast(o).serializeToString());
    }
    return resultSet;
  }

  @Override
  public void flush() {
    memcache.clearAll();
  }

  private Set<String> getStringifiedSetOfEnum(Set value) {
    Set<String> resultSet = new HashSet<>(value.size());
    for (Object o : value) {
      resultSet.add(Enum.class.cast(o).name());
    }
    return resultSet;
  }

  private void updateProperty(Entity entity, Field field, Object value) {
    if (field.isAnnotationPresent(Transient.class)) {
      return;
    }

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

  private Map<Object, Object> getValueFromMap(Field field, Object value) {
    if (!(field.isAnnotationPresent(MapKey.class) && field.isAnnotationPresent(MapValue.class))) {
      throw new RuntimeException("Map must be annotated with @MapKey and @MapValue");
    }
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

  private List<String> getStringifiedMap(Map map) {
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

  protected List<T> getListResult(QueryResultList<Entity> results) {
    List<T> resultObjects = new ArrayList<>(results.size());
    for (Entity result : results) {
      resultObjects.add(getResultObject(result));
    }
    return resultObjects;
  }

  @Override
  public List<T> findAll() {
    Query query = new Query(getEntityName());
    PreparedQuery preparedQuery = datastore.prepare(query);
    return getListResult(preparedQuery);
  }

  @Override
  public T find(long id) {
    return find(KeyFactory.createKey(getEntityName(), id));
  }

  @Override
  public T find(Key key) {
    if (null == key) {
      return null;
    }
    Query query = new Query(getEntityName());
    query.setFilter(new Query.FilterPredicate(Entity.KEY_RESERVED_PROPERTY,
        Query.FilterOperator.EQUAL,
        key));
    PreparedQuery preparedQuery = datastore.prepare(query);
    return getSingleResultObject(preparedQuery);
  }

  public List<T> findRange(int offset, int limit) {
    if (offset < 0 || limit < 0) {
      return null;
    }
    FetchOptions fetchOptions = FetchOptions.Builder.withLimit(limit);
    fetchOptions.offset(offset);

    Query query = new Query(getEntityName());
    PreparedQuery preparedQuery = datastore.prepare(query);
    return getListResult(preparedQuery.asQueryResultList(fetchOptions));
  }

  @JsonIgnore
  protected <O extends BaseModel> String serializeToString(O obj) {
    return Utils.serializeToJson(obj);
  }

  @JsonIgnore
  protected <O extends BaseModel> O fromString(String json, Class<O> clazz) {
    return Utils.deserializeFromJson(json, clazz);
  }
}
