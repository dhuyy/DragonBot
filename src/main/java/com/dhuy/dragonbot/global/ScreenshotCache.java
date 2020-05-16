package com.dhuy.dragonbot.global;

import java.awt.image.BufferedImage;

public class ScreenshotCache {
  private static ScreenshotCache instance = new ScreenshotCache();

  String currentScreenshotKey;
  BufferedImage currentScreenshotValue;

  private ScreenshotCache() {
    currentScreenshotKey = "<NOT_LOADED>";
    currentScreenshotValue = null;
  }

  public static ScreenshotCache getInstance() {
    if (instance == null) {
      synchronized (ScreenshotCache.class) {
        if (instance == null) {
          instance = new ScreenshotCache();
        }
      }
    }

    return instance;
  }

  public String getCurrentScreenshotKey() {
    return currentScreenshotKey;
  }

  public void setCurrentScreenshotKey(String currentScreenshotKey) {
    this.currentScreenshotKey = currentScreenshotKey;
  }

  public BufferedImage getCurrentScreenshotValue() {
    return currentScreenshotValue;
  }

  public void setCurrentScreenshotValue(BufferedImage currentScreenshotValue) {
    this.currentScreenshotValue = currentScreenshotValue;
  }
}
