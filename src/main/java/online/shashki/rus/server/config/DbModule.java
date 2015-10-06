//package online.shashki.rus.server.config;
//
//import com.google.inject.AbstractModule;
//import com.google.inject.Provides;
//import com.google.inject.Singleton;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.Persistence;
//
///**
// * Created with IntelliJ IDEA.
// * User: alekspo
// * Date: 06.10.15
// * Time: 16:59
// */
//public class DbModule extends AbstractModule {
//
//  private static final ThreadLocal<EntityManager> ENTITY_MANAGER_CACHE = new ThreadLocal<>();
//
//  @Provides
//  @Singleton
//  public EntityManagerFactory provideEntityManagerFactory() {
//    return Persistence.createEntityManagerFactory("shashki64PU");
//  }
//
//  @Provides
//  public EntityManager provideEntityManager(EntityManagerFactory entityManagerFactory) {
//    EntityManager entityManager = ENTITY_MANAGER_CACHE.get();
//    if (entityManager == null) {
//      entityManager = entityManagerFactory.createEntityManager();
//      ENTITY_MANAGER_CACHE.set(entityManager);
//    }
//    return entityManager;
//  }
//
//  @Override
//  protected void configure() {
////    bind(PlayerDao.class).to(PlayerDaoImpl.class).in(Singleton.class);
////    bind(GameDao.class).to(GameDaoImpl.class).in(Singleton.class);
////    bind(GameMessageDao.class).to(GameMessageDaoImpl.class).in(Singleton.class);
//  }
//}