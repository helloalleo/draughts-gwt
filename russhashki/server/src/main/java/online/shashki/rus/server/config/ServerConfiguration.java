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

  private String notFoundError;
  private String serverError;
  private String context;
  private String vkRedirectUri;
  private String vkClientId;
  private String vkClientSecret;
  private String vkAuthUri;
  private String vkTokenUri;
  private String vkApiUserInfo;
  private String vkScope;
  private String vkVersion;

  public ServerConfiguration() {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("ServerConfiguration");

    context = resourceBundle.getString("context");
    serverError = resourceBundle.getString("server_error");
    notFoundError = resourceBundle.getString("not_found_error");

    vkRedirectUri = resourceBundle.getString("vk_redirect_uri");
    vkClientId = resourceBundle.getString("vk_client_id");
    vkClientSecret = resourceBundle.getString("vk_client_secret");
    vkScope = resourceBundle.getString("vk_scope");
    vkAuthUri = resourceBundle.getString("vk_auth_uri");
    vkTokenUri = resourceBundle.getString("vk_token_uri");
    vkApiUserInfo = resourceBundle.getString("vk_api_user_info");
    vkVersion = resourceBundle.getString("vk_version");
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

  public String getVkVersion() {
    return vkVersion;
  }

  public void setVkVersion(String vkVersion) {
    this.vkVersion = vkVersion;
  }
}
