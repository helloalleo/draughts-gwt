package online.draughts.rus.shared.channel;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 19.11.15
 * Time: 11:51
 */
public class Chunk implements Serializable {

  // сколько всего частей. на сколько частей разбито сообщение
  private int chunksInMessage;
  // номер чанка в последовательности сообщений
  private int number;
  // само сообщение
  private String message;

  public Chunk(int chunksInMessage, int number, String message) {
    this.chunksInMessage = chunksInMessage;
    this.number = number;
    this.message = message;
  }

  public Chunk() {
  }

  public int getChunksInMessage() {
    return chunksInMessage;
  }

  public Chunk setChunksInMessage(int chunksInMessage) {
    this.chunksInMessage = chunksInMessage;
    return this;
  }

  public int getNumber() {
    return number;
  }

  public Chunk setNumber(int number) {
    this.number = number;
    return this;
  }

  public String getMessage() {
    return message;
  }

  public Chunk setMessage(String message) {
    this.message = message;
    return this;
  }
}
