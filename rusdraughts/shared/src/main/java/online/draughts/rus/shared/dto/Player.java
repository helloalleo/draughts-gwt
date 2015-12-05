package online.draughts.rus.shared.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.GwtTransient;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Stringify;
import online.draughts.rus.shared.util.LongStringifier;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.12.14
 * Time: 0:36
 */
public class Player {

  private String firstName;

  private String lastName;

  private String playerName;

  private int rating = 0;

  private boolean loggedIn;
  private boolean playing;
  private boolean online;

  private boolean subscribed;

  public Player() {
  }

  public boolean getSubscribed() {
    return subscribed;
  }

  public Player setSubscribed(boolean subscribed) {
    this.subscribed = subscribed;
    return this;
  }

  public boolean isSubscribed() {
    return subscribed;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPlayerName() {
    return playerName;
  }

  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }

  public boolean isLoggedIn() {
    return loggedIn;
  }

  public void setLoggedIn(boolean loggedIn) {
    this.loggedIn = loggedIn;
  }

  public boolean isPlaying() {
    return playing;
  }

  public void setPlaying(boolean playing) {
    this.playing = playing;
  }

  public boolean isOnline() {
    return online;
  }

  public void setOnline(boolean online) {
    this.online = online;
  }

  public int getRating() {
    return rating;
  }

  public Player setRating(int rating) {
    this.rating = rating;
    return this;
  }

  /**
   * Обновляем только сеарилизуемые поля
   *
   * @param player
   */
  public enum AuthProvider {
    VK,
    FACEBOOK,
    GOOGLE
  }
}
