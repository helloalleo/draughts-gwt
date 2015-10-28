package online.draughts.rus.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import online.draughts.rus.client.resources.images.Images;
import online.draughts.rus.client.resources.sounds.Sounds;

public interface AppResources extends ClientBundle {
  Images images();

  Sounds sounds();

  @Source("css/normalize.gss")
  Normalize normalize();

  @Source({"css/variables.gss", "css/style.gss"})
  Style style();

  interface Normalize extends CssResource {
  }

  interface Style extends CssResource {
    String notationColumn();

    String playerSearch();

    String draughtsColumn();

    String playerList();

    String mainContainer();

    String alignHorizontal();

    String xLargeFont();

    String largeFont();

    String navbarScroll();

    String navbarTop();

    String logoTop();

    String logoScroll();

    String playItem();

    String colHeight();

    String colMiddle();

    String rowHeight();

    String inside();

    String dialogBox();

    String dialogCaptionInfo();

    String dialogCaptionError();

    String whoDidWin();

    String playEndDate();

    String playItemPlayerName();
  }
}
