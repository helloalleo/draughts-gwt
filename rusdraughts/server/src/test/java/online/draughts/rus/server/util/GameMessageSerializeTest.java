package online.draughts.rus.server.util;

import online.draughts.rus.server.BaseTest;
import online.draughts.rus.server.domain.GameMessage;
import online.draughts.rus.server.guice.DatabaseModule;
import online.draughts.rus.server.guice.DbModule;
import org.jukito.JukitoRunner;
import org.jukito.UseModules;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 25.12.15
 * Time: 11:18
 */
@RunWith(JukitoRunner.class)
@UseModules({DatabaseModule.class, DbModule.class})
public class GameMessageSerializeTest extends BaseTest {

  @Test
  public void shouldDeserializeGameMessage() {
    GameMessage gameMessage = Utils.deserializeFromJson(
        "{\n" +
        "  \"sender\": {\n" +
        "    \"firstName\": \"Алексей\",\n" +
        "    \"lastName\": \"Попрядухин\",\n" +
        "    \"playerName\": null,\n" +
        "    \"rating\": 12,\n" +
        "    \"loggedIn\": true,\n" +
        "    \"playing\": true,\n" +
        "    \"online\": true,\n" +
        "    \"subscribed\": false,\n" +
        "    \"id\": 5629499534213120\n" +
        "  },\n" +
        "  \"receiver\": {\n" +
        "    \"firstName\": \"Алексей\",\n" +
        "    \"lastName\": \"Попрядухин\",\n" +
        "    \"playerName\": null,\n" +
        "    \"rating\": 6,\n" +
        "    \"loggedIn\": true,\n" +
        "    \"playing\": false,\n" +
        "    \"online\": true,\n" +
        "    \"subscribed\": false,\n" +
        "    \"id\": 5066549580791808\n" +
        "  },\n" +
        "  \"message\": null,\n" +
        "  \"messageType\": \"PLAY_OPPONENT_MOVE\",\n" +
        "  \"data\": null,\n" +
        "  \"sentDate\": null,\n" +
        "  \"move\": {\n" +
        "    \"number\": 1,\n" +
        "    \"moveOrder\": 0,\n" +
        "    \"first\": true,\n" +
        "    \"startPos\": {\n" +
        "      \"pos\": {\n" +
        "        \"row\": 5,\n" +
        "        \"col\": 6,\n" +
        "        \"id\": 0\n" +
        "      },\n" +
        "      \"white\": true,\n" +
        "      \"queen\": false,\n" +
        "      \"id\": 0\n" +
        "    },\n" +
        "    \"endPos\": {\n" +
        "      \"pos\": {\n" +
        "        \"row\": 4,\n" +
        "        \"col\": 7,\n" +
        "        \"id\": 0\n" +
        "      },\n" +
        "      \"white\": true,\n" +
        "      \"queen\": false,\n" +
        "      \"id\": 0\n" +
        "    },\n" +
        "    \"takenPos\": null,\n" +
        "    \"moveFlags\": [\n" +
        "      \"SIMPLE_MOVE\"\n" +
        "    ],\n" +
        "    \"title\": \"g3-h4\",\n" +
        "    \"comment\": null,\n" +
        "    \"hashTags\": [\n" +
        "      \"#dsffsd\"\n" +
        "    ],\n" +
        "    \"id\": 0\n" +
        "  },\n" +
        "  \"game\": {\n" +
        "    \"playerWhite\": {\n" +
        "      \"firstName\": \"Алексей\",\n" +
        "      \"lastName\": \"Попрядухин\",\n" +
        "      \"playerName\": null,\n" +
        "      \"rating\": 12,\n" +
        "      \"loggedIn\": true,\n" +
        "      \"playing\": false,\n" +
        "      \"online\": true,\n" +
        "      \"subscribed\": false,\n" +
        "      \"id\": 5629499534213120\n" +
        "    },\n" +
        "    \"playerBlack\": {\n" +
        "      \"firstName\": \"Алексей\",\n" +
        "      \"lastName\": \"Попрядухин\",\n" +
        "      \"playerName\": null,\n" +
        "      \"rating\": 6,\n" +
        "      \"loggedIn\": true,\n" +
        "      \"playing\": false,\n" +
        "      \"online\": true,\n" +
        "      \"subscribed\": false,\n" +
        "      \"id\": 5066549580791808\n" +
        "    },\n" +
        "    \"playEndStatus\": null,\n" +
        "    \"playStartDate\": 1451030558677,\n" +
        "    \"playFinishDate\": null,\n" +
        "    \"notation\": null,\n" +
        "    \"endGameScreenshot\": null,\n" +
        "    \"initialPos\": [],\n" +
        "    \"id\": 6509108836433920\n" +
        "  },\n" +
        "  \"playerList\": null,\n" +
        "  \"id\": 0\n" +
        "}", GameMessage.class);
    assertNotNull(gameMessage);
  }
}
