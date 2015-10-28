package online.shashki.rus.client.gin;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import online.shashki.rus.client.application.ApplicationModule;
import online.shashki.rus.client.application.security.CurrentSession;
import online.shashki.rus.client.place.NameTokens;
import online.shashki.rus.client.websocket.WebsocketModule;

public class ClientModule extends AbstractGinModule {
  @Override
  protected void configure() {
    install(new DefaultModule.Builder()
        .defaultPlace(NameTokens.homePage)
        .errorPlace(NameTokens.errorPage)
        .unauthorizedPlace(NameTokens.loginPage)
        .build());
    install(new DispatchModule());
    install(new ApplicationModule());
    install(new WebsocketModule());

    bind(CurrentSession.class).in(Singleton.class);

    // Load and inject CSS resources
    bind(ResourceLoader.class).asEagerSingleton();
  }
}
