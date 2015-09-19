package online.shashki.rus.client.resources;

import com.google.gwt.core.client.GWT;

public class ResourceLoader {
  public static AppResources INSTANCE = GWT.create(AppResources.class);
  static {
    INSTANCE.normalize().ensureInjected();
    INSTANCE.style().ensureInjected();
  }
}
