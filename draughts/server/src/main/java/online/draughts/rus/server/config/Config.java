package online.draughts.rus.server.config;

import com.google.inject.Singleton;

import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 15.03.15
 * Time: 18:43
 */
@Singleton
public class Config {
  private static ResourceBundle resourceBundle = ResourceBundle.getBundle("ServerConfiguration");

  public static final String GOOGLE_STORAGE_URL = resourceBundle.getString("google_storage_url");
  public static final String BUCKET_NAME = resourceBundle.getString("bucket_name");
  public static final String GAMES_ENDS_PATH = resourceBundle.getString("games_ends_path");

  public static final String SITE_URI = resourceBundle.getString("site_uri");
  public static final String SITE_NAME_EN = resourceBundle.getString("site_name_en");
  public static final String SITE_NAME_RU = resourceBundle.getString("site_name_ru");

  public static final String ONESIGNAL_APP_ID = resourceBundle.getString("onesignal_app_id");
  public static final String ONESIGNAL_POST_NOTIFICATION_URL = resourceBundle.getString("onesignal_post_notification_url");
  public static final String ONESIGNAL_APP_KEY = resourceBundle.getString("onesignal_app_key");

  public static final String NOT_FOUND_ERROR_URL = resourceBundle.getString("not_found_error_url");
  public static final String SERVER_ERROR_URL = resourceBundle.getString("server_error_url");
  public static final String CONTEXT = resourceBundle.getString("context");

  public static final String VK_REDIRECT_URI = resourceBundle.getString("vk_redirect_uri");
  public static final String VK_CLIENT_ID = resourceBundle.getString("vk_client_id");
  public static final String VK_CLIENT_SECRET = resourceBundle.getString("vk_client_secret");
  public static final String VK_AUTH_URI = resourceBundle.getString("vk_auth_uri");
  public static final String VK_TOKEN_URI = resourceBundle.getString("vk_token_uri");
  public static final String VK_API_USER_INFO = resourceBundle.getString("vk_api_user_info");
  public static final String VK_SCOPE = resourceBundle.getString("vk_scope");
  public static final String VK_API_VERSION = resourceBundle.getString("vk_api_version");

  public static final String FB_REDIRECT_URI = resourceBundle.getString("fb_redirect_uri");
  public static final String FB_CLIENT_ID = resourceBundle.getString("fb_client_id");
  public static final String FB_CLIENT_SECRET = resourceBundle.getString("fb_client_secret");
  public static final String FB_SCOPE = resourceBundle.getString("fb_scope");
  public static final String FB_API_VERSION = resourceBundle.getString("fb_api_version");
  public static final String FB_API_GRAPH = resourceBundle.getString("fb_api_graph");
  public static final String FB_API_OAUTH_PATH = resourceBundle.getString("fb_api_oauth_path");

  public static final String LOGIN_HASH = resourceBundle.getString("loginHash");
  public static final String PLAY_HASH = resourceBundle.getString("playHash");
  public static final String HOME_HASH = resourceBundle.getString("homeHash");

  public static final String GOOGLE_CLIENT_ID = resourceBundle.getString("google_client_id");
  public static final String GOOGLE_SCOPE = resourceBundle.getString("google_scope");
  public static final String GOOGLE_REDIRECT_URI = resourceBundle.getString("google_redirect_uri");
  public static final String GOOGLE_CLIENT_SECRET = resourceBundle.getString("google_client_secret");
  public static final String GOOGLE_API_USER_INFO = resourceBundle.getString("google_api_user_info");

  public static final boolean DEBUG = Boolean.valueOf(resourceBundle.getString("debug"));

  public static final String GAE_CRON_RESET_USERS_INTERVAL_HOUR = resourceBundle.getString("gae_cron_reset_users_interval_hour");
  public static final String GAE_CRON_IP_ADDR = resourceBundle.getString("gae_cron_ip_addr");

  public static final String FROM_EMAIL = resourceBundle.getString("from_email");
  public static final String ADMIN_MAIL = resourceBundle.getString("admin_mail");

  public static final int STRIP_PLAYER_NAME = Integer.valueOf(resourceBundle.getString("strip_player_name"));
}
