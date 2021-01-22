package com.dhuy.dragonbot.modules;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.logging.Level;
import com.dhuy.dragonbot.global.Log;
import com.dhuy.dragonbot.global.Store;
import com.dhuy.dragonbot.model.Item;
import com.dhuy.dragonbot.util.ApplicationWindow;
import com.dhuy.dragonbot.util.ImageProcessor;
import com.dhuy.dragonbot.util.Keyboard;
import com.dhuy.dragonbot.util.Mouse;
import com.dhuy.dragonbot.util.MouseCoordinates;
import com.dhuy.dragonbot.util.XMLHelper;

public class CollectItemsToSell {
  Store store = Store.getInstance();

  private List<Item> items;

  private Log log;
  private XMLHelper xml;
  private Mouse mouse;
  private Keyboard keyboard;
  private ImageProcessor imageProcessor;
  private ApplicationWindow appWindow;
  private Screenshot screenshotModule;
  private Hunting hunting;

  public CollectItemsToSell() throws AWTException {
    log = Log.getInstance();
    xml = new XMLHelper();
    mouse = new Mouse();
    keyboard = new Keyboard();
    imageProcessor = new ImageProcessor();
    appWindow = new ApplicationWindow();
    screenshotModule = new Screenshot();
    hunting = new Hunting();
  }

  public void execute() {
    BufferedImage currentScreenshot = screenshotModule.execute(this);

    int titleBarHeight = appWindow.getWindowsTitleBarHeight(currentScreenshot.getHeight(),
        appWindow.getWindowsTaskbarHeight());

    store.setWindowsTitleBarHeight(titleBarHeight);

    try {
      items = xml.getItemsList();

      boolean shouldOpenDepotBox = true;
      boolean isThereCapacityLeft = true;
      boolean shouldCollectMoreItems = true;
      while (shouldCollectMoreItems) {
        if (shouldOpenDepotBox) {
          mouse.clickOn(MouseCoordinates.DEPOT_BOX_X,
              MouseCoordinates.DEPOT_BOX_Y + store.getWindowsTitleBarHeight(), true);
          delay(500);

          mouse.clickOn(MouseCoordinates.SEARCH_ITEMS_BUTTON_X,
              MouseCoordinates.SEARCH_ITEMS_BUTTON_Y, store.getWindowsTitleBarHeight());
          delay(500);

          shouldOpenDepotBox = false;
        }

        mouse.clickOn(MouseCoordinates.CLEAR_FOUND_ITEM_NAME_X,
            MouseCoordinates.CLEAR_FOUND_ITEM_NAME_Y, store.getWindowsTitleBarHeight());
        delay(500);

        keyboard.writeWord(items.get(store.getCurrentCollectItemIndex()).getName());
        delay(500);

        mouse.clickOn(MouseCoordinates.FIRST_FOUND_ITEM_X, MouseCoordinates.FIRST_FOUND_ITEM_Y,
            store.getWindowsTitleBarHeight());
        delay(500);

        mouse.clickOn(MouseCoordinates.SEARCH_FOR_ITEMS_X, MouseCoordinates.SEARCH_FOR_ITEMS_Y,
            store.getWindowsTitleBarHeight());
        delay(500);

        currentScreenshot = screenshotModule.execute(this);

        String isThereAnyItemPixelHex = imageProcessor.getHexFromColor(
            new Color(currentScreenshot.getSubimage(MouseCoordinates.CHECK_EXISTING_ITEMS_X,
                MouseCoordinates.CHECK_EXISTING_ITEMS_Y, 1, 1).getRGB(0, 0)));

        if (isThereAnyItemPixelHex
            .equals(Store.INDICATOR_EXISTING_ITEMS_IN_POSTAL_BOX_PIXEL_COLOR)) {
          while (isThereCapacityLeft && isThereAnyItemPixelHex
              .equals(Store.INDICATOR_EXISTING_ITEMS_IN_POSTAL_BOX_PIXEL_COLOR)) {
            mouse.clickOn(MouseCoordinates.RETRIEVE_ITEMS_X, MouseCoordinates.RETRIEVE_ITEMS_Y,
                store.getWindowsTitleBarHeight());

            delay(2000);

            BufferedImage newCurrentScreenshot = screenshotModule.execute(this);

            String isThereCapacityLeftPixelHex =
                imageProcessor
                    .getHexFromColor(
                        new Color(
                            newCurrentScreenshot
                                .getSubimage(MouseCoordinates.PIXEL_NOT_ENOUGH_CAPACITY_X,
                                    MouseCoordinates.PIXEL_NOT_ENOUGH_CAPACITY_Y, 1, 1)
                                .getRGB(0, 0)));

            if (isThereCapacityLeftPixelHex.equals(Store.NOT_ENOUGH_CAPACITY_PIXEL_COLOR)) {
              mouse.clickOn(MouseCoordinates.NOT_ENOUGH_CAPACITY_INFO_MESSAGE_X,
                  MouseCoordinates.NOT_ENOUGH_CAPACITY_INFO_MESSAGE_Y,
                  store.getWindowsTitleBarHeight());
              delay(500);

              isThereCapacityLeft = false;
            }

            isThereAnyItemPixelHex = imageProcessor.getHexFromColor(
                new Color(newCurrentScreenshot.getSubimage(MouseCoordinates.CHECK_EXISTING_ITEMS_X,
                    MouseCoordinates.CHECK_EXISTING_ITEMS_Y, 1, 1).getRGB(0, 0)));
          }

          if (!isThereAnyItemPixelHex
              .equals(Store.INDICATOR_EXISTING_ITEMS_IN_POSTAL_BOX_PIXEL_COLOR)) {
            store.setCurrentCollectItemIndex(store.getCurrentCollectItemIndex() + 1);
            delay(250);

            mouse.clickOn(MouseCoordinates.GO_BACK_SEARCH_ITEMS_X,
                MouseCoordinates.GO_BACK_SEARCH_ITEMS_Y, store.getWindowsTitleBarHeight());
            delay(500);
          }

          if (!isThereCapacityLeft) {
            while (!store.isShouldStopCavebotModule()) {
              hunting.execute(false);
            }

            store.setShouldStopCavebotModule(false);

            isThereCapacityLeft = true;
            shouldOpenDepotBox = true;

            delay(2000);
          }
        } else {
          store.setCurrentCollectItemIndex(store.getCurrentCollectItemIndex() + 1);
          delay(250);

          mouse.clickOn(MouseCoordinates.GO_BACK_SEARCH_ITEMS_X,
              MouseCoordinates.GO_BACK_SEARCH_ITEMS_Y, store.getWindowsTitleBarHeight());
          delay(500);
        }

        if ((store.getCurrentCollectItemIndex() + 1) > items.size()) {
          shouldCollectMoreItems = false;
        }
      }

      System.exit(0);
    } catch (Exception e) {
      log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e);
    }
  }

  private void delay(int milliseconds) throws InterruptedException {
    Thread.sleep(milliseconds);
  }
}
