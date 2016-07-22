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
  public static final String SERVER_RESOURCE = "/shashki/resource";

  static final String GAMES = "/games";

  // players
  static final String ERRORS = "/errors";
  static final String PLAYERS = "/players";
  static final String RESET_UNREAD = "/reset_unread";
  static final String PLAYERS_TOTAL = "/total";
  static final String PLAYERS_ONLINE = "/online";

  static final String COUNT = "/count";
  static final String LOGGED_IN_USER = "/loggedin";

  // friends
  static final String FRIENDS = "/friends";
  static final String PLAYER_FRIEND_LIST = "/player/friends";

  // messages
  static final String MESSAGES = "/messages";
  static final String LAST = "/last";
  static final String UNREAD = "/unread";

  static final String PATH_ID = "/{id}";
  static final String NOTIFICATIONS = "/notifications";

  static final String COACH_APPLY = "/coachApply";
  static final String ERROR = "/error";
}
