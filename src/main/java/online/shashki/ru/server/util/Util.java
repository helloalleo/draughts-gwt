package online.shashki.ru.server.util;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import online.shashki.ru.server.config.ConfigHelper;
import online.shashki.ru.server.servlet.oauth.ClientSecrets;
import online.shashki.ru.shared.model.GameMessage;
import online.shashki.ru.shared.websocket.message.MessageFactory;

import java.io.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 16.11.14
 * Time: 17:19
 */
public class Util {

  public static final String LOCALHOST = "127.0.0.1";
  public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
  public static final JsonFactory JSON_FACTORY = new JacksonFactory();

  public static AuthorizationCodeFlow getFlow(ClientSecrets secrets,
                                               List<String> scopes) throws IOException {
    return new AuthorizationCodeFlow.Builder(BearerToken.authorizationHeaderAccessMethod(),
        HTTP_TRANSPORT,
        JSON_FACTORY,
        new GenericUrl(secrets.getTokenUri()),
        new ClientParametersAuthentication(secrets.getClientId(), secrets.getClientSecret()),
        secrets.getClientId(),
        secrets.getAuthUri())
        .setCredentialDataStore(StoredCredential.getDefaultDataStore(new FileDataStoreFactory(
            new File(ConfigHelper.CREDENTIAL_STORE_FILE_PATH))))
        .setScopes(scopes)
        .build();
  }

  public static String inputStreamToString(InputStream inputStream) throws IOException {
    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
    return bufferedReader.readLine();
  }

  public static String serializePlayerMessageToJson(GameMessage message) {
    // Retrieve the AutoBean controller
    AutoBean<GameMessage> bean = AutoBeanUtils.getAutoBean(message);
    return AutoBeanCodex.encode(bean).getPayload();
  }

  public static GameMessage deserializeFromJson(String json) {
    MessageFactory messageFactory = AutoBeanFactorySource.create(MessageFactory.class);
    AutoBean<GameMessage> playerMessageBean = AutoBeanCodex.decode(messageFactory, GameMessage.class, json);
    return playerMessageBean.as();
  }
}
