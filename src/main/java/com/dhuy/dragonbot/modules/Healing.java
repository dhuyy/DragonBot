package com.dhuy.dragonbot.modules;

import com.dhuy.dragonbot.global.ScreenshotCache;

public class Healing {
  ScreenshotCache screenshotCache = ScreenshotCache.getInstance();

  public Healing() {}

  public void execute() {
    try {
      String screenshotKey = screenshotCache.getCurrentScreenshotKey();

      if (screenshotKey != null) {
        // INCLUDE THE HEALING CODE IN HERE
      }

      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
