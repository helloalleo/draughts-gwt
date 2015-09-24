package online.shashki.rus.shared.model;

import com.google.gwt.user.client.rpc.GwtTransient;
import online.shashki.rus.shared.model.PersistableObject;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;

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

  @GwtTransient
  @JsonIgnore
  @Version
  private Integer version;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public Integer getVersion() {
    return version;
  }
}
