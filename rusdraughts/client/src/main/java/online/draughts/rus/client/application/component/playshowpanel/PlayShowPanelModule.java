package online.draughts.rus.client.application.component.playshowpanel;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import online.draughts.rus.client.gin.PlayShowPanelFactory;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 29.10.15
 * Time: 5:25
 */
public class PlayShowPanelModule extends AbstractGinModule {
  @Override
  protected void configure() {
    install(new GinFactoryModuleBuilder().build(PlayShowPanelFactory.class));
  }
}
