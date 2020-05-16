package com.dhuy.dragonbot.global;

public class Constants {
  private static Constants instance = new Constants();

  private static final String TIBIA_SCREENSHOT_RELATIVE_DIRECTORY =
      "\\AppData\\Local\\Tibia\\packages\\Tibia\\screenshots";

  private String homeDirectoryPath;
  private String tibiaScreenshotAbsoluteDirectory;

  private Constants() {
    homeDirectoryPath = System.getProperty("user.home");
    tibiaScreenshotAbsoluteDirectory =
        homeDirectoryPath.concat(TIBIA_SCREENSHOT_RELATIVE_DIRECTORY);
  }

  public static Constants getInstance() {
    if (instance == null) {
      synchronized (Constants.class) {
        if (instance == null) {
          instance = new Constants();
        }
      }
    }

    return instance;
  }

  public String getHomeDirectoryPath() {
    return homeDirectoryPath;
  }

  public String getTibiaScreenshotAbsoluteDirectory() {
    return tibiaScreenshotAbsoluteDirectory;
  }
}
