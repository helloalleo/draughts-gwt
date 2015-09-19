package online.shashki.rus.client.gin;

import com.gwtplatform.mvp.client.annotations.DefaultPlace;
import com.gwtplatform.mvp.client.annotations.ErrorPlace;
import com.gwtplatform.mvp.client.annotations.UnauthorizedPlace;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import online.shashki.rus.client.application.ApplicationModule;
import online.shashki.rus.client.application.ClientFactory;
import online.shashki.rus.client.application.ClientFactoryImpl;
import online.shashki.rus.client.place.NameTokens;
import online.shashki.rus.client.resources.ResourceLoader;
import online.shashki.rus.client.rpc.GameMessageRpcServiceAsync;
import online.shashki.rus.client.rpc.GameRpcServiceAsync;
import online.shashki.rus.client.rpc.ProfileRpcServiceAsync;

public class ClientModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    install(new DefaultModule.Builder().build());
    install(new ApplicationModule());

    bind(ResourceLoader.class).asEagerSingleton();
    bind(ClientFactory.class).to(ClientFactoryImpl.class).asEagerSingleton();
    bind(ProfileRpcServiceAsync.class).asEagerSingleton();
    bind(GameMessageRpcServiceAsync.class).asEagerSingleton();
    bind(GameRpcServiceAsync.class).asEagerSingleton();

    // DefaultPlaceManager Places
    bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.HOME);
    bindConstant().annotatedWith(ErrorPlace.class).to(NameTokens.HOME);
    bindConstant().annotatedWith(UnauthorizedPlace.class).to(NameTokens.HOME);
  }
}
