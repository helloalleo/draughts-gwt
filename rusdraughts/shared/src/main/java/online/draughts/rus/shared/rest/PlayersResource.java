package online.shashki.rus.shared.rest;

import online.shashki.rus.shared.model.Friend;
import online.shashki.rus.shared.model.Player;

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
}
