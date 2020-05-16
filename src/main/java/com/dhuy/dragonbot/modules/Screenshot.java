package com.dhuy.dragonbot.modules;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
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

public class Screenshot {
  ScreenshotCache screenshotCache = ScreenshotCache.getInstance();
  ThreadPoolExecutor threadPoolExecutor = ThreadPool.getInstance().getExecutor();

  private FileSystem fileSystem;
  private ApplicationWindow appWindow;

  public Screenshot() {
    fileSystem = new FileSystem();
    appWindow = new ApplicationWindow();

    fileSystem.cleanupScreenshots();
    appWindow.restore("Django Enforced");
  }

  public void execute() {
    try {
      String screenshotPath = null;

      new Robot().keyPress(KeyEvent.VK_F12);
      new Robot().keyRelease(KeyEvent.VK_F12);

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
    } catch (AWTException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
