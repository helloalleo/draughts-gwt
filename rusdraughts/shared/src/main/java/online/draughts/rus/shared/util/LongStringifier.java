package online.draughts.rus.shared.util;

import com.googlecode.objectify.stringifier.Stringifier;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 05.12.15
 * Time: 14:57
 */
public class LongStringifier implements Stringifier<Long> {
  @Override
  public String toString(Long obj) {
    return String.valueOf(obj);
  }

  @Override
  public Long fromString(String str) {
    return Long.valueOf(str);
  }
}
