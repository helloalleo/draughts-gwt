package online.draughts.rus.server.domain;

import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 15.11.14
 * Time: 16:03
 */
public class ModelImpl<T extends BaseModel> extends BaseModelImpl<T> implements Model<T> {

  private long id;

  public ModelImpl(Class<T> entityClass) {
    super(entityClass);
  }

  @Override
  public void setId(long id) {
    this.id = id;
  }

  @Override
  public long getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ModelImpl<?> model = (ModelImpl<?>) o;
    return Objects.equals(id, model.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String serializeToString() {
    return serializeToString(this);
  }

  @Override
  public T fromString(String s) {
    return (T) fromString(s, ModelImpl.class);
  }
}
