/*
 * Copyright 2014 ArcBees Inc.
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

package online.shashki.rus.server.rest;

import com.google.inject.assistedinject.Assisted;
import online.shashki.rus.server.dao.GameDao;
import online.shashki.rus.server.utils.AuthUtils;
import online.shashki.rus.shared.rest.GameResource;
import online.shashki.rus.shared.model.Game;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

public class GameResourceImpl implements GameResource {
  private final GameDao gameDao;
  private final Long gameId;
  private final HttpServletRequest request;

  @Inject
  GameResourceImpl(
      GameDao gameDao,
      HttpServletRequest request,
      @Assisted Long gameId) {
    this.gameDao = gameDao;
    this.gameId = gameId;
    this.request = request;
  }

  @Override
  public Game get() {
    if (!AuthUtils.isAuthenticated(request.getSession())) {
      throw new RuntimeException("Unauthorized");
    }

    return gameDao.findLazyFalse(gameId);
  }
}
