package online.draughts.rus.server.domain.converter;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 08.01.16
 * Time: 11:51
 */
public interface Converter<T> {

  String convertTo(T value);

  T convertFrom(String value);
}
