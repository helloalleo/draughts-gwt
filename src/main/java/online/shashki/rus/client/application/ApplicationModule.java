package online.shashki.rus.client.application;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import online.shashki.rus.client.application.component.play.PlayComponentModule;
import online.shashki.rus.client.application.home.HomeModule;

public class ApplicationModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    install(new HomeModule());
    install(new PlayComponentModule());

    bindPresenter(ApplicationPresenter.class, ApplicationPresenter.MyView.class, ApplicationView.class,
        ApplicationPresenter.MyProxy.class);
  }
}
