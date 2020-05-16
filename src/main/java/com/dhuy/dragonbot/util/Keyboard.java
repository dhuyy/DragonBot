package com.dhuy.dragonbot.util;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class Keyboard {
  private Robot robot;
  private KeyEventMapping keyEventMap;

  public Keyboard() {
    try {
      robot = new Robot();
      keyEventMap = new KeyEventMapping();
    } catch (AWTException e) {
      e.printStackTrace();
    }
  }

  public void type(String word) {
    for (int i = 0; i < word.length(); i++) {
      int keyCode = keyEventMap.getKeyEvent(word.charAt(i));

      if (keyCode != KeyEvent.CHAR_UNDEFINED) {
        robot.keyPress(keyCode);
        robot.keyRelease(keyCode);
        robot.delay(50);
      }
    }
  }
}
