package com.dhuy.dragonbot.modules;

import com.dhuy.dragonbot.global.ScreenshotCache;

public class Cavebot {
  ScreenshotCache screenshotCache = ScreenshotCache.getInstance();

  public Cavebot() {}

  public void execute() {
    try {
      String screenshotKey = screenshotCache.getCurrentScreenshotKey();

      if (screenshotKey != null) {
        // INCLUDE THE CAVEBOT CODE IN HERE
      }

      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
