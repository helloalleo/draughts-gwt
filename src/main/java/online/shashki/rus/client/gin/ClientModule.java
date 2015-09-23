package online.shashki.rus.client.gin;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import online.shashki.rus.client.application.ApplicationModule;
import online.shashki.rus.client.application.login.CurrentSession;
import online.shashki.rus.client.place.NameTokens;
import online.shashki.rus.client.resources.ResourceLoader;
import online.shashki.rus.client.rpc.ProfileRpcServiceAsync;

public class ClientModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    install(new DefaultModule.Builder()
        .defaultPlace(NameTokens.homePage)
        .errorPlace(NameTokens.errorPage)
        .unauthorizedPlace(NameTokens.loginPage)
        .build());
    install(new ApplicationModule());

    bind(ResourceLoader.class).asEagerSingleton();
    bind(CurrentSession.class).asEagerSingleton();
    bind(ProfileRpcServiceAsync.class).asEagerSingleton();
//    bind(GameMessageRpcServiceAsync.class).asEagerSingleton();
//    bind(GameRpcServiceAsync.class).asEagerSingleton();
  }
}
