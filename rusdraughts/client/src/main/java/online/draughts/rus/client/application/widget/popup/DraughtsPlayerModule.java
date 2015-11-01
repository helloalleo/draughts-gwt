package online.draughts.rus.client.application.widget.popup;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import online.draughts.rus.client.application.widget.popup.a.aModule;

public class DraughtsPlayerModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    install(new aModule());

    bindPresenterWidgetFactory(DraughtsPlayerPresenter.Factory.class, DraughtsPlayerPresenter.FactoryImpl.class,
        DraughtsPlayerPresenter.ViewFactory.class, DraughtsPlayerView.ViewFactoryImpl.class);
  }
}
