package online.shashki.rus.shared.rest;

import online.shashki.rus.shared.model.Friend;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(ApiPaths.PLAYERS)
@Produces(MediaType.APPLICATION_JSON)
public interface FriendsResource {

  @POST
  Friend saveOrCreate(Friend friend);
}
