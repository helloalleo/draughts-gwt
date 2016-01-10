package online.draughts.rus.client.util;

import com.google.gwt.media.client.Audio;
import com.google.gwt.resources.client.DataResource;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 27.12.15
 * Time: 11:19
 */
public class AudioUtil {

  public static void playSound(DataResource soundResource) {
    Audio inviteSound;
    inviteSound = Audio.createIfSupported();
    inviteSound.setSrc(soundResource.getSafeUri().asString());
    inviteSound.play();
  }
}
