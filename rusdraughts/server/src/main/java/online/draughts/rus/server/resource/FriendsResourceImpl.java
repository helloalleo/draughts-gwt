package online.draughts.rus.server.resource;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import online.draughts.rus.server.service.FriendService;
import online.draughts.rus.server.utils.AuthUtils;
import online.draughts.rus.shared.model.Friend;
import online.draughts.rus.shared.resource.FriendsResource;

import javax.servlet.http.HttpServletRequest;

@RequestScoped
public class FriendsResourceImpl implements FriendsResource {

  private final FriendService friendService;
  private final HttpServletRequest request;

  @Inject
  FriendsResourceImpl(
      FriendService friendService,
      HttpServletRequest request) {
    this.friendService = friendService;
    this.request = request;
  }

  @Override
  public Friend saveOrCreate(Friend friend) {
    if (!AuthUtils.isAuthenticated(request.getSession())) {
      throw new RuntimeException("Unauthorized");
    }
    return friendService.saveOrCreate(friend);
  }
}
