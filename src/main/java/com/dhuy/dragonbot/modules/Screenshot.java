package com.dhuy.dragonbot.modules;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import com.dhuy.dragonbot.global.ScreenshotCache;
import com.dhuy.dragonbot.util.FileSystem;
import com.dhuy.dragonbot.util.Keyboard;

public class Screenshot {
  private ScreenshotCache screenshotCache = ScreenshotCache.getInstance();

  private Keyboard keyboard;
  private FileSystem fileSystem;

  public Screenshot() {
    keyboard = new Keyboard();
    fileSystem = new FileSystem();
  }

  public void execute(String screenshotRequestedBy) {
    System.out.println("Screenshot requested from " + screenshotRequestedBy);

    try {
      String screenshotPath = null;

      keyboard.type("F12");

      boolean hasFoundLastCreatedScreenshot = false;
      while (!hasFoundLastCreatedScreenshot) {
        screenshotPath = fileSystem.getLastModifiedScreenshot();

        if (screenshotPath != null
            && !screenshotPath.equals(screenshotCache.getCurrentScreenshotKey())) {
          hasFoundLastCreatedScreenshot = true;
        }
      }

      screenshotCache.setCurrentScreenshotKey(screenshotPath);
      screenshotCache.setCurrentScreenshotValue(ImageIO.read(new File(screenshotPath)));

      fileSystem.deleteFileIfExists(Paths.get(screenshotPath));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
