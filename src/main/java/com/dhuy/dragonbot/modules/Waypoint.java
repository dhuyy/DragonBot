package com.dhuy.dragonbot.modules;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import com.dhuy.dragonbot.global.Database;
import com.dhuy.dragonbot.global.ScreenshotCache;
import com.dhuy.dragonbot.global.Store;
import com.dhuy.dragonbot.util.ImageProcessor;

public class Waypoint {
  private Store store = Store.getInstance();
  private ScreenshotCache screenshotCache = ScreenshotCache.getInstance();

  private ImageProcessor imageProcessor;
  private Database database;

  public Waypoint() {
    imageProcessor = new ImageProcessor();

    database = Database.getInstance();
  }

  public void captureWaypoint() {
    Rectangle minimapArea = store.getMinimapArea();

    BufferedImage currentScreenshot = screenshotCache.getCurrentScreenshotValue();
    BufferedImage currentMinimap = currentScreenshot.getSubimage(minimapArea.x, minimapArea.y,
        minimapArea.width, minimapArea.height);
    BufferedImage currentCross = store.getMinimapCross();

    int[] crossCoord = imageProcessor.findSubimage(currentMinimap, currentCross);

    BufferedImage goalImage =
        currentMinimap.getSubimage(crossCoord[0] - Store.WAYPOINT_CENTER_CROSS_TO_MATCH_PIXEL,
            crossCoord[1] - Store.WAYPOINT_CENTER_CROSS_TO_MATCH_PIXEL, Store.WAYPOINT_BLOCK_SIZE,
            Store.WAYPOINT_BLOCK_SIZE);
    BufferedImage baseImage = currentMinimap.getSubimage(
        crossCoord[0] - Store.WAYPOINT_CENTER_CROSS_TO_MATCH_PIXEL
            - Store.MAP_SPACING_FROM_BASE_TO_GOAL_WAYPOINT,
        crossCoord[1] - Store.WAYPOINT_CENTER_CROSS_TO_MATCH_PIXEL, Store.WAYPOINT_BLOCK_SIZE,
        Store.WAYPOINT_BLOCK_SIZE);

    database.insertWaypoint(baseImage, goalImage);
  }
}
