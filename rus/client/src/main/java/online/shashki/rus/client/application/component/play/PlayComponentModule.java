package online.shashki.rus.client.application.component.play;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class PlayComponentModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    bindSingletonPresenterWidget(online.shashki.rus.client.application.component.play.PlayComponentPresenter.class, online.shashki.rus.client.application.component.play.PlayComponentPresenter.MyView.class,
        online.shashki.rus.client.application.component.play.PlayComponentView.class);
  }
}
