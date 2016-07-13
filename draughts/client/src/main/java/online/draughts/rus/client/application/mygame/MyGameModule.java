package online.draughts.rus.client.application.mygame;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class MyGameModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    bindPresenter(MyGamePresenter.class, MyGamePresenter.MyView.class, MyGameView.class, MyGamePresenter.MyProxy.class);
  }
}
