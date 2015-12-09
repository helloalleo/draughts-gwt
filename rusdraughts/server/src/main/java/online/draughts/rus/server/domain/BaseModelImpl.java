package online.draughts.rus.server.domain;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 09.12.15
 * Time: 18:33
 */
public abstract class BaseModelImpl implements BaseModel {

  @Inject
  private DatastoreService datastore;

  private final Class entityClass;

  private Entity entity;

  public BaseModelImpl(Class entityClass) {
    this.entityClass = entityClass;
    entity = new Entity(entityClass.getSimpleName());
  }

  public void update() {
    datastore.put(entity);
    setId(entity.getKey().getId());
  }

  protected abstract void setId(long id);

  protected Entity getEntiy() {
    return entity;
  }

  public <T extends Model> void setProperty(String name, T value) {
    setProperty(name, value, false);
  }
  public <T extends Model> void setIndexedProperty(String name, T value) {
    setProperty(name, value, true);
  }

  private <T extends Model> void setProperty(String name, T value, boolean indexed) {
    if (value == null) {
      return;
    }
    Key key = KeyFactory.createKey(entityClass.getSimpleName(), value.getId());
    if (indexed) {
      entity.setIndexedProperty(name, key);
    } else {
      entity.setProperty(name, key);
    }
  }
}
