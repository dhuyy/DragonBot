package com.dhuy.dragonbot.modules;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import com.dhuy.dragonbot.global.Database;
import com.dhuy.dragonbot.global.KeyboardHook;
import com.dhuy.dragonbot.global.Log;
import com.dhuy.dragonbot.global.Store;
import com.dhuy.dragonbot.model.Waypoint;
import com.dhuy.dragonbot.util.ApplicationWindow;
import com.dhuy.dragonbot.util.AreaSelector;
import com.dhuy.dragonbot.util.ImageProcessor;

public class Setup {
  private Store store = Store.getInstance();
  private Database database = Database.getInstance();
  private KeyboardHook keyboardHook = KeyboardHook.getInstance();

  private AreaSelector areaSelector;
  private ApplicationWindow appWindow;
  private Screenshot screenshotModule;
  private ImageProcessor imageProcessor;
  private Log log;

  public Setup() {
    areaSelector = new AreaSelector();
    appWindow = new ApplicationWindow();
    screenshotModule = new Screenshot();
    imageProcessor = new ImageProcessor();
    log = Log.getInstance();
  }

  private void calculateMinimapAndBattleLeftSpace() {
    int[] screenSize = appWindow.getScreenSize();

    store.setMinimapLeftSpace(screenSize[0] - Store.MAP_INNER_WIDTH - Store.MAP_RIGHT_PADDING);
    store.setBattleLeftSpace(screenSize[0] - Store.BATTLE_INNER_WIDTH);
  }


  private void calculateTitleBarHeight(BufferedImage setupScreenshot) {
    int titleBarHeight = appWindow.getWindowsTitleBarHeight(setupScreenshot.getHeight(),
        appWindow.getWindowsTaskbarHeight());

    store.setWindowsTitleBarHeight(titleBarHeight);
  }

  public void storeWaypointsInMemory() {
    try {
      ResultSet resultSet = database.getAllWaypoints();
      LinkedList<Waypoint> waypointList = new LinkedList<Waypoint>();

      while (resultSet.next()) {
        int rowId = resultSet.getInt("ID");
        int type = resultSet.getInt("TYPE");
        int direction = resultSet.getInt("DIRECTION");
        String phrase = resultSet.getString("PHRASE");

        if (type == 0) {
          Blob baseImageBlob = resultSet.getBlob("BASE_IMAGE");
          BufferedImage baseImage = imageProcessor.getBufferedImageFromByteArray(
              baseImageBlob.getBytes(1, (int) baseImageBlob.length()));

          Blob goalImageBlob = resultSet.getBlob("GOAL_IMAGE");
          BufferedImage goalImage = imageProcessor.getBufferedImageFromByteArray(
              goalImageBlob.getBytes(1, (int) goalImageBlob.length()));

          waypointList
              .add(new Waypoint(rowId, type, direction, baseImage, goalImage, "NOT_PRESENT"));
        } else if (type == 5) {
          waypointList.add(new Waypoint(rowId, type, direction, null, null, phrase));
        } else if (type == 6) {
          waypointList.add(new Waypoint(rowId, type, direction, null, null, phrase));
        } else {
          waypointList.add(new Waypoint(rowId, type, direction, null, null, "NOT_PRESENT"));
        }
      }

      store.setWaypointList(waypointList);
      log.getLogger().info(
          log.getMessage(this, "(" + waypointList.size() + ") waypoints carregados na memória"));
    } catch (SQLException e) {
      log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e);
    }
  }

  public void execute(int mode) {
    appWindow.restore();

    BufferedImage currentScreenshot = screenshotModule.execute(this);

    if (mode == 1 || mode == 4) {
      Rectangle battleWindowArea;

      if (store.getChosenSettings() == 1) {
        battleWindowArea = new Rectangle(1737, 370, 37, 305);
      } else {
        battleWindowArea = areaSelector.getSelectedArea(currentScreenshot,
            "Selecione a área superior esquerda do Battle List");
      }

      store.setBattleWindowArea(battleWindowArea);
    }

    // Rectangle characterPositionArea =
    // areaSelector.getSelectedArea(currentScreenshot);
    Rectangle characterPositionArea = new Rectangle(866, 462, 1, 1);

    store.setCharacterPositionArea(characterPositionArea);

    try {
      store.setMinimapCross(ImageIO.read(new File(Store.MINIMAP_CROSS_ZOOM_4X_PATH)));
      store.setBattleListCrop(ImageIO.read(new File(Store.BATTLE_LIST_CROP_PATH)));
    } catch (IOException e) {
      log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e);
    }

    calculateMinimapAndBattleLeftSpace();
    store.setMinimapArea(new Rectangle(store.getMinimapLeftSpace(), Store.MAP_TOP_PADDING,
        Store.MAP_INNER_WIDTH, Store.MAP_INNER_HEIGHT));
    calculateTitleBarHeight(currentScreenshot);

    keyboardHook.register(keyboardHook.getEnableCaptureWaypointHook());
  }
}
