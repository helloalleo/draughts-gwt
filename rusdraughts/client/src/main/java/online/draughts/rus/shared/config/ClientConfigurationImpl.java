package online.draughts.rus.shared.config;

import java.util.Map;
import java.util.MissingResourceException;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.11.15
 * Time: 18:32
 */
public class ClientConfigurationImpl implements ClientConfiguration {

  @Override
  public String playerWebsocketUrl() {
    return null;
  }

  @Override
  public String debug() {
    return null;
  }

  @Override
  public String level() {
    return null;
  }

  @Override
  public String site_url() {
    return null;
  }

  @Override
  public String production() {
    return null;
  }

  @Override
  public String initShowGamesPageSize() {
    return "10";
  }

  @Override
  public String incrementPlayShowSize() {
    return null;
  }

  @Override
  public String strokeCommentLength() {
    return null;
  }

  @Override
  public String escapeChars() {
    return null;
  }

  @Override
  public boolean getBoolean(String methodName) throws MissingResourceException {
    return false;
  }

  @Override
  public double getDouble(String methodName) throws MissingResourceException {
    return 0;
  }

  @Override
  public float getFloat(String methodName) throws MissingResourceException {
    return 0;
  }

  @Override
  public int getInt(String methodName) throws MissingResourceException {
    return 0;
  }

  @Override
  public Map<String, String> getMap(String methodName) throws MissingResourceException {
    return null;
  }

  @Override
  public String getString(String methodName) throws MissingResourceException {
    return null;
  }

  @Override
  public String[] getStringArray(String methodName) throws MissingResourceException {
    return new String[0];
  }
}
