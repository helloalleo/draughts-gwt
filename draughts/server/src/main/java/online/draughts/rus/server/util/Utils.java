package online.draughts.rus.server.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import online.draughts.rus.server.config.Config;
import online.draughts.rus.shared.util.StringUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 16.11.14
 * Time: 17:19
 */
public class Utils {

  public static final Logger log = Logger.getAnonymousLogger();

  public static String inputStreamToString(InputStream inputStream) throws IOException {
    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
    return bufferedReader.readLine();
  }

  public static String serializeToJson(Object message) {
    ObjectMapper objectMapper = new ObjectMapper();
    StringWriter stringWriter = new StringWriter();
    try {
      objectMapper.writeValue(stringWriter, message);
    } catch (IOException e) {
      log.severe(e.getLocalizedMessage());
      return "";
    }
    return stringWriter.toString();
  }

  public static <T> T deserializeFromJson(String json, Class<T> clazz) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.readValue(json, clazz);
    } catch (IOException e) {
      log.severe("An error occurred while parsing JSON: " + e.getMessage());
      return null;
    }
  }

  public static String readFile(String path) {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    BufferedInputStream inputStream = (BufferedInputStream) loader.getResourceAsStream(path);
    String text = null;
    try {
      text = IOUtils.toString(inputStream, "UTF-8");
      inputStream.close();
    } catch (IOException e) {
      log.severe(e.getLocalizedMessage());
    }
    return text;
  }

  public static void sendPushNotification(String notificationsUserId, Map<String, String> message) {
    if (StringUtils.isEmpty(notificationsUserId)) {
      return;
    }
    CloseableHttpClient httpClient = HttpClients.createMinimal();
    HttpPost httpPost = new HttpPost(Config.ONESIGNAL_POST_NOTIFICATION_URL);
    httpPost.setHeader("Authorization", "Basic " + Config.ONESIGNAL_APP_KEY);
    httpPost.setHeader("Content-Type", "application/json");
    String json = "{\n" +
        "    \"app_id\": \"" + Config.ONESIGNAL_APP_ID + "\",\n" +
        "    \"include_player_ids\" : [\"" + notificationsUserId + "\"],\n" +
        "    \"contents\": {\"ru\": \"" + message.get("ru") + "\", " +
        "    \"en\": \"" + message.get("en") + "\"},\n" +
        "    \"include_segments\": [\"All\"],\n" +
        "    \"url\": \"https://shashki.online/" + Config.CONTEXT + "/\"," +
        "    \"headings\": {\"en\": \"" + Config.SITE_NAME_EN + "\", \"ru\": \"" + Config.SITE_NAME_RU + "\"}\n" +
        "}";
    StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
    httpPost.setEntity(entity);
    try {
      HttpResponse response = httpClient.execute(httpPost);
      log.info("Response " + response.toString());
    } catch (IOException e) {
      log.severe(e.getMessage());
    }
  }
}
