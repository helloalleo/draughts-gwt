//
//  ThemeFactory.java
//  emoji-gwt
//
//  Created by William Shakour (billy1380) on 29 May 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package online.draughts.rus.client.resources.emoji;

/**
 * @author William Shakour (billy1380)
 */
public class ThemeFactory {
  public static Emojis getTheme(String themeName) {
    switch (themeName) {
      case "apple":
        return Apple.INSTANCE;
      default:
        return null;
    }
  }
}
