package online.shashki.rus.client.application.profile.mygames;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class MyGamesModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    bindPresenter(MyGamesPresenter.class, MyGamesPresenter.MyView.class, MyGamesView.class, MyGamesPresenter.MyProxy.class);
  }
}
