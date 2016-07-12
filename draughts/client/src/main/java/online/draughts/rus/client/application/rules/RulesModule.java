package online.draughts.rus.client.application.rules;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class RulesModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    bindPresenter(RulesPresenter.class, RulesPresenter.MyView.class, RulesView.class, RulesPresenter.MyProxy.class);
  }
}
