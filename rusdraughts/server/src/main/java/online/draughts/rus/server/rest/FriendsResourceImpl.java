package online.shashki.rus.server.rest;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import online.shashki.rus.server.service.FriendService;
import online.shashki.rus.server.utils.AuthUtils;
import online.shashki.rus.shared.model.Friend;
import online.shashki.rus.shared.rest.FriendsResource;

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
