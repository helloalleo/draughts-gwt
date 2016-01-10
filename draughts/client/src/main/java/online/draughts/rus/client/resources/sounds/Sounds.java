package online.draughts.rus.client.resources.sounds;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 22.03.14
 * Time: 14:28
 */
public interface Sounds extends ClientBundle {
  @Source("snd/new_message.m4a")
  DataResource newMessageSound();

  @Source("snd/invite.m4a")
  DataResource inviteSound();
}
