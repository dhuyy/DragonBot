package com.dhuy.dragonbot.global;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import com.dhuy.dragonbot.model.Waypoint;

public class Store {
  private static Store instance = new Store();

  public static final int CHARACTER_SPEED_BASE = 270;
  public static final int GRASS_TILE_SPEED_WALKING = 150;
  public static final int WAYPOINT_BLOCK_SIZE = 16;
  public static final int WAYPOINT_MATCH_PIXEL_TO_CENTER_CROSS = WAYPOINT_BLOCK_SIZE / 2 - 1;
  public static final int WAYPOINT_CENTER_CROSS_TO_MATCH_PIXEL = WAYPOINT_BLOCK_SIZE / 2 - 3;
  public static final int MAP_SPACING_FROM_BASE_TO_GOAL_WAYPOINT = WAYPOINT_BLOCK_SIZE;
  public static final int MAP_INNER_WIDTH = 106;
  public static final int BATTLE_INNER_WIDTH = 176;
  public static final int MAP_INNER_HEIGHT = 109;
  public static final int MAP_TOP_PADDING = 5;
  public static final int MAP_RIGHT_PADDING = 61;
  public static final String TIBIA_SCREENSHOT_RELATIVE_DIRECTORY =
      "\\AppData\\Local\\Tibia\\packages\\Tibia\\screenshots";
  public static final String MINIMAP_CROSS_ZOOM_4X_PATH = "images\\minimapCross_4x.png";
  public static final String BATTLE_LIST_CROP_PATH = "images\\battleListCrop.png";
  public static final int BATTLE_MATCH_PIXEL_TO_MONSTER_LIFE_BAR_X = 27;
  public static final int BATTLE_MATCH_PIXEL_TO_MONSTER_LIFE_BAR_Y = 32;
  public static final int BATTLE_MATCH_PIXEL_TO_MONSTER_BEING_ATTACKED_X = 4;
  public static final int BATTLE_MATCH_PIXEL_TO_MONSTER_BEING_ATTACKED_Y = 15;
  public static final int BATTLE_PIXEL_RGB_WITHOUT_MONSTER_VISIBLE_COLOR = -12303292;
  public static final int BATTLE_PIXEL_RGB_MONSTER_BEING_ATTACKED_COLOR = -65536;

  private LinkedList<Waypoint> waypointList;
  private int currentWaypointIndex;
  private String homeDirectoryPath;
  private String tibiaScreenshotAbsoluteDirectory;
  private String scriptsDirectory;
  private int windowsTitleBarHeight;
  private int minimapLeftSpace;
  private int battleLeftSpace;
  private Rectangle minimapArea;
  private Rectangle battleWindowArea;
  private Rectangle characterPositionArea;
  private BufferedImage minimapCross;
  private BufferedImage battleListCrop;
  private String scriptName;
  private String characterName;
  private int characterLevel;
  private long startTimeWaypoint;
  private long intervalToReachWaypoint;

  private Store() {
    waypointList = new LinkedList<Waypoint>();
    currentWaypointIndex = 0;
    homeDirectoryPath = System.getProperty("user.home");
    tibiaScreenshotAbsoluteDirectory =
        homeDirectoryPath.concat(Store.TIBIA_SCREENSHOT_RELATIVE_DIRECTORY);
    scriptsDirectory = "scripts\\";
    windowsTitleBarHeight = 0;
    minimapLeftSpace = 0;
    battleLeftSpace = 0;
    minimapArea = null;
    battleWindowArea = null;
    characterPositionArea = null;
    minimapCross = null;
    battleListCrop = null;
    scriptName = null;
    characterName = null;
    characterLevel = 0;
    startTimeWaypoint = 0;
    intervalToReachWaypoint = -1;
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

  public LinkedList<Waypoint> getWaypointList() {
    return waypointList;
  }

  public void setWaypointList(LinkedList<Waypoint> waypointList) {
    this.waypointList = waypointList;
  }

  public int getCurrentWaypointIndex() {
    return currentWaypointIndex;
  }

  public void setCurrentWaypointIndex(int currentWaypointIndex) {
    this.currentWaypointIndex = currentWaypointIndex;
  }

  public String getHomeDirectoryPath() {
    return homeDirectoryPath;
  }

  public String getTibiaScreenshotAbsoluteDirectory() {
    return tibiaScreenshotAbsoluteDirectory;
  }

  public String getScriptsDirectory() {
    return scriptsDirectory;
  }

  public void setScriptsDirectory(String scriptsDirectory) {
    this.scriptsDirectory = scriptsDirectory;
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

  public int getBattleLeftSpace() {
    return battleLeftSpace;
  }

  public void setBattleLeftSpace(int battleLeftSpace) {
    this.battleLeftSpace = battleLeftSpace;
  }

  public Rectangle getMinimapArea() {
    return minimapArea;
  }

  public void setMinimapArea(Rectangle minimapArea) {
    this.minimapArea = minimapArea;
  }

  public Rectangle getBattleWindowArea() {
    return battleWindowArea;
  }

  public void setBattleWindowArea(Rectangle battleWindowArea) {
    this.battleWindowArea = battleWindowArea;
  }

  public Rectangle getCharacterPositionArea() {
    return characterPositionArea;
  }

  public void setCharacterPositionArea(Rectangle characterPositionArea) {
    this.characterPositionArea = characterPositionArea;
  }

  public BufferedImage getMinimapCross() {
    return minimapCross;
  }

  public void setMinimapCross(BufferedImage minimapCross) {
    this.minimapCross = minimapCross;
  }

  public BufferedImage getBattleListCrop() {
    return battleListCrop;
  }

  public void setBattleListCrop(BufferedImage battleListCrop) {
    this.battleListCrop = battleListCrop;
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

  public int getCharacterLevel() {
    return characterLevel;
  }

  public void setCharacterLevel(int characterLevel) {
    this.characterLevel = characterLevel;
  }

  public long getStartTimeWaypoint() {
    return startTimeWaypoint;
  }

  public void setStartTimeWaypoint(long startTimeWaypoint) {
    this.startTimeWaypoint = startTimeWaypoint;
  }

  public long getIntervalToReachWaypoint() {
    return intervalToReachWaypoint;
  }

  public void setIntervalToReachWaypoint(long intervalToReachWaypoint) {
    this.intervalToReachWaypoint = intervalToReachWaypoint;
  }
}
