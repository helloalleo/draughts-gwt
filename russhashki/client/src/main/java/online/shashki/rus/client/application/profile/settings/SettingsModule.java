package online.shashki.rus.client.application.profile.settings;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class SettingsModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    bindPresenterWidgetFactory(SettingsPresenter.Factory.class, SettingsPresenter.FactoryImpl.class,
        SettingsPresenter.ViewFactory.class, SettingsView.ViewFactoryImpl.class);
  }
}
