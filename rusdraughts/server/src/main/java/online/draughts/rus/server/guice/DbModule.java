package online.draughts.rus.server.guice;

import com.google.inject.AbstractModule;
import online.draughts.rus.server.dao.FriendDao;
import online.draughts.rus.server.dao.GameDao;
import online.draughts.rus.server.dao.GameMessageDao;
import online.draughts.rus.server.dao.PlayerDao;
import online.draughts.rus.server.dao.impl.FriendDaoImpl;
import online.draughts.rus.server.dao.impl.GameDaoImpl;
import online.draughts.rus.server.dao.impl.GameMessageDaoImpl;
import online.draughts.rus.server.dao.impl.PlayerDaoImpl;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 08.10.15
 * Time: 19:50
 */
public class DbModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(PlayerDao.class).to(PlayerDaoImpl.class);
    bind(FriendDao.class).to(FriendDaoImpl.class);
    bind(GameDao.class).to(GameDaoImpl.class);
    bind(GameMessageDao.class).to(GameMessageDaoImpl.class);
  }
}
