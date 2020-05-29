package com.dhuy.dragonbot.util;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.logging.Level;
import com.dhuy.dragonbot.global.Log;

public class Mouse {
  private Log log;
  private Robot robot;

  public Mouse() {
    log = Log.getInstance();

    try {
      robot = new Robot();
    } catch (AWTException e) {
      log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e.getStackTrace());
    }
  }

  public void move(int x, int y) {
    robot.mouseMove(x, y);
  }

  public void clickOn(int x, int y) {
    robot.mouseMove(x, y);
    robot.mousePress(InputEvent.BUTTON1_MASK);
    robot.mouseRelease(InputEvent.BUTTON1_MASK);
  }

  public void clickOn(int x, int y, boolean backButton) {
    robot.mouseMove(x, y);
    robot.mousePress(InputEvent.BUTTON3_MASK);
    robot.mouseRelease(InputEvent.BUTTON3_MASK);
  }
}
