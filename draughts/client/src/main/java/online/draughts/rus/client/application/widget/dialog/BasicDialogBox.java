package online.draughts.rus.client.application.widget.dialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.web.bindery.event.shared.EventBus;
import online.draughts.rus.client.resources.AppResources;
import online.draughts.rus.client.util.Cookies;
import online.draughts.rus.shared.locale.DraughtsMessages;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 27.12.14
 * Time: 23:17
 */
public class BasicDialogBox extends DialogBox implements BaseDialogBox {
  private final EventBus eventBus;
  private final String header;
  private final String content;
  private DraughtsMessages messages;
  private AppResources resources;
  private Cookies cookies;

  int WIDTH = 400;
  int HEIGHT = 60;

  BasicDialogBox(DraughtsMessages messages, AppResources resources,
                 Cookies cookies, EventBus eventBus,
                 String header, String content) {
    this.messages = messages;
    this.resources = resources;
    this.cookies = cookies;
    this.eventBus = eventBus;
    this.header = header;
    this.content = content;
    setAnimationEnabled(true);

    getElement().addClassName(resources.style().dialogBox());
  }

  @Override
  protected void onPreviewNativeEvent(Event.NativePreviewEvent event) {
    super.onPreviewNativeEvent(event);
    switch (event.getTypeInt()) {
      case Event.ONKEYDOWN:
        if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE
            || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
          hide();
        }
        break;
    }
  }

  public EventBus getEventBus() {
    return eventBus;
  }

  public String getHeader() {
    return header;
  }

  public String getContent() {
    return content;
  }

  public DraughtsMessages getMessages() {
    return messages;
  }

  public AppResources getResources() {
    return resources;
  }

  public Cookies getCookies() {
    return cookies;
  }
}
