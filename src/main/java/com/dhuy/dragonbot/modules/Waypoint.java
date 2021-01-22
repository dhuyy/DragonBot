package com.dhuy.dragonbot.modules;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import com.dhuy.dragonbot.global.Database;
import com.dhuy.dragonbot.global.Store;
import com.dhuy.dragonbot.util.ImageProcessor;

public class Waypoint {
  private Store store;
  private ImageProcessor imageProcessor;
  private Database database;
  private Screenshot screenshotModule;

  public Waypoint() {
    store = Store.getInstance();
    imageProcessor = new ImageProcessor();
    screenshotModule = new Screenshot();

    database = Database.getInstance();
  }

  public void captureWaypoint() {
    Rectangle minimapArea = store.getMinimapArea();

    BufferedImage currentScreenshot = screenshotModule.execute(this);

    BufferedImage currentMinimap = currentScreenshot.getSubimage(minimapArea.x, minimapArea.y,
        minimapArea.width, minimapArea.height);
    BufferedImage currentCross = store.getMinimapCross();

    int[] crossCoord = imageProcessor.findSubimage(currentMinimap, currentCross);

    BufferedImage baseImage = null;

    switch (store.getCurrentDirection()) {
      case 1:
        baseImage =
            currentMinimap.getSubimage(crossCoord[0] - Store.WAYPOINT_CENTER_CROSS_TO_MATCH_PIXEL,
                crossCoord[1] - Store.WAYPOINT_CENTER_CROSS_TO_MATCH_PIXEL
                    - Store.MAP_SPACING_FROM_BASE_TO_GOAL_WAYPOINT,
                Store.WAYPOINT_BLOCK_SIZE, Store.WAYPOINT_BLOCK_SIZE);
        break;
      case 2:
        baseImage = currentMinimap.getSubimage(
            crossCoord[0] + Store.WAYPOINT_CENTER_CROSS_TO_MATCH_PIXEL
                + Store.MINIMAP_CROSS_ZOOM_WIDTH,
            crossCoord[1] - Store.WAYPOINT_CENTER_CROSS_TO_MATCH_PIXEL, Store.WAYPOINT_BLOCK_SIZE,
            Store.WAYPOINT_BLOCK_SIZE);
        break;
      case 3:
        baseImage =
            currentMinimap.getSubimage(crossCoord[0] - Store.WAYPOINT_CENTER_CROSS_TO_MATCH_PIXEL,
                crossCoord[1] + Store.WAYPOINT_CENTER_CROSS_TO_MATCH_PIXEL
                    + Store.MINIMAP_CROSS_ZOOM_HEIGHT,
                Store.WAYPOINT_BLOCK_SIZE, Store.WAYPOINT_BLOCK_SIZE);
        break;
      case 4:
        baseImage = currentMinimap.getSubimage(
            crossCoord[0] - Store.WAYPOINT_CENTER_CROSS_TO_MATCH_PIXEL
                - Store.MAP_SPACING_FROM_BASE_TO_GOAL_WAYPOINT,
            crossCoord[1] - Store.WAYPOINT_CENTER_CROSS_TO_MATCH_PIXEL, Store.WAYPOINT_BLOCK_SIZE,
            Store.WAYPOINT_BLOCK_SIZE);
        break;
    }

    BufferedImage goalImage =
        currentMinimap.getSubimage(crossCoord[0] - Store.WAYPOINT_CENTER_CROSS_TO_MATCH_PIXEL,
            crossCoord[1] - Store.WAYPOINT_CENTER_CROSS_TO_MATCH_PIXEL, Store.WAYPOINT_BLOCK_SIZE,
            Store.WAYPOINT_BLOCK_SIZE);

    database.insertWaypoint(0, store.getCurrentDirection(), baseImage, goalImage, "NOT_PRESENT");
  }

  public void captureRopeWaypoint() {
    database.insertWaypoint(1, store.getCurrentDirection(), null, null, "NOT_PRESENT");
  }

  public void captureShovelWaypoint() {
    database.insertWaypoint(2, store.getCurrentDirection(), null, null, "NOT_PRESENT");
  }

  public void captureLadderWaypoint() {
    database.insertWaypoint(3, store.getCurrentDirection(), null, null, "NOT_PRESENT");
  }

  public void captureHoleRampWaypoint() {
    database.insertWaypoint(4, store.getCurrentDirection(), null, null, "NOT_PRESENT");
  }

  public void captureTalkWaypoint(String phrase) {
    database.insertWaypoint(5, store.getCurrentDirection(), null, null, phrase);
  }

  public void captureSequentialClicksWaypoint(String sequence) {
    database.insertWaypoint(6, store.getCurrentDirection(), null, null, sequence);
  }
}
