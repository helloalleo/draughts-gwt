package online.draughts.rus.client.application.home;


import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import online.draughts.rus.client.application.play.messanger.MessengerModule;
import online.draughts.rus.client.gin.PlayShowPanelFactory;

public class NomeModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    bindPresenter(NomePresenterPresenter.class, NomePresenterPresenter.MyView.class, NomePresenterView.class, NomePresenterPresenter.MyProxy.class);
  }
}
