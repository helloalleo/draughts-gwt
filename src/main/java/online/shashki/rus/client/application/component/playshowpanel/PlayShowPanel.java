
package online.shashki.rus.client.application.component.playshowpanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import online.shashki.rus.shared.model.Game;

import java.util.List;

public class PlayShowPanel extends Composite {

  private static Binder binder = GWT.create(Binder.class);

  @UiField
  PlayRow playRow;
  private List<Game> gameList;

  PlayShowPanel() {
    initWidget(binder.createAndBindUi(this));
  }

  public void setGames(List<Game> gameList) {
    this.gameList = gameList;

    for (Game game : gameList) {

    }
  }

  interface Binder extends UiBinder<HTMLPanel, PlayShowPanel> {
  }
}
