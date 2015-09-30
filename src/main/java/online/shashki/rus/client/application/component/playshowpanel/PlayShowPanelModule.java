
package online.shashki.rus.client.application.component.playshowpanel;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import online.shashki.rus.client.application.component.playrow.PlayRowModule;

public class PlayShowPanelModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    bindPresenterWidget(PlayShowPanelPresenter.class, PlayShowPanelPresenter.MyView.class, PlayShowPanelView.class);
  }
}
