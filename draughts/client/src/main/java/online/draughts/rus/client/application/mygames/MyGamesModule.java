package online.draughts.rus.client.application.mygames;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class MyGamesModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    bindPresenter(MyGamesPresenter.class, MyGamesPresenter.MyView.class, MyGamesView.class, MyGamesPresenter.MyProxy.class);
  }
}
