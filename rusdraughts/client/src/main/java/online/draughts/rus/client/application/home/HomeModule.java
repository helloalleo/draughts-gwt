package online.draughts.rus.client.application.home;

import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import online.draughts.rus.client.application.home.messanger.MessangerModule;
import online.draughts.rus.client.gin.PlayShowPanelFactory;

public class HomeModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    install(new MessangerModule());
    install(new GinFactoryModuleBuilder().build(PlayShowPanelFactory.class));

    bindPresenter(HomePresenter.class, HomePresenter.MyView.class, HomeView.class, HomePresenter.MyProxy.class);
  }
}
