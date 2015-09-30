
package online.shashki.rus.client.application.component.playshowpanel;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class PlayShowPanelModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    bindPresenterWidgetFactory(PlayShowPanelPresenter.Factory.class, PlayShowPanelPresenter.FactoryImpl.class,
        PlayShowPanelPresenter.ViewFactory.class, PlayShowPanelView.FactoryImpl.class);
  }
}
