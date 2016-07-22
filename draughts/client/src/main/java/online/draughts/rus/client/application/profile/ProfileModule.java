package online.draughts.rus.client.application.profile;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import online.draughts.rus.client.application.profile.coach.CoachModule;
import online.draughts.rus.client.application.profile.general.GeneralModule;
import online.draughts.rus.client.application.profile.settings.SettingsModule;

public class ProfileModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    install(new CoachModule());
    install(new GeneralModule());
    install(new SettingsModule());

    bindPresenter(ProfilePresenter.class, ProfilePresenter.MyView.class, ProfileView.class, ProfilePresenter.MyProxy.class);
  }
}
