/*
 * Copyright 2013 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package online.draughts.rus.shared.resource;

public class ApiPaths {
  public static final String RESOURCE = "/resource";
  public static final String SERVER_RESOURCE = "/d/resource";

  public static final String GAMES = "/games";

  // players
  public static final String PLAYERS = "/players";
  public static final String RESET_UNREAD = "/reset_unread";
  public static final String PLAYERS_TOTAL = "/total";
  public static final String PLAYERS_ONLINE = "/online";

  public static final String COUNT = "/count";
  public static final String LOGGED_IN_USER = "/loggedin";

  // friends
  public static final String FRIENDS = "/friends";
  public static final String PLAYER_FRIEND_LIST = "/player/friends";

  // messages
  public static final String MESSAGES = "/messages";
  public static final String LAST = "/last";
  public static final String UNREAD = "/unread";

  public static final String PATH_ID = "/{id}";
}
