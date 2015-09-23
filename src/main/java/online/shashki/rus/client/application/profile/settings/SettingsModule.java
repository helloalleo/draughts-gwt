package online.shashki.rus.client.application.profile.settings;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class SettingsModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    bindPresenter(SettingsPresenter.class, SettingsPresenter.MyView.class, SettingsView.class, SettingsPresenter.MyProxy.class);
  }
}
