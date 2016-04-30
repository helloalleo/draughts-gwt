package online.draughts.rus.server.channel;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.common.base.Splitter;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import online.draughts.rus.server.domain.GameMessage;
import online.draughts.rus.server.domain.Player;
import online.draughts.rus.server.service.PlayerService;
import online.draughts.rus.server.util.Utils;
import online.draughts.rus.shared.channel.Chunk;
import online.draughts.rus.shared.dto.GameMessageDto;
import org.dozer.Mapper;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 16.12.15
 * Time: 13:40
 */
@Singleton
public class CoreChannel {

  private static Map<String, String> channelTokenPeers = Collections.synchronizedMap(new HashMap<String, String>());

  private final PlayerService playerService;
  private final Mapper mapper;

  @Inject
  public CoreChannel(PlayerService playerService,
                     Mapper mapper) {
    this.playerService = playerService;
    this.mapper = mapper;
  }

  void connectPlayer(GameMessage gameMessage, String token) {
    Player player = playerService.find(gameMessage.getSender().getId());

    player.setPlaying(false);
    player.setOnline(true);
    player.setLastVisited(new Date());
    playerService.save(player);

    final String channel = String.valueOf(player.getId());
    channelTokenPeers.put(channel, token);
    updatePlayerList();
  }

  void updatePlayerList() {
    GameMessage gameMessage = new GameMessage();
    gameMessage.setMessageType(GameMessageDto.MessageType.USER_LIST_UPDATE);
    List<Player> playerList = playerService.findAll();
    gameMessage.setPlayerList(playerList);
    for (String channelName : channelTokenPeers.keySet()) {
      sendMessage(channelName, gameMessage);
    }
  }


  void sendMessage(String channel, GameMessage message) {
    GameMessageDto dto = mapper.map(message, GameMessageDto.class);
    final String serialized = Utils.serializeToJson(dto);
    List<String> chunks = Splitter.fixedLength(1024 * 31).splitToList(serialized);
    for (int i = 0; i < chunks.size(); i++) {
      send(channel, Utils.serializeToJson(new Chunk(chunks.size(), i + 1, chunks.get(i))));
    }
  }

  private void send(String channel, String chunk) {
    ChannelServiceFactory.getChannelService().sendMessage(new ChannelMessage(channel, chunk));
  }

  Set<String> getChannels() {
    return channelTokenPeers.keySet();
  }

  void joinPlayer(String token, String channelName) {
    channelTokenPeers.put(channelName, token);
  }

  String getToken(String channel) {
    return channelTokenPeers.get(channel);
  }

  void disconnectChannel(String channel) {
    channelTokenPeers.remove(channel);
    updatePlayerList();
  }
}
