package com.dhuy.dragonbot.modules;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.ThreadPoolExecutor;
import javax.imageio.ImageIO;
import com.dhuy.dragonbot.global.ScreenshotCache;
import com.dhuy.dragonbot.global.ThreadPool;
import com.dhuy.dragonbot.util.ApplicationWindow;
import com.dhuy.dragonbot.util.FileSystem;
import com.dhuy.dragonbot.util.Keyboard;

public class Screenshot {
  ScreenshotCache screenshotCache = ScreenshotCache.getInstance();
  ThreadPoolExecutor threadPoolExecutor = ThreadPool.getInstance().getExecutor();

  private Keyboard keyboard;
  private FileSystem fileSystem;
  private ApplicationWindow appWindow;

  public Screenshot() {
    keyboard = new Keyboard();
    fileSystem = new FileSystem();
    appWindow = new ApplicationWindow();

    fileSystem.cleanupScreenshots();
    appWindow.restore("Django Enforced");
  }

  public void execute() {
    try {
      String screenshotPath = null;

      keyboard.type("F12");

      boolean hasFoundNewScreenshot = true;
      while (hasFoundNewScreenshot) {
        screenshotPath = fileSystem.getLastModifiedScreenshot();

        if (screenshotPath != null
            && !screenshotPath.equals(screenshotCache.getCurrentScreenshotKey())) {
          hasFoundNewScreenshot = false;
        }

        Thread.sleep(1000);
      }

      BufferedImage screenshot = ImageIO.read(new File(screenshotPath));

      screenshotCache.setCurrentScreenshotKey(screenshotPath);
      screenshotCache.setCurrentScreenshotValue(screenshot);

      final String finalScreenshotPath = screenshotPath;
      threadPoolExecutor.submit(() -> {
        try {
          Thread.sleep(8000);

          fileSystem.deleteFileIfExists(Paths.get(finalScreenshotPath));
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
