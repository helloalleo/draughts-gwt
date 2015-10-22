package online.shashki.rus.server.config;

import com.google.inject.Singleton;

import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 15.03.15
 * Time: 18:43
 */
@Singleton
public class ServerConfiguration {

  private final String notFoundError;
  private final String serverError;
  private final String context;
  private final String vkRedirectUri;
  private final String vkClientId;
  private final String vkClientSecret;
  private final String vkAuthUri;
  private final String vkTokenUri;
  private final String vkApiUserInfo;
  private final String vkScope;
  private final String vkApiVersion;
  private final String fbRedirectUri;
  private final String fbClientId;
  private final String fbClientSecret;
  private final String fbScope;
  private final String fbApiVersion;
  private final String fbApiGraph;
  private String fbApiOAuthPath;
  private String loginUrl;
  private String homeUrl;

  public ServerConfiguration() {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("ServerConfiguration");

    context = resourceBundle.getString("context");
    serverError = resourceBundle.getString("server_error");
    notFoundError = resourceBundle.getString("not_found_error");
    loginUrl = resourceBundle.getString("login");
    homeUrl = resourceBundle.getString("home");

    // VK API Data
    vkRedirectUri = resourceBundle.getString("vk_redirect_uri");
    vkClientId = resourceBundle.getString("vk_client_id");
    vkClientSecret = resourceBundle.getString("vk_client_secret");
    vkScope = resourceBundle.getString("vk_scope");
    vkAuthUri = resourceBundle.getString("vk_auth_uri");
    vkTokenUri = resourceBundle.getString("vk_token_uri");
    vkApiUserInfo = resourceBundle.getString("vk_api_user_info");
    vkApiVersion = resourceBundle.getString("vk_api_version");

    // Facebook API Data
    fbRedirectUri = resourceBundle.getString("fb_redirect_uri");
    fbApiGraph = resourceBundle.getString("fb_api_graph");
    fbApiOAuthPath = resourceBundle.getString("fb_api_oauth_path");
    fbClientId = resourceBundle.getString("fb_client_id");
    fbClientSecret = resourceBundle.getString("fb_client_secret");
    fbScope = resourceBundle.getString("fb_scope");
    fbApiVersion = resourceBundle.getString("fb_api_version");
  }

  public String getNotFoundError() {
    return notFoundError;
  }

  public String getServerError() {
    return serverError;
  }

  public String getContext() {
    return context;
  }

  public String getVkRedirectUri() {
    return vkRedirectUri;
  }

  public String getVkClientId() {
    return vkClientId;
  }

  public String getVkClientSecret() {
    return vkClientSecret;
  }

  public String getVkAuthUri() {
    return vkAuthUri;
  }

  public String getVkTokenUri() {
    return vkTokenUri;
  }

  public String getVkApiUserInfo() {
    return vkApiUserInfo;
  }

  public String getVkScope() {
    return vkScope;
  }

  public String getVkApiVersion() {
    return vkApiVersion;
  }

  public String getFbRedirectUri() {
    return fbRedirectUri;
  }

  public String getFbClientId() {
    return fbClientId;
  }

  public String getFbClientSecret() {
    return fbClientSecret;
  }

  public String getFbScope() {
    return fbScope;
  }

  public String getFbApiVersion() {
    return fbApiVersion;
  }

  public String getFbApiGraph() {
    return fbApiGraph;
  }

  public String getFbApiOAuthPath() {
    return fbApiOAuthPath;
  }

  public String getLoginUrl() {
    return loginUrl;
  }


  public String getHomeUrl() {
    return homeUrl;
  }
}
