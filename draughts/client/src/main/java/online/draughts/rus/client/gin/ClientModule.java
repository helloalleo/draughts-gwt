package online.draughts.rus.client.gin;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import com.gwtplatform.mvp.shared.proxy.RouteTokenFormatter;
import online.draughts.rus.client.application.ApplicationModule;
import online.draughts.rus.client.application.security.CurrentSession;
import online.draughts.rus.client.application.widget.dialog.DialogModule;
import online.draughts.rus.client.application.widget.popup.DraughtsPlayerModule;
import online.draughts.rus.client.channel.WebsocketModule;
import online.draughts.rus.client.place.NameTokens;
import online.draughts.rus.client.util.Cookies;
import online.draughts.rus.client.util.CookiesImpl;
import online.draughts.rus.client.util.LogImpl;
import online.draughts.rus.client.util.Log;

public class ClientModule extends AbstractGinModule {
  @Override
  protected void configure() {
    install(new DefaultModule.Builder()
        .tokenFormatter(RouteTokenFormatter.class)
        .defaultPlace(NameTokens.HOME_PAGE)
        .errorPlace(NameTokens.ERROR_PAGE)
        .unauthorizedPlace(NameTokens.LOGIN_PAGE)
        .build());
    install(new DraughtsPlayerModule());
    install(new DispatchModule());
    install(new ApplicationModule());
    install(new WebsocketModule());
    install(new DialogModule());

    install(new GinFactoryModuleBuilder().build(NotationPanelFactory.class));

    bind(CurrentSession.class).in(Singleton.class);
    bind(Cookies.class).to(CookiesImpl.class).in(Singleton.class);
    bind(Log.class).to(LogImpl.class).in(Singleton.class);

    // Load and inject CSS resources
    bind(ResourceLoader.class).asEagerSingleton();
  }
}
