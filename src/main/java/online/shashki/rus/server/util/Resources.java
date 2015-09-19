package online.shashki.rus.server.util;

import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

import javax.ejb.Singleton;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
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

  @Produces
  public EntityManager getEntityManager() {
    return entityManager;
  }

  @Default
  @Produces
  public EventBus getEventBus() {
    return new SimpleEventBus();
  }

}
