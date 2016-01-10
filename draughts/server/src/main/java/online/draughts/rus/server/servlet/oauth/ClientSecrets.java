package online.draughts.rus.server.servlet.oauth;

import online.draughts.rus.server.config.ServerConfiguration;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 03.01.15
 * Time: 14:51
 */
public class ClientSecrets {
  private String clientId;
  private String clientSecret;
  private String authUri;
  private String tokenUri;

  public ClientSecrets(ServerConfiguration serverConfiguration, SocialType socialType) {
    switch (socialType) {
      case VK:
        clientId = serverConfiguration.getVkClientId();
        clientSecret = serverConfiguration.getVkClientSecret();
        authUri = serverConfiguration.getVkAuthUri();
        tokenUri = serverConfiguration.getVkTokenUri();
        break;
    }
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  public String getAuthUri() {
    return authUri;
  }

  public void setAuthUri(String authUri) {
    this.authUri = authUri;
  }

  public String getTokenUri() {
    return tokenUri;
  }

  public void setTokenUri(String tokenUri) {
    this.tokenUri = tokenUri;
  }

  public enum SocialType {
    VK
  }

}
