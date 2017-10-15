package online.draughts.rus.server.channel;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.appengine.api.appidentity.AppIdentityService;
import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import com.google.common.base.Preconditions;
import com.google.common.io.BaseEncoding;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import online.draughts.rus.server.domain.GameMessage;
import online.draughts.rus.server.domain.Player;
import online.draughts.rus.server.service.PlayerService;
import online.draughts.rus.shared.dto.GameMessageDto;
import online.draughts.rus.shared.util.StringUtils;
import org.dozer.Mapper;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 16.12.15
 * Time: 13:40
 */
@Singleton
public class CoreChannel {

  private static final String FIREBASE_SNIPPET_PATH = "/WEB-INF/view/firebase_config.jspf";
  static InputStream firebaseConfigStream = null;
  private static final Collection FIREBASE_SCOPES = Arrays.asList(
      "https://www.googleapis.com/auth/firebase.database",
      "https://www.googleapis.com/auth/userinfo.email"
  );
  private static final String IDENTITY_ENDPOINT =
      "https://identitytoolkit.googleapis.com/google.identity.identitytoolkit.v1.IdentityToolkit";

  private String firebaseDbUrl;
  private GoogleCredential credential;
  // Keep this a package-private member variable, so that it can be mocked for unit tests
  UrlFetchTransport httpTransport;

  private static CoreChannel instance;


  private Set<String> channelTokenPeers = new HashSet<>();

  private final PlayerService playerService;
  private final Mapper mapper;

  @Inject
  public CoreChannel(PlayerService playerService,
                     Mapper mapper) {
    this.playerService = playerService;
    this.mapper = mapper;
  }

  public void initFirebase(ServletContext servletContext) {
    try {
      // This variables exist primarily so it can be stubbed out in unit tests.
      if (null == firebaseConfigStream) {
        Preconditions.checkNotNull(servletContext,
            "Servlet context expected to initialize Firebase channel");
        firebaseConfigStream = servletContext.getResourceAsStream(FIREBASE_SNIPPET_PATH);
      }

      String firebaseSnippet = CharStreams.toString(new InputStreamReader(
          firebaseConfigStream, StandardCharsets.UTF_8));
      firebaseDbUrl = parseFirebaseUrl(firebaseSnippet);

      credential = GoogleCredential.getApplicationDefault().createScoped(FIREBASE_SCOPES);
      httpTransport = UrlFetchTransport.getDefaultInstance();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * CoreChannel is a singleton, since it's just utility functions.
   * The class derives auth information when first instantiated.
   */
//  public static CoreChannel getInstance(ServletContext servletContext) {
//    if (instance == null) {
//      instance = new CoreChannel(servletContext);
//    }
//    return instance;
//  }

  /**
   * Parses out the Firebase database url from the client-side code snippet.
   * The code snippet is a piece of javascript that defines an object with the key 'databaseURL'. So
   * look for that key, then parse out its quote-surrounded value.
   */
  private static String parseFirebaseUrl(String firebaseSnippet) {
    int idx = firebaseSnippet.indexOf("databaseURL");
    if (-1 == idx) {
      throw new RuntimeException(
          "Please copy your Firebase web snippet into " + FIREBASE_SNIPPET_PATH);
    }
    idx = firebaseSnippet.indexOf(':', idx);
    int openQuote = firebaseSnippet.indexOf('"', idx);
    int closeQuote = firebaseSnippet.indexOf('"', openQuote + 1);
    return firebaseSnippet.substring(openQuote + 1, closeQuote);
  }

  public void sendFirebaseMessage(String channelKey, GameMessageDto game)
      throws IOException {
    // Make requests auth'ed using Application Default Credentials
    HttpRequestFactory requestFactory = httpTransport.createRequestFactory(credential);
    GenericUrl url = new GenericUrl(
        String.format("%s/channels/%s.json", firebaseDbUrl, channelKey));
    HttpResponse response = null;

    try {
      if (null == game) {
        response = requestFactory.buildDeleteRequest(url).execute();
      } else {
        String gameJson = new Gson().toJson(game);
        response = requestFactory.buildPatchRequest(
            url, new ByteArrayContent("application/json", gameJson.getBytes())).execute();
      }

      if (response.getStatusCode() != 200) {
        throw new RuntimeException(
            "Error code while updating Firebase: " + response.getStatusCode());
      }

    } finally {
      if (null != response) {
        response.disconnect();
      }
    }
  }

  /**
   * Create a secure JWT token for the given userId.
   */
  public String createFirebaseToken(String channelKey) {
    final AppIdentityService appIdentity = AppIdentityServiceFactory.getAppIdentityService();
    final BaseEncoding base64 = BaseEncoding.base64();

    String header = base64.encode("{\"typ\":\"JWT\",\"alg\":\"RS256\"}".getBytes());

    // Construct the claim
    String clientEmail = appIdentity.getServiceAccountName();
    long epochTime = System.currentTimeMillis() / 1000;
    long expire = epochTime + 60 * 60; // an hour from now

    Map<String, Object> claims = new HashMap<String, Object>();
    claims.put("iss", clientEmail);
    claims.put("sub", clientEmail);
    claims.put("aud", IDENTITY_ENDPOINT);
    claims.put("uid", channelKey);
    claims.put("iat", epochTime);
    claims.put("exp", expire);

    String payload = base64.encode(new Gson().toJson(claims).getBytes());
    String toSign = String.format("%s.%s", header, payload);
    AppIdentityService.SigningResult result = appIdentity.signForApp(toSign.getBytes());
    return String.format("%s.%s", toSign, base64.encode(result.getSignature()));
  }

  // The following methods are to illustrate making various calls to Firebase from App Engine
  // Standard
  public HttpResponse firebasePut(String path, Object object) throws IOException {
    // Make requests auth'ed using Application Default Credentials
    Credential credential = GoogleCredential.getApplicationDefault().createScoped(FIREBASE_SCOPES);
    HttpRequestFactory requestFactory = httpTransport.createRequestFactory(credential);

    String json = new Gson().toJson(object);
    GenericUrl url = new GenericUrl(path);

    return requestFactory.buildPutRequest(
        url, new ByteArrayContent("application/json", json.getBytes())).execute();
  }

  public HttpResponse firebasePatch(String path, Object object) throws IOException {
    // Make requests auth'ed using Application Default Credentials
    Credential credential = GoogleCredential.getApplicationDefault().createScoped(FIREBASE_SCOPES);
    HttpRequestFactory requestFactory = httpTransport.createRequestFactory(credential);

    String json = new Gson().toJson(object);
    GenericUrl url = new GenericUrl(path);

    return requestFactory.buildPatchRequest(
        url, new ByteArrayContent("application/json", json.getBytes())).execute();
  }

  public HttpResponse firebasePost(String path, Object object) throws IOException {
    // Make requests auth'ed using Application Default Credentials
    Credential credential = GoogleCredential.getApplicationDefault().createScoped(FIREBASE_SCOPES);
    HttpRequestFactory requestFactory = httpTransport.createRequestFactory(credential);

    String json = new Gson().toJson(object);
    GenericUrl url = new GenericUrl(path);

    return requestFactory.buildPostRequest(
        url, new ByteArrayContent("application/json", json.getBytes())).execute();
  }

  public HttpResponse firebaseGet(String path) throws IOException {
    // Make requests auth'ed using Application Default Credentials
    Credential credential = GoogleCredential.getApplicationDefault().createScoped(FIREBASE_SCOPES);
    HttpRequestFactory requestFactory = httpTransport.createRequestFactory(credential);

    GenericUrl url = new GenericUrl(path);

    return requestFactory.buildGetRequest(url).execute();
  }

  public HttpResponse firebaseDelete(String path) throws IOException {
    // Make requests auth'ed using Application Default Credentials
    Credential credential = GoogleCredential.getApplicationDefault().createScoped(FIREBASE_SCOPES);
    HttpRequestFactory requestFactory = httpTransport.createRequestFactory(credential);

    GenericUrl url = new GenericUrl(path);

    return requestFactory.buildDeleteRequest(url).execute();
  }

  void connectPlayer(String clientId) {
    if (StringUtils.isEmpty(clientId)) {
      return;
    }
    Player player = playerService.find(Long.valueOf(clientId));
    if (player.isBanned()) {
      return;
    }

    player.setPlaying(false);
    player.setOnline(true);
    player.setLastVisited(new Date());
    playerService.save(player);

    final String channel = String.valueOf(player.getId());
    channelTokenPeers.add(channel);
    updatePlayerList();
  }

  void updatePlayerList() {
    GameMessage gameMessage = new GameMessage();
    gameMessage.setMessageType(GameMessageDto.MessageType.USER_LIST_UPDATE);
    List<Player> playerList = playerService.findOnline();
    gameMessage.setPlayerList(playerList);
    for (String channelName : channelTokenPeers) {
      sendMessage(channelName, gameMessage);
    }
  }


  void sendMessage(String channel, GameMessage message) {
    GameMessageDto dto = mapper.map(message, GameMessageDto.class);
    try {
      sendFirebaseMessage(channel, dto);
    } catch (IOException e) {
      e.printStackTrace();
    }
//    final String serialized = Utils.serializeToJson(dto);
//    List<String> chunks = Splitter.fixedLength(1024 * 15).splitToList(serialized);
//    int chunksSize = chunks.size();
//    for (int i = 0; i < chunksSize; i++) {
//      send(channel, Utils.serializeToJson(new Chunk(chunksSize, i + 1, chunks.get(i))));
//    }
  }

//  private void send(String channel, String chunk) {
//    ChannelServiceFactory.getChannelService().sendMessage(new ChannelMessage(channel, chunk));
//  }

  Set<String> getChannels() {
    return channelTokenPeers;
  }

  void disconnectChannel(String channel) {
    channelTokenPeers.remove(channel);
    updatePlayerList();
  }
}
