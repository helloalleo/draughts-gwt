package online.shashki.rus.client.application.component.play;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class PlayComponentModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    bindPresenter(PlayComponentPresenter.class, PlayComponentPresenter.MyView.class, PlayComponentView.class,
        PlayComponentPresenter.MyProxy.class);
  }
}
