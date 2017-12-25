package online.draughts.rus.client.json;

import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.core.client.GWT;
import online.draughts.rus.shared.dto.GameMessageDto;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 24.09.15
 * Time: 12:36
 */
public interface GameMessageMapper extends ObjectMapper<GameMessageDto> {
  GameMessageMapper INSTANCE = GWT.create(GameMessageMapper.class);
}
