package online.shashki.rus.client.application.profile;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import online.shashki.rus.client.application.profile.settings.SettingsModule;

public class ProfileModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    install(new SettingsModule());

    bindPresenter(ProfilePresenter.class, ProfilePresenter.MyView.class, ProfileView.class, ProfilePresenter.MyProxy.class);
  }
}
