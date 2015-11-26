package online.draughts.rus.client.application.love;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class LoveModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    bindPresenter(LovePresenter.class, LovePresenter.MyView.class, LoveView.class, LovePresenter.MyProxy.class);
  }
}
