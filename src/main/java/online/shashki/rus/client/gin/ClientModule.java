package online.shashki.rus.client.gin;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import online.shashki.rus.client.application.ApplicationModule;
import online.shashki.rus.client.place.NameTokens;
import online.shashki.rus.client.resources.ResourceLoader;

public class ClientModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    install(new DefaultModule.Builder()
        .defaultPlace(NameTokens.homePage)
        .errorPlace(NameTokens.homePage)
        .unauthorizedPlace(NameTokens.homePage)
        .build());
    install(new ApplicationModule());

    bind(ResourceLoader.class).asEagerSingleton();
//    bind(ClientFactory.class).to(ClientFactoryImpl.class).asEagerSingleton();
//    bind(ProfileRpcServiceAsync.class).asEagerSingleton();
//    bind(GameMessageRpcServiceAsync.class).asEagerSingleton();
//    bind(GameRpcServiceAsync.class).asEagerSingleton();

    // DefaultPlaceManager Places
//    bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.HOME);
//    bindConstant().annotatedWith(ErrorPlace.class).to(NameTokens.ERROR);
//    bindConstant().annotatedWith(UnauthorizedPlace.class).to(NameTokens.UNAUTHORIZED);
  }
}
