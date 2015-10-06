package online.shashki.rus.server.utils;

import com.google.inject.Singleton;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 15.11.14
 * Time: 17:34
 */
@Singleton
public class Resources {

  @PersistenceContext(unitName = "shashki64PU")
  private EntityManager entityManager;

//  @Produces
//  public EntityManager getEntityManager() {
//    return entityManager;
//  }

//  @Default
//  @Produces
//  public EventBus getEventBus() {
//    return new SimpleEventBus();
//  }

}
