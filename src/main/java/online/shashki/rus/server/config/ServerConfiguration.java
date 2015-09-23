package online.shashki.rus.server.config;

import javax.ejb.Stateless;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 15.03.15
 * Time: 18:43
 */
@Stateless
public class ServerConfiguration {

  private String context;
  private String vkRedirectUri;
  private String vkClientId;
  private String vkClientSecret;
  private String vkAuthUri;
  private String vkTokenUri;
  private String vkApiUserInfo;

  public ServerConfiguration() {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("SocialConfiguration");

    context = resourceBundle.getString("context");
    vkClientId = resourceBundle.getString("vk_client_id");
    vkClientSecret = resourceBundle.getString("vk_client_secret");
    vkRedirectUri = resourceBundle.getString("vk_redirect_uri");
    vkAuthUri = resourceBundle.getString("vk_auth_uri");
    vkTokenUri = resourceBundle.getString("vk_token_uri");
    vkApiUserInfo = resourceBundle.getString("vk_api_user_info");
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
}
