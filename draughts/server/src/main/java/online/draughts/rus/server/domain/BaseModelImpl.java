package online.draughts.rus.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.AsyncMemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import online.draughts.rus.server.annotation.Transient;
import online.draughts.rus.server.domain.command.*;
import online.draughts.rus.server.util.Utils;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 09.12.15
 * Time: 18:33
 */
abstract class BaseModelImpl<T extends BaseModel> implements BaseModel<T> {

  @Transient
  private transient Logger logger = Logger.getLogger(BaseModelImpl.class);

  @Transient
  private transient DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  @Transient
  private transient AsyncMemcacheService memcache = MemcacheServiceFactory.getAsyncMemcacheService();
  @Transient
  private transient Class<T> entityClass;
  @Transient
  private transient String entityName;

  @Transient
  private transient Entity entity;

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

  // ********* Datastore Methods ********* //

  /**
   * Преобразуем GAE объект Entity в объект Java типа T
   *
   * @param entity сущность хранящаяся в datastore
   * @return Java объект типа entityClass, заполненный значениями из entity
   */
  private T getResultObject(Entity entity) {
    T resultObject;
    try {
      resultObject = entityClass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      logger.error(e.getMessage(), e);
      return null;
    }
    Commander<T> commander;
    // итерируем по поляем модели типа entityClass
    List<Field> fields = getAllFields(new LinkedList<Field>(), entityClass);
    for (Field field : fields) {
      final String fieldName = field.getName();
      // получаем свойство из хранилища
      Object property = entity.getProperty(fieldName);
      if (property == null) {
        continue;
      }
      // создаем команду
      Command<T> command;
      if (property instanceof Long && field.getType().equals(int.class)) {
        // если свойство целого типа и типа длинного типа
        command = new IntCommand<>();
      } else if ((property instanceof Long || property instanceof Double) && field.getType().equals(double.class)) {
        command = new DoubleCommand<>();
      } else if (field.getType().isEnum()) {
        // если свойство типа перечисления
        command = new EnumCommand<>();
      } else if (property instanceof Boolean && field.getType().equals(boolean.class)) {
        // если свойство булевого типа
        command = new BooleanCommand<>();
      } else if (Map.class.isAssignableFrom(field.getType())) {
        // если свойство отображение
        command = new MapCommand<>();
      } else if (field.getType().isInstance(new HashSet<>())) {
        // если свойство множество
        command = new SetCommand<>();
      } else if (property instanceof Key) {
        // если свойство ключ GAE
        command = new ModelCommand<>();
      } else if (property instanceof com.google.appengine.api.datastore.Text) {
        // если свойство текст
        command = new TextCommand<>();
      } else {
        // если свойство остальных типов
        command = new SimpleCommand<>();
      }
      // выполняем команду присваивания свойства property объекту resultObject в поле field
      commander = new Commander<>(command);
      commander.setProperty(resultObject, field, property);
    }

    try {
      resultObject.getClass().getSuperclass().getDeclaredMethod("setId", long.class)
          .invoke(resultObject, entity.getKey().getId());
    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      logger.error(e.getMessage(), e);
    }
    // возвращаем заполненный объект
    return resultObject;
  }

  public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
    fields.addAll(Arrays.asList(type.getDeclaredFields()));

    if (type.getSuperclass() != null) {
      fields = getAllFields(fields, type.getSuperclass());
    }

    return fields;
  }

  /**
   * метод обновления сущности из хранилища entity полями текущего объекта
   */
  @Override
  public void update() {
    // если обновляем уже сохранённую сущность, тогда пересоздаем сущность с id объекта,
    // потому что id сущности задать нельзя
    if (0 != getId()) {
      this.entity = new Entity(this.entity.getKind(), getId());
    }

    // итерируем по всем полям текущего класса
    Commander<T> commander;
    for (Field field : entityClass.getDeclaredFields()) {
      final String fieldName = field.getName();
      final String methodType = field.getType().equals(Boolean.class) || field.getType().equals(boolean.class)
          ? "is" : "get";
      final String methodName = methodType + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
      Object value = null;
      try {
        value = entityClass.getMethod(methodName).invoke(this);
      } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        e.printStackTrace();
      }
      if (value == null) {
        continue;
      }
      Command<T> command;
      if (value.getClass().isEnum()) {
        // если значение перечисление
        command = new EnumCommand<>();
      } else if (value instanceof Map) {
        // если значение отображение
        command = new MapCommand<>();
      } else if (value instanceof Set) {
        // если значение множество
        command = new SetCommand<>();
      } else if (value instanceof Model) {
        // если значение другая модель
        command = new ModelCommand<>();
      } else {
        // если другой тип значение
        command = new SimpleCommand<>();
      }
      commander = new Commander<>(command);
      // обновляем поле field сущности entity значением value
      commander.updateField(entity, field, value);
    }
    // сохраняем сущность
    datastore.put(entity);
    // устанавливаем id текущего объекта id сохраненной сущности
    setId(entity.getKey().getId());
  }

  @Override
  public void flush() {
    memcache.clearAll();
  }

  T getSingleResultObject(PreparedQuery preparedQuery) {
    Entity entity = preparedQuery.asSingleEntity();
    if (null == entity) {
      return null;
    }
    return getResultObject(entity);
  }

  List<T> getListResult(PreparedQuery preparedQuery) {
    Iterable<Entity> entities = preparedQuery.asIterable();

    List<T> resultObjects = new ArrayList<>();
    for (Entity e : entities) {
      resultObjects.add(getResultObject(e));
    }
    return resultObjects;
  }

  List<T> getListResult(QueryResultList<Entity> results) {
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
  <O extends BaseModel> String serializeToString(O obj) {
    return Utils.serializeToJson(obj);
  }

  @JsonIgnore
  <O extends BaseModel> O fromString(String json, Class<O> clazz) {
    return Utils.deserializeFromJson(json, clazz);
  }
}
