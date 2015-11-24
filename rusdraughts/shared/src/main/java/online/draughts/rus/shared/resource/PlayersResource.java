package online.draughts.rus.shared.resource;

import online.draughts.rus.shared.model.Friend;
import online.draughts.rus.shared.model.Player;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path(ApiPaths.PLAYERS)
@Produces(MediaType.APPLICATION_JSON)
public interface PlayersResource {

  @POST
  Player saveOrCreate(Player player);

  @GET
  @Path(ApiPaths.PLAYER_FRIEND_LIST)
  List<Friend> getPlayerFriendList(@QueryParam(ApiParameters.ID) Long playerId);

  @GET
  @Path(ApiPaths.PLAYERS_TOTAL)
  Integer totalPlayers();

  @GET
  @Path(ApiPaths.PLAYERS_ONLINE)
  Integer onlinePlayers();
}
