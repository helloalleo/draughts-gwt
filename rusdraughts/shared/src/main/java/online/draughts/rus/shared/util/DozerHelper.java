package online.draughts.rus.shared.util;

import org.dozer.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 05.12.15
 * Time: 18:18
 */
public class DozerHelper {

  public static <T, U> ArrayList<U> map(final Mapper mapper, final List<T> source, final Class<U> destType) {

    final ArrayList<U> dest = new ArrayList<U>();

    for (T element : source) {
      if (element == null) {
        continue;
      }
      dest.add(mapper.map(element, destType));
    }

    // finally remove all null values if any
    List s1 = new ArrayList();
    s1.add(null);
    dest.removeAll(s1);

    return dest;
  }
}
