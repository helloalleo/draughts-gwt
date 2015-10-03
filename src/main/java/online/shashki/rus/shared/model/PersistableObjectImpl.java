package online.shashki.rus.shared.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 15.11.14
 * Time: 16:03
 */
@MappedSuperclass
public class PersistableObjectImpl implements PersistableObject {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

//  @GwtTransient
//  @JsonIgnore
//  @com.fasterxml.jackson.annotation.JsonIgnore
//  @Version
//  private Integer version;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

//  @Override
//  public Integer getVersion() {
//    return version;
//  }
}
