package com.dhuy.dragonbot.modules;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import com.dhuy.dragonbot.global.Log;
import com.dhuy.dragonbot.global.Store;
import com.dhuy.dragonbot.util.Character;
import com.dhuy.dragonbot.util.ImageProcessor;
import com.dhuy.dragonbot.util.Keyboard;
import com.dhuy.dragonbot.util.Mouse;

public class CavebotActions {
  private static final int MINOR_ACTIONS_DELAY = 75;

  private Store store;
  private Character character;
  private Keyboard keyboard;
  private Mouse mouse;
  private ImageProcessor imageProcessor;
  private Screenshot screenshotModule;
  private Looting looting;
  private Log log;

  public CavebotActions() {
    store = Store.getInstance();
    log = Log.getInstance();

    character = new Character();
    mouse = new Mouse();
    keyboard = new Keyboard();
    imageProcessor = new ImageProcessor();
    screenshotModule = new Screenshot();
    looting = new Looting();
  }

  public void executeWalkAction(int direction, boolean enableLooting) {
    Rectangle minimapArea = store.getMinimapArea();

    BufferedImage baseImage =
        store.getWaypointList().get(store.getCurrentWaypointIndex()).getBaseImage();
    BufferedImage goalImage =
        store.getWaypointList().get(store.getCurrentWaypointIndex()).getGoalImage();

    keyboard.type("ESC");

    BufferedImage currentScreenshot = screenshotModule.execute(this);

    BufferedImage currentMinimap = currentScreenshot.getSubimage(minimapArea.x, minimapArea.y,
        minimapArea.width, minimapArea.height);

    int[] waypoint = imageProcessor.findSubimage(currentMinimap, baseImage);

    BufferedImage currentGoalImage = null;

    switch (direction) {
      case 1:
        currentGoalImage = currentMinimap.getSubimage(waypoint[0],
            waypoint[1] + Store.MAP_SPACING_FROM_BASE_TO_GOAL_WAYPOINT, Store.WAYPOINT_BLOCK_SIZE,
            Store.WAYPOINT_BLOCK_SIZE);
        break;
      case 2:
        currentGoalImage =
            currentMinimap.getSubimage(waypoint[0] - Store.MAP_SPACING_FROM_BASE_TO_GOAL_WAYPOINT,
                waypoint[1], Store.WAYPOINT_BLOCK_SIZE, Store.WAYPOINT_BLOCK_SIZE);
        break;
      case 3:
        currentGoalImage = currentMinimap.getSubimage(waypoint[0],
            waypoint[1] - Store.MAP_SPACING_FROM_BASE_TO_GOAL_WAYPOINT, Store.WAYPOINT_BLOCK_SIZE,
            Store.WAYPOINT_BLOCK_SIZE);
        break;
      case 4:
        currentGoalImage =
            currentMinimap.getSubimage(waypoint[0] + Store.MAP_SPACING_FROM_BASE_TO_GOAL_WAYPOINT,
                waypoint[1], Store.WAYPOINT_BLOCK_SIZE, Store.WAYPOINT_BLOCK_SIZE);
        break;
    }

    double hasGoalImagesMatchedPercentage =
        imageProcessor.compareImages(goalImage, currentGoalImage) * 100;

    if (hasGoalImagesMatchedPercentage <= 1) {
      log.getLogger().info(log.getMessage(this, "Chegou no waypoint"));

      setNextWaypointIndex();
    } else {
      log.getLogger().info(log.getMessage(this, "Ainda não chegou no waypoint"));

      if (enableLooting) {
        looting.execute();
      }

      switch (direction) {
        case 1:
          mouse.clickOn(
              waypoint[0] + Store.WAYPOINT_MATCH_PIXEL_TO_CENTER_CROSS
                  + store.getMinimapLeftSpace(),
              waypoint[1] + Store.MAP_SPACING_FROM_BASE_TO_GOAL_WAYPOINT
                  + Store.WAYPOINT_MATCH_PIXEL_TO_CENTER_CROSS + Store.MAP_TOP_PADDING
                  + store.getWindowsTitleBarHeight());
          break;
        case 2:
          mouse.clickOn(
              waypoint[0] - Store.MAP_SPACING_FROM_BASE_TO_GOAL_WAYPOINT
                  + Store.WAYPOINT_MATCH_PIXEL_TO_CENTER_CROSS + store.getMinimapLeftSpace(),
              waypoint[1] + Store.WAYPOINT_MATCH_PIXEL_TO_CENTER_CROSS + Store.MAP_TOP_PADDING
                  + store.getWindowsTitleBarHeight());
          break;
        case 3:
          mouse.clickOn(
              waypoint[0] + Store.WAYPOINT_MATCH_PIXEL_TO_CENTER_CROSS
                  + store.getMinimapLeftSpace(),
              waypoint[1] - Store.MAP_SPACING_FROM_BASE_TO_GOAL_WAYPOINT
                  + Store.WAYPOINT_MATCH_PIXEL_TO_CENTER_CROSS + Store.MAP_TOP_PADDING
                  + store.getWindowsTitleBarHeight());
          break;
        case 4:
          mouse.clickOn(
              waypoint[0] + Store.MAP_SPACING_FROM_BASE_TO_GOAL_WAYPOINT
                  + Store.WAYPOINT_MATCH_PIXEL_TO_CENTER_CROSS + store.getMinimapLeftSpace(),
              waypoint[1] + Store.WAYPOINT_MATCH_PIXEL_TO_CENTER_CROSS + Store.MAP_TOP_PADDING
                  + store.getWindowsTitleBarHeight());
          break;
      }
    }

    store.getMovementDetector().resetForNewWalk();
  }

  public void executeRopeAction() {
    character.moveMouseToCharacterPosition();

    delay(MINOR_ACTIONS_DELAY);
    keyboard.type("F11");

    delay(MINOR_ACTIONS_DELAY);
    mouse.click();

    log.getLogger().info(log.getMessage(this, "Executando ação: ROPE"));

    setNextWaypointIndex();
  }

  public void executeShovelAction(int direction) {
    moveMouseToDirection(direction);

    delay(MINOR_ACTIONS_DELAY);
    keyboard.type("F10");

    delay(MINOR_ACTIONS_DELAY);
    mouse.click();

    log.getLogger().info(log.getMessage(this, "Executando ação: SHOVEL"));

    setNextWaypointIndex();
  }

  public void executeLadderAction(int direction) {
    moveMouseToDirection(direction);

    delay(MINOR_ACTIONS_DELAY);
    mouse.backClick();

    log.getLogger().info(log.getMessage(this, "Executando ação: LADDER"));

    setNextWaypointIndex();
  }

  public void executeHoleRampAction(int direction) {
    moveMouseToDirection(direction);

    delay(MINOR_ACTIONS_DELAY);
    mouse.click();

    log.getLogger().info(log.getMessage(this, "Executando ação: HOLE/RAMP"));

    setNextWaypointIndex();
  }

  public void executeTalkAction(String phrase) {
    delay(MINOR_ACTIONS_DELAY);
    keyboard.writeWord(phrase);
    delay(MINOR_ACTIONS_DELAY);
    keyboard.type("ENTER");
    delay(MINOR_ACTIONS_DELAY);

    log.getLogger().info(log.getMessage(this, "Executando ação: TALK"));

    setNextWaypointIndex();
  }

  public void executeSequencialClicksAction(String sequence) {
    delay(MINOR_ACTIONS_DELAY * 3);

    String[] split = sequence.split("[*]");
    int numberOfRounds = Integer.parseInt(split[0]);
    String[] coordinates = split[1].split("[|]");

    for (int i = 0; i < numberOfRounds; i++) {
      for (String coordinate : coordinates) {
        String[] axis = coordinate.split("x");

        mouse.clickOn(Integer.parseInt(axis[0]), Integer.parseInt(axis[1]),
            store.getWindowsTitleBarHeight());
        delay(250);
      }
    }

    log.getLogger().info(log.getMessage(this, "Executando ação: SEQUENCIAL CLICKS"));

    setNextWaypointIndex();
  }

  private void moveMouseToDirection(int direction) {
    switch (direction) {
      case 1:
        character.moveMouseToCharacterNorth();
        break;
      case 2:
        character.moveMouseToCharacterEast();
        break;
      case 3:
        character.moveMouseToCharacterSouth();
        break;
      case 4:
        character.moveMouseToCharacterWest();
        break;
    }
  }

  private void setNextWaypointIndex() {
    int currentWaypointIndex = store.getCurrentWaypointIndex();
    int nextWaypointIndex = currentWaypointIndex + 1;

    if (nextWaypointIndex == store.getWaypointList().size()) {
      store.setCurrentWaypointIndex(0);
      store.setShouldStopCavebotModule(true);
    } else {
      store.setCurrentWaypointIndex(nextWaypointIndex);
    }
  }

  private void delay(int ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e);
    }
  }
}
