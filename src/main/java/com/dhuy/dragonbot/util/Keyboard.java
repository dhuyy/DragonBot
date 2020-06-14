package com.dhuy.dragonbot.util;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import com.dhuy.dragonbot.global.Log;

public class Keyboard {
  private Log log;
  private Robot robot;
  private KeyEventMapping keyEventMap;

  public Keyboard() {
    log = Log.getInstance();

    try {
      robot = new Robot();
      keyEventMap = new KeyEventMapping();
    } catch (AWTException e) {
      log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e);
    }
  }

  public void type(String stringKey) {
    int keyCode = getKeyCode(stringKey);

    robot.keyPress(keyCode);
    robot.keyRelease(keyCode);
    robot.delay(250);
  }

  public void writeWord(String word) {
    for (int i = 0; i < word.length(); i++) {
      int keyCode = keyEventMap.getKeyCodeFromChar(word.charAt(i));

      if (keyCode != KeyEvent.CHAR_UNDEFINED) {
        robot.keyPress(keyCode);
        robot.keyRelease(keyCode);
        robot.delay(50);
      }
    }
  }

  private int getKeyCode(String fnKey) {
    return keyEventMap.getKeyCode(fnKey);
  }

  public void pressShiftAndBackButton() {
    robot.keyPress(KeyEvent.VK_SHIFT);
    robot.mousePress(InputEvent.BUTTON3_MASK);
    robot.mouseRelease(InputEvent.BUTTON3_MASK);
    robot.keyRelease(KeyEvent.VK_SHIFT);
  }
}
