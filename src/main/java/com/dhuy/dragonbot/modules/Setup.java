package com.dhuy.dragonbot.modules;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import com.dhuy.dragonbot.global.KeyboardHook;
import com.dhuy.dragonbot.global.ScreenshotCache;
import com.dhuy.dragonbot.global.Store;
import com.dhuy.dragonbot.util.ApplicationWindow;
import com.dhuy.dragonbot.util.FileSystem;

public class Setup {
  private Store store = Store.getInstance();
  private KeyboardHook keyboardHook = KeyboardHook.getInstance();
  private ScreenshotCache screenshotCache = ScreenshotCache.getInstance();

  private ApplicationWindow appWindow;
  private Screenshot screenshotModule;
  private FileSystem fileSystem;

  public Setup() {
    appWindow = new ApplicationWindow();
    screenshotModule = new Screenshot();
    fileSystem = new FileSystem();

    fileSystem.cleanupScreenshots();
  }

  private void calculateMinimapLeftSpace() {
    int[] screenSize = appWindow.getScreenSize();

    store.setMinimapLeftSpace(screenSize[0] - Store.MAP_INNER_WIDTH - Store.MAP_RIGHT_PADDING);
  }

  private void calculateTitleBarHeight(BufferedImage setupScreenshot) {
    int titleBarHeight = appWindow.getWindowsTitleBarHeight(setupScreenshot.getHeight(),
        appWindow.getWindowsTaskbarHeight());

    store.setWindowsTitleBarHeight(titleBarHeight);
  }

  public void execute() {
    appWindow.restore();

    screenshotModule.execute(this.getClass().getName());

    calculateMinimapLeftSpace();
    store.setMinimapArea(new Rectangle(store.getMinimapLeftSpace(), Store.MAP_TOP_PADDING,
        Store.MAP_INNER_WIDTH, Store.MAP_INNER_HEIGHT));
    calculateTitleBarHeight(screenshotCache.getCurrentScreenshotValue());

    keyboardHook.register(keyboardHook.getEnableCaptureWaypointHook());
  }
}
