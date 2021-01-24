package com.dhuy.dragonbot.modules;

import java.awt.AWTException;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.logging.Level;
import com.dhuy.dragonbot.global.Log;
import com.dhuy.dragonbot.global.Store;
import com.dhuy.dragonbot.model.Item;
import com.dhuy.dragonbot.util.ApplicationWindow;
import com.dhuy.dragonbot.util.Keyboard;
import com.dhuy.dragonbot.util.Mouse;
import com.dhuy.dragonbot.util.MouseCoordinates;
import com.dhuy.dragonbot.util.NormalizeOCREntries;
import com.dhuy.dragonbot.util.OCRHelper;
import com.dhuy.dragonbot.util.XMLHelper;

public class OptimizeItemList {
  public int AMOUNT_TO_BUY_FACTOR = 20;
  public int MINIMUM_TRANSACTIONS = 60;

  Store store = Store.getInstance();

  private List<Item> items;

  private Log log;
  private XMLHelper xml;
  private Mouse mouse;
  private Keyboard keyboard;
  private NormalizeOCREntries normalizeOCREntries;
  private OCRHelper ocrHelper;
  private ApplicationWindow appWindow;
  private Screenshot screenshotModule;
  private MouseCoordinates mouseCoordinates;

  public OptimizeItemList(boolean isRunningInVM) throws AWTException {
    log = Log.getInstance();
    xml = new XMLHelper();
    mouse = new Mouse();
    keyboard = new Keyboard();
    normalizeOCREntries = new NormalizeOCREntries();
    ocrHelper = new OCRHelper();
    appWindow = new ApplicationWindow();
    screenshotModule = new Screenshot();
    mouseCoordinates = new MouseCoordinates(isRunningInVM);
  }

  public void execute() {
    BufferedImage currentScreenshot = screenshotModule.execute(this);

    int titleBarHeight = appWindow.getWindowsTitleBarHeight(currentScreenshot.getHeight(),
        appWindow.getWindowsTaskbarHeight());

    store.setWindowsTitleBarHeight(titleBarHeight);

    try {
      items = xml.getItemsList();

      for (Item item : items) {
        mouse.clickOn(mouseCoordinates.SEARCH_BOX_X, mouseCoordinates.SEARCH_BOX_Y,
            store.getWindowsTitleBarHeight());
        delay(250);

        keyboard.selectAllTextAndDelete();
        delay(250);

        keyboard.writeWord(item.getName());
        delay(250);

        mouse.clickOn(mouseCoordinates.FIRST_FOUND_X, mouseCoordinates.FIRST_FOUND_Y,
            store.getWindowsTitleBarHeight());
        delay(250);

        mouse.clickOn(mouseCoordinates.DETAIL_BUTTON_MARKET_X,
            mouseCoordinates.DETAIL_BUTTON_MARKET_Y, store.getWindowsTitleBarHeight());
        delay(500);

        int numberOfTransactions = Integer.parseInt(normalizeOCREntries.normalizePrice(
            ocrHelper.getTextFromImage(mouseCoordinates.AMOUNT_BUY_OFFERS_IN_SERVER_X_TOP,
                mouseCoordinates.AMOUNT_BUY_OFFERS_IN_SERVER_Y_TOP,
                mouseCoordinates.AMOUNT_BUY_OFFERS_IN_SERVER_X_BOTTOM,
                mouseCoordinates.AMOUNT_BUY_OFFERS_IN_SERVER_Y_BOTTOM)));

        if (numberOfTransactions < MINIMUM_TRANSACTIONS) {
          xml.deleteItem(item.getName());
        } else {
          int calculateAmountOfItemsToBuy = numberOfTransactions / AMOUNT_TO_BUY_FACTOR;

          xml.updateBuyAmount("" + calculateAmountOfItemsToBuy + "", item.getName());
        }
      }

      mouse.clickOn(mouseCoordinates.CLOSE_MARKET_X, mouseCoordinates.CLOSE_MARKET_Y,
          store.getWindowsTitleBarHeight());
      System.exit(0);
    } catch (Exception e) {
      log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e);
    }
  }

  private void delay(int milliseconds) throws InterruptedException {
    Thread.sleep(milliseconds);
  }
}
