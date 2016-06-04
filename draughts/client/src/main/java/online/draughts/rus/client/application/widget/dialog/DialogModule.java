package online.draughts.rus.client.application.widget.dialog;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import online.draughts.rus.client.gin.DialogFactory;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 29.10.15
 * Time: 4:57
 */
public class DialogModule extends AbstractGinModule {
  @Override
  protected void configure() {
    install(new GinFactoryModuleBuilder().build(DialogFactory.class));
  }
}
