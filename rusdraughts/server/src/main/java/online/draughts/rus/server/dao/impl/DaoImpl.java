package online.draughts.rus.server.dao.impl;

import com.google.inject.TypeLiteral;
import com.google.inject.persist.Transactional;
import online.draughts.rus.server.dao.Dao;
import online.draughts.rus.server.domain.BasePersistableObject;

import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 15.11.14
 * Time: 17:00
 */
public abstract class DaoImpl<E extends BasePersistableObject> implements Dao<E> {
  private Class<E> entityClass;

  public DaoImpl(TypeLiteral<E> type) {
    //noinspection unchecked
    this.entityClass = (Class<E>) type.getRawType();
  }

  public Class<E> getEntityClass() {
    return entityClass;
  }

  public E save(E entity) {
    return ofy().load().key(ofy().save().entity(entity).now()).now();
  }

  public E find(Long id) {
    if (id == null) {
      return null;
    }
    return ofy().load().type(entityClass).id(id).now();
  }

  @Transactional
  public List<E> findAll() {
    return ofy().load().type(entityClass).list();
  }

  @Transactional
  public List<E> findRange(int start, int length) {
    return ofy().load().type(entityClass).offset(start).limit(length).list();
  }

  @Transactional
  public Integer countAll() {
    return ofy().load().type(entityClass).count();
  }
}
