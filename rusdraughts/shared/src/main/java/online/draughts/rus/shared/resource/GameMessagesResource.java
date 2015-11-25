package online.draughts.rus.shared.resource;

import online.draughts.rus.shared.model.GameMessage;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 24.11.15
 * Time: 19:47
 */
@Path(ApiPaths.MESSAGES)
@Produces(MediaType.APPLICATION_JSON)
public interface GameMessagesResource {

  @GET
  @Path(ApiPaths.LAST)
  List<GameMessage> findLastMessages(@QueryParam(ApiParameters.LIMIT) Integer countLast,
                                     @QueryParam(ApiParameters.PLAYER_ID) Long playerId,
                                     @QueryParam(ApiParameters.OPPONENT_ID) Long opponentId);
}
