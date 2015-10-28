package online.draughts.rus.client.resources.images;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Images extends ClientBundle {
  @Source("img/logo.png")
  ImageResource logo();

  @Source("img/online.png")
  ImageResource onlineIconImage();

  @Source("img/offline.png")
  ImageResource offlineIconImage();

  @Source("img/playing.png")
  ImageResource playingIconImage();

  @Source("img/busy.gif")
  ImageResource busyIconImage();

  @Source("img/loading.gif")
  ImageResource loadIconImage();

  @Source("img/black-win.png")
  ImageResource blackWin();

  @Source("img/white-win.png")
  ImageResource whiteWin();
}
