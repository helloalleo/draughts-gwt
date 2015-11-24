package online.draughts.rus.client.application.play;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class PlayModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    install(new PlayComponentModule());

    bindPresenter(PlayPresenter.class, PlayPresenter.MyView.class, PlayView.class, PlayPresenter.MyProxy.class);
  }
}
