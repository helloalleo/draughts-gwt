package online.shashki.rus.client.application.home;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import online.shashki.rus.client.application.component.atest.AtestModule;
import online.shashki.rus.client.application.component.play.PlayComponentModule;
import online.shashki.rus.client.application.component.ttest.TtestModule;

public class HomeModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    install(new PlayComponentModule());
    install(new AtestModule());
    install(new TtestModule());

    bindPresenter(HomePresenter.class, HomePresenter.MyView.class, HomeView.class, HomePresenter.MyProxy.class);
  }
}
