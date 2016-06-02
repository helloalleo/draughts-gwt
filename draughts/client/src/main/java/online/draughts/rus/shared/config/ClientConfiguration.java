package online.draughts.rus.shared.config;

import com.google.gwt.i18n.client.LocalizableResource;
import com.google.gwt.i18n.client.Messages;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 15.03.15
 * Time: 16:19
 */
public interface ClientConfiguration extends Messages {

  @LocalizableResource.Key("log.level")
  String logLevel();

  String initShowGamesPageSize();

  String incrementPlayShowSize();

  String strokeCommentLength();

  String escapeChars();

  String lastGameMessagesLoad();

  String maxCommentLength();

  String removeAccountUrl();

  String linkToGame(String id);

  String draughtsOnlineBucket();
}
