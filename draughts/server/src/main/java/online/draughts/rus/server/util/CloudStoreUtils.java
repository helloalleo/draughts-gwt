package online.draughts.rus.server.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.StorageScopes;
import com.google.api.services.storage.model.StorageObject;
import online.draughts.rus.server.config.Config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 02.06.16
 * Time: 7:15
 */
public class CloudStoreUtils {

  private static StorageObject uploadSimple(Storage storage, String bucketName, String objectName,
                                            InputStream data, String contentType) throws IOException {
    InputStreamContent mediaContent = new InputStreamContent(contentType, data);
    Storage.Objects.Insert insertObject = storage.objects().insert(bucketName, null, mediaContent)
        .setName(objectName).setPredefinedAcl("publicRead");
    // The media uploader gzips content by default, and alters the Content-Encoding accordingly.
    // GCS dutifully stores content as-uploaded. This line disables the media uploader behavior,
    // so the service stores exactly what is in the InputStream, without transformation.
    insertObject.getMediaHttpUploader().setDisableGZipContent(true);
    return insertObject.execute();
  }

  public static void saveFileToCloud(String fileName, byte[] data) throws GeneralSecurityException, IOException {
    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
//    Credential credential = CredentialsProvider.authorize(httpTransport, jsonFactory);
    GoogleCredential credential = GoogleCredential.fromStream(
        CloudStoreUtils.class.getResourceAsStream("/client_secrets.json"), httpTransport, jsonFactory)
        .createScoped(Collections.singleton(StorageScopes.DEVSTORAGE_FULL_CONTROL));
    Storage storage = new Storage.Builder(httpTransport, jsonFactory, credential)
        .setApplicationName("Google-ObjectsUploadExample/1.0").build();
    StorageObject object = uploadSimple(storage, Config.BUCKET_NAME, fileName,
        new ByteArrayInputStream(data), "image/png");
    System.out.println(object.getName() + " (size: " + object.getSize() + ")");
  }
}
