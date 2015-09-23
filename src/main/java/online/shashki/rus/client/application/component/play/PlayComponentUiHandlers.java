package online.shashki.rus.client.application.component.play;

import com.gwtplatform.mvp.client.UiHandlers;
import online.shashki.rus.shared.model.Shashist;

interface PlayComponentUiHandlers extends UiHandlers {
  void connectToServer(Shashist opponent);

  void refreshConnectionToServer();
}
