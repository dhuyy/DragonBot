package com.dhuy.dragonbot.global;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Store {
  private static Store instance = new Store();

  public static final int WAYPOINT_BLOCK_SIZE = 14;
  public static final int WAYPOINT_MATCH_PIXEL_TO_CENTER_CROSS = WAYPOINT_BLOCK_SIZE / 2 - 1;
  public static final int WAYPOINT_CENTER_CROSS_TO_MATCH_PIXEL = WAYPOINT_BLOCK_SIZE / 2 - 3;
  public static final int MAP_SPACING_FROM_BASE_TO_GOAL_WAYPOINT = WAYPOINT_BLOCK_SIZE;
  public static final int MAP_INNER_WIDTH = 106;
  public static final int MAP_INNER_HEIGHT = 109;
  public static final int MAP_TOP_PADDING = 5;
  public static final int MAP_RIGHT_PADDING = 61;
  public static final String TIBIA_SCREENSHOT_RELATIVE_DIRECTORY =
      "\\AppData\\Local\\Tibia\\packages\\Tibia\\screenshots";
  public static final String MINIMAP_CROSS_ZOOM_4X_PATH = "images\\minimapCross_4x.png";

  private String homeDirectoryPath;
  private String tibiaScreenshotAbsoluteDirectory;
  private int windowsTitleBarHeight;
  private int minimapLeftSpace;
  private Rectangle minimapArea;
  private BufferedImage minimapCross;
  private String scriptName;
  private String characterName;

  private Store() {
    homeDirectoryPath = System.getProperty("user.home");
    tibiaScreenshotAbsoluteDirectory =
        homeDirectoryPath.concat(Store.TIBIA_SCREENSHOT_RELATIVE_DIRECTORY);
    windowsTitleBarHeight = 0;
    minimapLeftSpace = 0;
    minimapArea = null;
    minimapCross = null;
    scriptName = null;
    characterName = null;
  }

  public static Store getInstance() {
    if (instance == null) {
      synchronized (Store.class) {
        if (instance == null) {
          instance = new Store();
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

  public int getWindowsTitleBarHeight() {
    return windowsTitleBarHeight;
  }

  public void setWindowsTitleBarHeight(int windowsTitleBarHeight) {
    this.windowsTitleBarHeight = windowsTitleBarHeight;
  }

  public int getMinimapLeftSpace() {
    return minimapLeftSpace;
  }

  public void setMinimapLeftSpace(int minimapLeftSpace) {
    this.minimapLeftSpace = minimapLeftSpace;
  }

  public Rectangle getMinimapArea() {
    return minimapArea;
  }

  public void setMinimapArea(Rectangle minimapArea) {
    this.minimapArea = minimapArea;
  }

  public BufferedImage getMinimapCross() {
    return minimapCross;
  }

  public void setMinimapCross(BufferedImage minimapCross) {
    this.minimapCross = minimapCross;
  }

  public String getScriptName() {
    return scriptName;
  }

  public void setScriptName(String scriptName) {
    this.scriptName = scriptName;
  }

  public String getCharacterName() {
    return characterName;
  }

  public void setCharacterName(String characterName) {
    this.characterName = characterName;
  }
}
