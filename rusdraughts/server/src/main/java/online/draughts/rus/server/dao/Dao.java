package online.draughts.rus.server.dao;

import online.draughts.rus.shared.model.BasePersistableObject;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 15.11.14
 * Time: 16:58
 */
public interface Dao<E extends BasePersistableObject> {
  void create(E entity);

  E edit(E entity);

  void remove(E entity);

  E find(Object id);

  List<E> findAll();

  List<E> findPublishedAll();

  List<E> findRange(int start, int length);

  Long countAll();
}
