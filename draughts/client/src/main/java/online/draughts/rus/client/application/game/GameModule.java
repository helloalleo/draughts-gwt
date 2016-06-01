package online.draughts.rus.client.application.game;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class GameModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    bindPresenter(GamePresenter.class, GamePresenter.MyView.class, GameView.class, GamePresenter.MyProxy.class);
  }
}
