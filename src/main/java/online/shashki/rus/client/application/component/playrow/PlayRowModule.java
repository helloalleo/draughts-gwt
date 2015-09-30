
package online.shashki.rus.client.application.component.playrow;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import online.shashki.rus.client.application.component.playshowpanel.PlayShowPanelPresenter;
import online.shashki.rus.client.application.component.playshowpanel.PlayShowPanelView;

public class PlayRowModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    bindPresenterWidgetFactory(PlayRowPresenter.Factory.class, PlayRowPresenter.FactoryImpl.class,
        PlayRowPresenter.ViewFactory.class, PlayRowView.FactoryImpl.class);
  }
}
