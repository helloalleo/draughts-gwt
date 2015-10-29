package online.draughts.rus.server.dao.impl;

import com.google.inject.TypeLiteral;
import com.google.inject.persist.Transactional;
import online.draughts.rus.server.dao.Dao;
import online.draughts.rus.shared.model.BasePersistableObject;
import online.draughts.rus.shared.model.Player;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 15.11.14
 * Time: 17:00
 */
public abstract class DaoImpl<E extends BasePersistableObject> implements Dao<E> {
  private Class<E> entityClass;

  public DaoImpl(TypeLiteral<E> type) {
    this.entityClass = (Class<E>) type.getRawType();
  }

  protected abstract EntityManager getEntityManager();

  @Transactional
  public void create(E entity) {
    getEntityManager().persist(entity);
  }

  @Transactional
  public E edit(E entity) {
    return getEntityManager().merge(entity);
  }

  @Transactional
  public void remove(E entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public boolean isExistingWithId(Long id) {
    if (id == null) {
      return false;
    }
    CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
    CriteriaQuery<Tuple> query = builder.createTupleQuery();
    Root<E> root = query.from(entityClass);
    Path<Long> idPath = root.get("id");
    query.multiselect(idPath);
    query.where(builder.equal(root.get("id"), id));
    List<Tuple> result = getEntityManager().createQuery(query).setMaxResults(1).getResultList();
    return !result.isEmpty();
  }


  @Transactional
  public E find(Object id) {
    if (id == null) {
      return null;
    }
    return getEntityManager().find(entityClass, id);
  }

  @Transactional
  public List<E> findAll() {
    CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
    cq.select(cq.from(entityClass));
    return getEntityManager().createQuery(cq).getResultList();
  }

  @Transactional
  public List<E> findPublishedAll() {
    CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
    CriteriaQuery<E> cq = cb.createQuery(entityClass);
    Root<E> root = cq.from(entityClass);
    cq.select(root).where(cb.equal(root.get("published"), true));
    return getEntityManager().createQuery(cq).getResultList();
  }

  @Transactional
  public List<E> findRange(int start, int length) {
    CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
    cq.select(cq.from(entityClass));
    Query q = getEntityManager().createQuery(cq);
    q.setMaxResults(length);
    q.setFirstResult(start);
    return q.getResultList();
  }

  @Transactional
  public Long countAll() {
    CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
    CriteriaQuery<Long> cq = qb.createQuery(Long.class);
    cq.select(qb.count(cq.from(entityClass)));
    cq.where(/*your stuff*/);
    return getEntityManager().createQuery(cq).getSingleResult();
  }

  protected Player findByParam(String entity, String[] params, Object[] values) throws IllegalArgumentException {
    if (StringUtils.isEmpty(entity)) {
      throw new IllegalArgumentException("Illegal entity parameter");
    }
    if (params.length != values.length) {
      throw new IllegalArgumentException("Length of params and values must be equal");
    }

    String hql = " FROM " + entity + " WHERE ";
    for (int i = 0; i < params.length; i++) {
      if (i != 0) {
        hql += " AND ";
      }
      hql += " " + params[i] + " = :" + params[i] + " ";
    }

    Query query = getEntityManager().createQuery(hql);
    for (int i = 0; i < params.length; i++) {
      query.setParameter(params[i], values[i]);
    }

    List result = query.getResultList();
    if (result.isEmpty()) {
      return null;
    }
    return (Player) result.get(0);
  }
}
