package online.draughts.rus.client.application.widget.popup;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class DraughtsPlayerModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    bindPresenterWidgetFactory(DraughtsPlayerPresenter.Factory.class, DraughtsPlayerPresenter.FactoryImpl.class,
        DraughtsPlayerPresenter.ViewFactory.class, DraughtsPlayerView.ViewFactoryImpl.class);
  }
}
