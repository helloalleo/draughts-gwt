package online.shashki.rus.shared.config;

import com.google.gwt.i18n.client.ConstantsWithLookup;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 15.03.15
 * Time: 16:19
 */
public interface ShashkiConfiguration extends ConstantsWithLookup {

  String protocol();

  String hostname();

  String port();

  @Key("player.websocket.url")
  String playerWebsocketUrl();
}
