package online.draughts.rus.shared.resource;

import online.draughts.rus.shared.dto.FriendDto;
import online.draughts.rus.shared.dto.PlayerDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path(ApiPaths.PLAYERS)
@Produces(MediaType.APPLICATION_JSON)
public interface PlayersResource {

  @POST
  PlayerDto save(PlayerDto player);

  @GET
  @Path(ApiPaths.PLAYERS_TOTAL)
  Integer totalPlayers();

  @GET
  @Path(ApiPaths.PLAYERS_ONLINE)
  Integer onlinePlayers();

  @POST
  @Path(ApiPaths.RESET_UNREAD)
  void resetUnreadMessages(@QueryParam(ApiParameters.PLAYER_ID) Long playerId,
                           @QueryParam(ApiParameters.FRIEND_ID) Long friendId);
}
