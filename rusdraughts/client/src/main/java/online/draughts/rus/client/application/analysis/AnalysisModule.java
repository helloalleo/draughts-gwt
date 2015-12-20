package online.draughts.rus.client.application.analysis;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class AnalysisModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    bindPresenter(AnalysisPresenter.class, AnalysisPresenter.MyView.class, AnalysisView.class,
        AnalysisPresenter.MyProxy.class);
  }
}
