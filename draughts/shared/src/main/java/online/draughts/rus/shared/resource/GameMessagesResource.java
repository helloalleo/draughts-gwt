package online.draughts.rus.shared.resource;

import online.draughts.rus.shared.dto.GameMessageDto;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

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
  List<GameMessageDto> findLastMessages(@QueryParam(ApiParameters.LIMIT) Integer countLast,
                                     @QueryParam(ApiParameters.PLAYER_ID) Long playerId,
                                     @QueryParam(ApiParameters.OPPONENT_ID) Long opponentId);

  @GET
  @Path(ApiPaths.UNREAD)
  Map<Long, Integer> findUnreadMessages(@QueryParam(ApiParameters.ID) Long playerId);
}
