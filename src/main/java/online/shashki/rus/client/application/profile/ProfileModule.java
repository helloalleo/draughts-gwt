package online.shashki.rus.client.application.profile;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import online.shashki.rus.client.application.profile.mygames.MyGamesModule;
import online.shashki.rus.client.application.profile.settings.SettingsModule;

public class ProfileModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    install(new SettingsModule());
    install(new MyGamesModule());

    bindPresenter(ProfilePresenter.class, ProfilePresenter.MyView.class, ProfileView.class, ProfilePresenter.MyProxy.class);
  }
}
