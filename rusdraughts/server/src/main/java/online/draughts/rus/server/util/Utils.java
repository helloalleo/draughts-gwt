package online.draughts.rus.server.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.*;
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

  public static Object deserializeFromJson(String json, Class clazz) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(json, clazz);
  }

  public static String readFile(String path) {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    BufferedInputStream inputStream = (BufferedInputStream) loader.getResourceAsStream(path);
    String text = null;
    try {
      text = IOUtils.toString(inputStream);
      inputStream.close();
    } catch (IOException e) {
      log.severe(e.getLocalizedMessage());
    }
    return text;
  }
}
