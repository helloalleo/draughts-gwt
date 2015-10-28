package online.shashki.rus.server.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 23.10.15
 * Time: 21:21
 */
public class DatabaseModule extends AbstractModule {

  @Override
  protected void configure() {
    JpaPersistModule jpaPersistModel = new JpaPersistModule("shashki64PU");
    install(jpaPersistModel);
    bind(JPAInitializer.class).asEagerSingleton();
  }

  @Singleton
  public static class JPAInitializer {

    @Inject
    public JPAInitializer(final PersistService service) {
      service.start();
    }
  }
}