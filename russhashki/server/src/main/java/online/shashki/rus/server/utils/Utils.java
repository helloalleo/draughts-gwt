package online.shashki.rus.server.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import online.shashki.rus.shared.model.GameMessage;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 16.11.14
 * Time: 17:19
 */
public class Utils {

//  public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
//  public static final JsonFactory JSON_FACTORY = new JacksonFactory();

//  public static AuthorizationCodeFlow getFlow(ClientSecrets secrets,
//                                              List<String> scopes) throws IOException {
//    return new AuthorizationCodeFlow.Builder(BearerToken.authorizationHeaderAccessMethod(),
//        HTTP_TRANSPORT,
//        JSON_FACTORY,
//        new GenericUrl(secrets.getTokenUri()),
//        new ClientParametersAuthentication(secrets.getClientId(), secrets.getClientSecret()),
//        secrets.getClientId(),
//        secrets.getAuthUri())
//        .setCredentialDataStore(StoredCredential.getDefaultDataStore(new FileDataStoreFactory(
//            new File(ConfigHelper.CREDENTIAL_STORE_FILE_PATH))))
//        .setScopes(scopes)
//        .build();
//  }

  public static String inputStreamToString(InputStream inputStream) throws IOException {
    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
    return bufferedReader.readLine();
  }

  public static String serializeGameMessageToJson(GameMessage message) {
    ObjectMapper objectMapper = new ObjectMapper();
    StringWriter stringWriter = new StringWriter();
    try {
      objectMapper.writeValue(stringWriter, message);
    } catch (IOException e) {
      e.printStackTrace();
      return "";
    }
    return stringWriter.toString();
  }

  public static GameMessage deserializeGameMessageFromJson(String json) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(json, GameMessage.class);
  }
}
