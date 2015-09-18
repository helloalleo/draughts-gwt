package online.shashki.ru.shared.websocket.message;


import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import online.shashki.ru.shared.model.Game;
import online.shashki.ru.shared.model.GameMessage;
import online.shashki.ru.shared.model.Move;
import online.shashki.ru.shared.model.Shashist;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.12.14
 * Time: 16:37
 */
public interface MessageFactory extends AutoBeanFactory {

  AutoBean<Game> game();

  AutoBean<GameMessage> gameMessage();

  AutoBean<Shashist> shashist();

  AutoBean<Move> move();
}
