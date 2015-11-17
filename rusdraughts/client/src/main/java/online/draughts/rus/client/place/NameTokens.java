package online.draughts.rus.client.place;

import com.google.inject.Inject;
import online.draughts.rus.shared.locale.DraughtsMessages;

public class NameTokens {
  // токены - адреса в навигации
  public static final String homePage = "!home";
  public static final String playPage = "!play";
  public static final String loginPage = "!login";
  public static final String profilePage = "!profile";

  public static final String errorPage = "!error";
  public static final String settingsPage = "!settings";
  public static final String logoutPage = "/rus/logout";

  // ссылки - название и токен
  private final Link homeLink;
  private final Link loginLink;
  private final Link logoutLink;
  private final Link profileLink;
  private final Link settingsLink;
  private final Link playLink;

  @Inject
  public NameTokens(DraughtsMessages messages) {
    homeLink = new Link(homePage, messages.home());
    playLink = new Link(playPage, messages.play());
    loginLink = new Link(loginPage, messages.login());
    logoutLink = new Link(logoutPage, messages.logout());
    profileLink = new Link(profilePage, messages.profile());
    settingsLink = new Link(settingsPage, messages.settings());
  }

  public Link[] getLeftLinks() {
    return new Link[]{homeLink, playLink};
  }

  public Link[] getRightLinks() {
    return new Link[]{loginLink};
  }

  public Link[] getRightAuthLinks() {
    return new Link[]{profileLink, logoutLink};
  }

  public Link[] getProfileLinks() {
    return new Link[]{settingsLink};
  }

  public static class Link {
    public String token;
    public String name;

    public Link(String token, String name) {
      this.token = token;
      this.name = name;
    }
  }
}
