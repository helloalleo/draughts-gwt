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

  E save(E entity);

  E find(Long id);

  List<E> findAll();

  List<E> findRange(int start, int length);

  Integer countAll();
}
