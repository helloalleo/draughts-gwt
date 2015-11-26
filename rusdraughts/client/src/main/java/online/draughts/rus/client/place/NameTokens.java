package online.draughts.rus.client.place;

import com.google.inject.Inject;
import online.draughts.rus.shared.locale.DraughtsMessages;
import org.gwtbootstrap3.client.ui.constants.IconType;

public class NameTokens {
  // токены - адреса в навигации
  public static final String homePage = "!home";
  public static final String learnPage = "!learn";
  public static final String playPage = "!play";
  public static final String loginPage = "!login";
  public static final String profilePage = "!profile";
  public static final String lovePage = "!lovePage";

  public static final String errorPage = "!error";
  public static final String settingsPage = "!settings";
  public static final String logoutPage = "/logout";

  // ссылки - название и токен
  private final Link homeLink;
  private final Link learnLink;
  private final Link loveLink;
  private final Link loginLink;
  private final Link logoutLink;
  private final Link profileLink;
  private final Link settingsLink;
  private final Link playLink;

  @Inject
  public NameTokens(DraughtsMessages messages) {
    homeLink = new Link(homePage, messages.home(), "", IconType.HOME);
    learnLink = new Link(learnPage, messages.learn(), "", null);
    playLink = new Link(playPage, messages.play(), "", IconType.PLAY_CIRCLE_O);
    loveLink = new Link(lovePage, "", "", IconType.HEART_O);
    loginLink = new Link(loginPage, "", messages.login(), IconType.SIGN_IN);
    logoutLink = new Link(logoutPage, messages.logout(), logoutPage, IconType.SIGN_OUT);
    profileLink = new Link(profilePage, messages.profile(), "", IconType.USER);
    settingsLink = new Link(settingsPage, messages.settings(), "", IconType.GEAR);
  }

  public static String getLovePage() {
    return lovePage;
  }

  public Link[] getLeftLinks() {
    return new Link[]{homeLink, learnLink, loveLink};
  }

  public Link[] getLeftAuthLinks() {
    return new Link[]{homeLink, playLink, learnLink, loveLink};
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
    public String href;
    public IconType iconType;

    public Link(String token, String name, String href, IconType iconType) {
      this.token = token;
      this.name = name;
      this.href = href;
      this.iconType = iconType;
    }
  }
}
