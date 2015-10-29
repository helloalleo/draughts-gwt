package online.draughts.rus.shared.config;

import com.google.gwt.i18n.client.ConstantsWithLookup;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 15.03.15
 * Time: 16:19
 */
public interface ClientConfiguration extends ConstantsWithLookup {

  @Key("player.websocket.url")
  String playerWebsocketUrl();

  String debug();

  String level();

  String site_url();

  String production();

  String initShowGamesPageSize();

  String incrementPlayShowSize();
}
