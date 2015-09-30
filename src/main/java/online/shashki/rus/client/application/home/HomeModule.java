package online.shashki.rus.client.application.home;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import online.shashki.rus.client.application.component.play.PlayComponentModule;

public class HomeModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    install(new PlayComponentModule());

    bindPresenter(HomePresenter.class, HomePresenter.MyView.class, HomeView.class, HomePresenter.MyProxy.class);
  }
}
