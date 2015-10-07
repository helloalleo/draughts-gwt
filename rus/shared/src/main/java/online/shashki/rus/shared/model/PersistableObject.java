package online.shashki.rus.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 12.12.14
 * Time: 10:20
 */
public interface PersistableObject extends IsSerializable {

  Long getId();

  void setId(Long id);

//  Integer getVersion();
}
