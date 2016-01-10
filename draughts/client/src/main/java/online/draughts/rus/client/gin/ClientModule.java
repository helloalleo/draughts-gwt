package online.draughts.rus.client.gin;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import online.draughts.rus.client.application.ApplicationModule;
import online.draughts.rus.client.application.security.CurrentSession;
import online.draughts.rus.client.application.widget.dialog.DialogModule;
import online.draughts.rus.client.application.widget.popup.DraughtsPlayerModule;
import online.draughts.rus.client.place.NameTokens;
import online.draughts.rus.client.util.Cookies;
import online.draughts.rus.client.util.DTCookiesImpl;
import online.draughts.rus.client.util.DTLogImpl;
import online.draughts.rus.client.util.Log;
import online.draughts.rus.client.channel.WebsocketModule;

public class ClientModule extends AbstractGinModule {
  @Override
  protected void configure() {
    install(new DefaultModule.Builder()
        .defaultPlace(NameTokens.homePage)
        .errorPlace(NameTokens.errorPage)
        .unauthorizedPlace(NameTokens.loginPage)
        .build());
    install(new DraughtsPlayerModule());
    install(new DispatchModule());
    install(new ApplicationModule());
    install(new WebsocketModule());
    install(new DialogModule());

    install(new GinFactoryModuleBuilder().build(NotationPanelFactory.class));

    bind(CurrentSession.class).in(Singleton.class);
    bind(Cookies.class).to(DTCookiesImpl.class).in(Singleton.class);
    bind(Log.class).to(DTLogImpl.class).in(Singleton.class);

    // Load and inject CSS resources
    bind(ResourceLoader.class).asEagerSingleton();
  }
}
