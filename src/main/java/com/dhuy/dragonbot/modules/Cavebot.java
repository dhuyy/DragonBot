package com.dhuy.dragonbot.modules;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.dhuy.dragonbot.global.Database;
import com.dhuy.dragonbot.global.ScreenshotCache;
import com.dhuy.dragonbot.global.Store;
import com.dhuy.dragonbot.util.Character;
import com.dhuy.dragonbot.util.ImageProcessor;
import com.dhuy.dragonbot.util.Keyboard;
import com.dhuy.dragonbot.util.Mouse;

public class Cavebot {
  private Store store = Store.getInstance();
  private Database database = Database.getInstance();
  private ScreenshotCache screenshotCache = ScreenshotCache.getInstance();

  private Character character;
  private Mouse mouse;
  private Keyboard keyboard;
  private ImageProcessor imageProcessor;
  private Screenshot screenshotModule;

  public Cavebot() {
    character = new Character();
    mouse = new Mouse();
    keyboard = new Keyboard();
    imageProcessor = new ImageProcessor();
    screenshotModule = new Screenshot();
  }

  public void execute() {
    try {
      Rectangle minimapArea = store.getMinimapArea();

      ResultSet resultSet = database.getAllWaypoints();

      while (resultSet.next()) {
        Blob baseImageBlob = resultSet.getBlob("BASE_IMAGE");
        Blob goalImageBlob = resultSet.getBlob("GOAL_IMAGE");

        BufferedImage currentCross = store.getMinimapCross();
        BufferedImage baseImage = imageProcessor
            .getBufferedImageFromByteArray(baseImageBlob.getBytes(1, (int) baseImageBlob.length()));
        BufferedImage goalImage = imageProcessor
            .getBufferedImageFromByteArray(goalImageBlob.getBytes(1, (int) goalImageBlob.length()));

        boolean hasReachedWaypoint = false;
        while (!hasReachedWaypoint) {
          keyboard.type("ESC");

          screenshotModule.execute(this.getClass().getName());

          BufferedImage currentMinimap = screenshotCache.getCurrentScreenshotValue()
              .getSubimage(minimapArea.x, minimapArea.y, minimapArea.width, minimapArea.height);

          int[] waypoint = imageProcessor.findSubimage(currentMinimap, baseImage);

          BufferedImage currentGoalImage =
              currentMinimap.getSubimage(waypoint[0] + Store.MAP_SPACING_FROM_BASE_TO_GOAL_WAYPOINT,
                  waypoint[1], Store.WAYPOINT_BLOCK_SIZE, Store.WAYPOINT_BLOCK_SIZE);

          double hasGoalImagesMatchedPercentage =
              imageProcessor.compareImages(goalImage, currentGoalImage) * 100;

          if (hasGoalImagesMatchedPercentage <= 1) {
            hasReachedWaypoint = true;
          } else {
            int[] initialPoint = imageProcessor.findSubimage(currentMinimap, currentCross);
            int[] finalPoint = imageProcessor.findSubimage(currentMinimap, currentGoalImage);

            double distanceToReachWaypoint = Math.sqrt(Math.pow(
                finalPoint[0] - (initialPoint[0] - Store.WAYPOINT_CENTER_CROSS_TO_MATCH_PIXEL), 2)
                + Math.pow(
                    finalPoint[1] - (initialPoint[1] - Store.WAYPOINT_CENTER_CROSS_TO_MATCH_PIXEL),
                    2));
            long walkingSpeedInMilliseconds = character.getWalkingSpeedInMilliseconds(
                Store.GRASS_TILE_SPEED_WALKING, distanceToReachWaypoint);

            mouse.clickOn(
                waypoint[0] + Store.MAP_SPACING_FROM_BASE_TO_GOAL_WAYPOINT
                    + Store.WAYPOINT_MATCH_PIXEL_TO_CENTER_CROSS + store.getMinimapLeftSpace(),
                waypoint[1] + Store.WAYPOINT_MATCH_PIXEL_TO_CENTER_CROSS + Store.MAP_TOP_PADDING
                    + store.getWindowsTitleBarHeight());

            Thread.sleep(walkingSpeedInMilliseconds);
          }
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
