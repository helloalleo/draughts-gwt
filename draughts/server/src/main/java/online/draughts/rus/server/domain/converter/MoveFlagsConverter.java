package online.draughts.rus.server.domain.converter;

import online.draughts.rus.shared.dto.MoveDto;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 08.01.16
 * Time: 11:54
 */
public class MoveFlagsConverter implements Converter<MoveDto.MoveFlag> {

  @Override
  public String convertTo(MoveDto.MoveFlag value) {
    return value.name();
  }

  @Override
  public MoveDto.MoveFlag convertFrom(String value) {
    return MoveDto.MoveFlag.valueOf(value);
  }
}
