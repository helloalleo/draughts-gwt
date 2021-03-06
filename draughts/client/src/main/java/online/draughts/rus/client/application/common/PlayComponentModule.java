package online.draughts.rus.client.application.common;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class PlayComponentModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    bindSingletonPresenterWidget(PlayComponentPresenter.class, PlayComponentPresenter.MyView.class,
        PlayComponentView.class);
  }
}
