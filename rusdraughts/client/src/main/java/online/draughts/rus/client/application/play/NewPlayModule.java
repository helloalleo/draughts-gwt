package online.draughts.rus.client.application.play;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class NewPlayModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    install(new PlayComponentModule());

    bindPresenter(NewPlayPresenter.class, NewPlayPresenter.MyView.class, NewPlayView.class, NewPlayPresenter.MyProxy.class);
  }
}
