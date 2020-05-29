package com.dhuy.dragonbot.modules;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import com.dhuy.dragonbot.global.Log;
import com.dhuy.dragonbot.global.Store;
import com.dhuy.dragonbot.util.Character;
import com.dhuy.dragonbot.util.ImageProcessor;
import com.dhuy.dragonbot.util.Keyboard;
import com.dhuy.dragonbot.util.Mouse;

public class Cavebot {
  private Store store = Store.getInstance();

  private Character character;
  private Keyboard keyboard;
  private Mouse mouse;
  private ImageProcessor imageProcessor;
  private Screenshot screenshotModule;
  private Looting looting;
  private Log log;

  public Cavebot() {
    character = new Character();
    mouse = new Mouse();
    keyboard = new Keyboard();
    imageProcessor = new ImageProcessor();
    screenshotModule = new Screenshot();
    looting = new Looting();
    log = Log.getInstance();
  }

  public long execute() {
    Rectangle minimapArea = store.getMinimapArea();

    BufferedImage currentCross = store.getMinimapCross();
    BufferedImage baseImage =
        store.getWaypointList().get(store.getCurrentWaypointIndex()).getBaseImage();
    BufferedImage goalImage =
        store.getWaypointList().get(store.getCurrentWaypointIndex()).getGoalImage();

    keyboard.type("ESC");

    BufferedImage currentScreenshot = screenshotModule.execute(this);

    BufferedImage currentMinimap = currentScreenshot.getSubimage(minimapArea.x, minimapArea.y,
        minimapArea.width, minimapArea.height);

    int[] waypoint = imageProcessor.findSubimage(currentMinimap, baseImage);

    BufferedImage currentGoalImage =
        currentMinimap.getSubimage(waypoint[0] + Store.MAP_SPACING_FROM_BASE_TO_GOAL_WAYPOINT,
            waypoint[1], Store.WAYPOINT_BLOCK_SIZE, Store.WAYPOINT_BLOCK_SIZE);

    int[] initialPoint = imageProcessor.findSubimage(currentMinimap, currentCross);
    int[] finalPoint = imageProcessor.findSubimage(currentMinimap, currentGoalImage);

    double distanceToReachWaypoint = Math.sqrt(
        Math.pow(finalPoint[0] - (initialPoint[0] - Store.WAYPOINT_CENTER_CROSS_TO_MATCH_PIXEL), 2)
            + Math.pow(
                finalPoint[1] - (initialPoint[1] - Store.WAYPOINT_CENTER_CROSS_TO_MATCH_PIXEL), 2));
    long walkingSpeedInMilliseconds = character
        .getWalkingSpeedInMilliseconds(Store.GRASS_TILE_SPEED_WALKING, distanceToReachWaypoint);

    double hasGoalImagesMatchedPercentage =
        imageProcessor.compareImages(goalImage, currentGoalImage) * 100;

    if (hasGoalImagesMatchedPercentage <= 1) {
      log.getLogger().info(log.getMessage(this, "Chegou no waypoint"));

      int currentWaypointIndex = store.getCurrentWaypointIndex();
      int nextWaypointIndex = currentWaypointIndex + 1;

      if (nextWaypointIndex == store.getWaypointList().size()) {
        store.setCurrentWaypointIndex(0);
      } else {
        store.setCurrentWaypointIndex(nextWaypointIndex);
      }
    } else {
      log.getLogger().info(log.getMessage(this, "Ainda não chegou no waypoint"));

      looting.execute();

      mouse.clickOn(
          waypoint[0] + Store.MAP_SPACING_FROM_BASE_TO_GOAL_WAYPOINT
              + Store.WAYPOINT_MATCH_PIXEL_TO_CENTER_CROSS + store.getMinimapLeftSpace(),
          waypoint[1] + Store.WAYPOINT_MATCH_PIXEL_TO_CENTER_CROSS + Store.MAP_TOP_PADDING
              + store.getWindowsTitleBarHeight());
    }

    return walkingSpeedInMilliseconds;
  }
}
