package online.draughts.rus.client.application.play.messanger;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class MessengerModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    bindPresenterWidgetFactory(MessengerPresenter.Factory.class, MessengerPresenter.FactoryImpl.class,
        MessengerPresenter.ViewFactory.class, MessengerView.ViewFactoryImpl.class);
  }
}
