package online.shashki.rus.client.application.widget.dialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.DialogBox;
import online.shashki.rus.shared.locale.ShashkiMessages;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 27.12.14
 * Time: 23:17
 */
public class BasicDialogBox extends DialogBox {
  protected static ShashkiMessages messages = GWT.create(ShashkiMessages.class);

  protected int WIDTH = 400;
  protected int HEIGHT = 60;

  public BasicDialogBox() {
    setAnimationEnabled(true);
    addHandler(new KeyPressHandler() {
      @Override
      public void onKeyPress(KeyPressEvent keyPressEvent) {
        if (keyPressEvent.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
          hide();
        }
      }
    }, KeyPressEvent.getType());
  }

  @Override
  protected void onPreviewNativeEvent(Event.NativePreviewEvent event) {
    super.onPreviewNativeEvent(event);
    switch (event.getTypeInt()) {
      case Event.ONKEYDOWN:
        if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
          hide();
        }
        break;
    }
  }
}
