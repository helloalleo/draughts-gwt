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

  private String vkRedirectUri;
  private String vkClientId;
  private String vkClientSecret;
  private String vkAuthUri;
  private String vkTokenUri;

  public ServerConfiguration() {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("SocialConfiguration");

    vkClientId = resourceBundle.getString("vk_client_id");
    vkClientSecret = resourceBundle.getString("vk_client_secret");
    vkRedirectUri = resourceBundle.getString("vk_redirect_uri");
    vkAuthUri = resourceBundle.getString("vk_auth_uri");
    vkTokenUri = resourceBundle.getString("vk_token_uri");
  }

  public String getVkRedirectUri() {
    return vkRedirectUri;
  }

  public void setVkRedirectUri(String vkRedirectUri) {
    this.vkRedirectUri = vkRedirectUri;
  }

  public String getVkClientId() {
    return vkClientId;
  }

  public void setVkClientId(String vkClientId) {
    this.vkClientId = vkClientId;
  }

  public String getVkClientSecret() {
    return vkClientSecret;
  }

  public void setVkClientSecret(String vkClientSecret) {
    this.vkClientSecret = vkClientSecret;
  }

  public String getVkAuthUri() {
    return vkAuthUri;
  }

  public void setVkAuthUri(String vkAuthUri) {
    this.vkAuthUri = vkAuthUri;
  }

  public String getVkTokenUri() {
    return vkTokenUri;
  }

  public void setVkTokenUri(String vkTokenUri) {
    this.vkTokenUri = vkTokenUri;
  }
}
