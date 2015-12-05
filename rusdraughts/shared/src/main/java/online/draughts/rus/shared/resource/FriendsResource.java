package online.draughts.rus.shared.resource;

import online.draughts.rus.shared.dto.FriendDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path(ApiPaths.FRIENDS)
@Produces(MediaType.APPLICATION_JSON)
public interface FriendsResource {

  @POST
  FriendDto save(FriendDto friend);

  @GET
  @Path(ApiPaths.PLAYER_FRIEND_LIST)
  List<FriendDto> getPlayerFriendList(@QueryParam(ApiParameters.ID) Long playerId);
}
