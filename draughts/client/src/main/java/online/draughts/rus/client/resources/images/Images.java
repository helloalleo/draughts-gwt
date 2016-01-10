package online.draughts.rus.client.resources.images;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Images extends ClientBundle {
  @Source("img/russia-flag.png")
  ImageResource russiaFlag();

  @Source("img/us-flag.png")
  ImageResource usFlag();

  @Source("img/BlackDraught.png")
  ImageResource blackDraught();

  @Source("img/WhiteDraught.png")
  ImageResource whiteDraught();

  @Source("img/BlackQueen.png")
  ImageResource blackQueen();

  @Source("img/WhiteQueen.png")
  ImageResource whiteQueen();

  @Source("img/logo.png")
  ImageResource logo();

  @Source("img/logo_new_year.png")
  ImageResource logoNewYear();

  @Source("img/online.png")
  ImageResource onlineIconImage();

  @Source("img/offline.png")
  ImageResource offlineIconImage();

  @Source("img/playing.png")
  ImageResource playingIconImage();

  @Source("img/black-win.png")
  ImageResource blackWin();

  @Source("img/white-win.png")
  ImageResource whiteWin();

  @Source("img/me.jpg")
  ImageResource me();
}
