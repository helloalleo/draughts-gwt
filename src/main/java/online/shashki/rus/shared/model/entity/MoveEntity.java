package online.shashki.rus.shared.model.entity;

import online.shashki.rus.shared.model.Move;
import online.shashki.rus.shared.model.PersistableObject;
import online.shashki.rus.shashki.Square;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.09.15
 * Time: 21:45
 */
@Entity
@Table(name = "move")
class MoveEntity extends PersistableObjectImpl implements Move, PersistableObject {
  private int number;
  private boolean first; // первый ход в паре ходов. Например, ee-aa в ee-aa bb-cc
  private boolean canceled; // ход был отменен (записываем все ходы)

  @OneToOne
  @JoinColumn(name="move_id", nullable=false, updatable=false)
  private GameMessageEntity gameMessage;

  @Column(name = "start_pos")
  private String startSquare;
  @Column(name = "end_pos")
  private String endSquare;
  @Column(name = "taken_pos")
  private String takenSquare;

  @Column(name = "move_flags")
  @ElementCollection
  private Set<MoveFlags> moveFlags = new HashSet<>();

  @Override
  public int getNumber() {
    return number;
  }

  @Override
  public MoveEntity setNumber(int number) {
    this.number = number;
    return this;
  }

  @Override
  public boolean isFirst() {
    return first;
  }

  @Override
  public MoveEntity setFirst(boolean first) {
    this.first = first;
    return this;
  }

  @Override
  public String getStartPos() {
    return startSquare;
  }

  public MoveEntity setStartSquare(Square startSquare) {
    this.startSquare = startSquare.toSend();
    return this;
  }

  @Override
  public String getEndPos() {
    return endSquare;
  }

  public MoveEntity setEndSquare(Square endSquare) {
    this.endSquare = endSquare.toSend();
    return this;
  }

  @Override
  public String getTakenPos() {
    return takenSquare;
  }

  public MoveEntity setTakenSquare(Square takenSquare) {
    this.takenSquare = takenSquare.toSend();
    return this;
  }

  @Override
  public Set<MoveFlags> getMoveFlags() {
    return moveFlags;
  }

  public MoveEntity setMoveFlags(Set<MoveFlags> moveFlags) {
    this.moveFlags = moveFlags;
    return this;
  }

  public GameMessageEntity getGameMessage() {
    return gameMessage;
  }

  public MoveEntity setGameMessage(GameMessageEntity gameMessage) {
    this.gameMessage = gameMessage;
    return this;
  }
}
