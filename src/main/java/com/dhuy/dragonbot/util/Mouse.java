package com.dhuy.dragonbot.util;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

public class Mouse {
  private Robot robot;

  public Mouse() {
    try {
      robot = new Robot();
    } catch (AWTException e) {
      e.printStackTrace();
    }
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
