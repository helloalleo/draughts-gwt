package online.draughts.rus.client.application.learn;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class LearnModule extends AbstractPresenterModule {

  @Override
  protected void configure() {
    bindPresenter(LearnPresenter.class, LearnPresenter.MyView.class, LearnView.class, LearnPresenter.MyProxy.class);
  }
}
