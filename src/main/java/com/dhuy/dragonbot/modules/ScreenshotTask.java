package com.dhuy.dragonbot.modules;

public class ScreenshotTask implements Runnable {
  private Screenshot screenshotModule;

  public ScreenshotTask() {
    screenshotModule = new Screenshot();
  }

  @Override
  public void run() {
    while (true) {
      screenshotModule.execute();
    }
  }
}
