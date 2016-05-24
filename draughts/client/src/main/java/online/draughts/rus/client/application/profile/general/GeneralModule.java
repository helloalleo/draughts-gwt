
package online.draughts.rus.client.application.profile.general;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class GeneralModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    bindPresenterWidgetFactory(GeneralPresenter.Factory.class, GeneralPresenter.FactoryImpl.class,
        GeneralPresenter.ViewFactory.class, GeneralView.ViewFactoryImpl.class);
  }
}
