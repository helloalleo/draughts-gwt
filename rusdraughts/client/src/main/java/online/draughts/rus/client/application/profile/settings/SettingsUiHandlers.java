package online.draughts.rus.client.application.profile.settings;

import com.gwtplatform.mvp.client.UiHandlers;

interface SettingsUiHandlers extends UiHandlers {
  void submitNewPlayerName(String playerName);

  void subscribeOnNewsletter(Boolean value);
}
