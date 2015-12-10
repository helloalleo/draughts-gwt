//package online.draughts.rus.server.dao.impl;
//
//import com.google.inject.TypeLiteral;
//import online.draughts.rus.server.dao.Dao;
//import online.draughts.rus.server.domain.BaseModel;
//
///**
// * Created with IntelliJ IDEA.
// * User: alekspo
// * Date: 15.11.14
// * Time: 17:00
// */
//public abstract class DaoImpl<E extends BaseModel> implements Dao<E> {
//  private Class<E> entityClass;
//
//  public DaoImpl(TypeLiteral<E> type) {
//    //noinspection unchecked
//    this.entityClass = (Class<E>) type.getRawType();
//  }
//
//  public Class<E> getEntityClass() {
//    return entityClass;
//  }
//
//  public E save(E entity) {
//    entity.update();
//    return entity;
//  }
//}
