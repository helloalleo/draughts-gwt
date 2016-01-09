package online.draughts.rus.server.domain.command;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;
import online.draughts.rus.server.annotation.Converter;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.01.16
 * Time: 22:58
 */
public class SetCommand<T> extends Command<T> {

  @Override
  public void setPropertyForObject(T resultObject, Field field, Object property) {
//    if (!field.isAnnotationPresent(KeySet.class)) {
//      throw new RuntimeException("Iterable field " + field.getName() + " of Model must be annotated by @KeySet");
//    }
    Set<Object> value = new HashSet<>();
    Class converter = null;
    // если указан ковертер, получаем класс конвертера для преобразования элементов множества из строки
    if (field.isAnnotationPresent(Converter.class)) {
      converter = field.getAnnotation(Converter.class).value();
    }
    if (null == converter) {
      Set<Blob> inputSet = new HashSet<>((List<Blob>) property);
      for (Blob b : inputSet) {
        try {
          // десериализуем элемент множества
          ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(b.getBytes());
          ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
          value.add(objectInputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
          e.printStackTrace();
        }
      }
    } else {
      Set<String> inputSet = new HashSet<>((List<String>) property);
      for (String s : inputSet) {
        try {
          // конвертируем из строки
          final online.draughts.rus.server.domain.converter.Converter converterInstance = (online.draughts.rus.server.domain.converter.Converter) converter.newInstance();
          value.add(converterInstance.convertFrom(s));
        } catch (InstantiationException | IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }
    setProp(resultObject, field.getName(), value);
  }

  @Override
  public void updateFieldOfDatastoreEntity(Entity entity, Field field, Object value) {
    try {
      // если аннотация ковертера присутсвует, тогда конвертирем элементы множества в строку иначе сериализуем
      Class converter = null;
      if (field.isAnnotationPresent(Converter.class)) {
        converter = field.getAnnotation(Converter.class).value();
      }
      if (null == converter) {
        // если нет конвертера сохраняем множество в массив болобов
        Set<Blob> resultSet = new HashSet<>();
        for (Object obj : (Set) value) {
          // сериализуем
          ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
          ObjectOutputStream objectInputStream = new ObjectOutputStream(byteArrayOutputStream);
          objectInputStream.writeObject(obj);
          // записываем в качестве блоба
          resultSet.add(new Blob(byteArrayOutputStream.toByteArray()));
        }
        super.updateFieldOfDatastoreEntity(entity, field, resultSet);
      } else {
        // иначе конвертируем в массив строк
        Set<String> resultSet = new HashSet<>();
        for (Object obj : (Set) value) {
          final online.draughts.rus.server.domain.converter.Converter converterInstance = (online.draughts.rus.server.domain.converter.Converter) converter.newInstance();
          resultSet.add(converterInstance.convertTo(obj));
        }
        super.updateFieldOfDatastoreEntity(entity, field, resultSet);
      }
    } catch (IOException | InstantiationException | IllegalAccessException e) {
      logger.error(e.getMessage(), e);
    }
  }
}
