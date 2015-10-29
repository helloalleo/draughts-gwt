package online.draughts.rus.client.gin;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import online.draughts.rus.client.application.ApplicationModule;
import online.draughts.rus.client.application.component.playshowpanel.PlayShowPanelModule;
import online.draughts.rus.client.application.security.CurrentSession;
import online.draughts.rus.client.application.widget.dialog.DialogModule;
import online.draughts.rus.client.place.NameTokens;
import online.draughts.rus.client.websocket.WebsocketModule;

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
    install(new DialogModule());
    install(new PlayShowPanelModule());

    bind(CurrentSession.class).in(Singleton.class);

    // Load and inject CSS resources
    bind(ResourceLoader.class).asEagerSingleton();
  }
}
