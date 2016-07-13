package online.draughts.rus.client.place;

import com.google.inject.Inject;
import online.draughts.rus.shared.locale.DraughtsMessages;
import org.gwtbootstrap3.client.ui.constants.IconType;

public class NameTokens {
  // токены - адреса в навигации
  public static final String HOME_PAGE = "!/home";
  public static final String LEARN_PAGE = "!/learn";
  public static final String PLAY_PAGE = "!/play";
  public static final String GAME_PAGE = "!/game";
  public static final String MY_GAMES_PAGE = "!/mygames";
  public static final String ANALYSIS_PAGE = "!/analysis";
  public static final String LOGIN_PAGE = "!/login";
  public static final String PROFILE_PAGE = "!/profile";
  public static final String RULES_PAGE = "!/rules";
  public static final String LOVE_PAGE = "!/love";

  public static final String ERROR_PAGE = "!/error";
  public static final String GENERAL_SETTINGS_PAGE = "!/generalSettings";
  public static final String SETTINGS_PAGE = "!/settings";
  public static final String LOGOUT_PAGE = "/logout";
  private final String ruShashkiNetUrl = "https://rushashki-net.appspot.com";

  // ссылки - название и токен
  private final Link homeLink;
  private final Link learnLink;
  private final Link loveLink;
  private final Link rulesLink;
  private final Link loginLink;
  private final Link logoutLink;
  private final Link profileLink;
  private final Link generalSettingsLink;
  private final Link settingsLink;
  private final Link playLink;
  private final Link myGamesLink;
//  private final Link gameLink;
  private final Link analysisLink;
  private final Link ruShashkiNetLink;

  @Inject
  public NameTokens(DraughtsMessages messages) {
    homeLink = new Link(HOME_PAGE, messages.home(), null, IconType.HOME);
    ruShashkiNetLink = new Link(null, "RuShashkiNet", ruShashkiNetUrl, IconType.LINK);
    learnLink = new Link(LEARN_PAGE, messages.learn(), null, null);
    playLink = new Link(PLAY_PAGE, messages.play(), null, IconType.PLAY_CIRCLE_O);
//    gameLink = new Link(GAME_PAGE, messages.game(), null, IconType.PLAY_CIRCLE_O);
    analysisLink = new Link(ANALYSIS_PAGE, messages.analysis(), null, null);
    loveLink = new Link(LOVE_PAGE, "", null, IconType.HEART_O);
    loginLink = new Link(LOGIN_PAGE, messages.login(), null, IconType.SIGN_IN);
    logoutLink = new Link(null, messages.logout(), LOGOUT_PAGE, IconType.SIGN_OUT);
    profileLink = new Link(PROFILE_PAGE, messages.profile(), null, IconType.USER);
    this.generalSettingsLink = new Link(GENERAL_SETTINGS_PAGE, messages.generalSettings(), null, IconType.USER);
    settingsLink = new Link(SETTINGS_PAGE, messages.settings(), null, IconType.GEAR);
    rulesLink = new Link(RULES_PAGE, messages.rules(), null, null);
    myGamesLink = new Link(MY_GAMES_PAGE, messages.myGames(), null, null);
  }

  public static String getLovePage() {
    return LOVE_PAGE;
  }

  public Link[] getLeftLinks() {
    return new Link[]{homeLink, rulesLink};
  }

  public Link[] getLeftAuthLinks() {
    return new Link[]{homeLink, playLink, myGamesLink, rulesLink};
  }

  public Link[] getRightLinks() {
    return new Link[]{loginLink};
  }

  public Link[] getRightAuthLinks() {
    return new Link[]{profileLink, logoutLink};
  }

  public Link[] getProfileLinks() {
    return new Link[]{generalSettingsLink, settingsLink};
  }

  public static class Link {
    public String token;
    public String name;
    public String href;
    public IconType iconType;

    Link(String token, String name, String href, IconType iconType) {
      this.token = token;
      this.name = name;
      this.href = href;
      this.iconType = iconType;
    }
  }
}
