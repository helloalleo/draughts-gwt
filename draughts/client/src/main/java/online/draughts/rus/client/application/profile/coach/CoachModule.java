package online.draughts.rus.client.application.profile.coach;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class CoachModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    bindPresenterWidgetFactory(CoachPresenter.Factory.class, CoachPresenter.FactoryImpl.class,
        CoachPresenter.ViewFactory.class, CoachView.ViewFactoryImpl.class);
  }
}
