package com.dhuy.dragonbot.modules;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import com.dhuy.dragonbot.global.Log;
import com.dhuy.dragonbot.util.FileSystem;
import com.dhuy.dragonbot.util.Keyboard;

public class Screenshot {
  private Keyboard keyboard;
  private FileSystem fileSystem;
  private Log log;

  public Screenshot() {
    keyboard = new Keyboard();
    fileSystem = new FileSystem();
    log = Log.getInstance();
  }

  public BufferedImage execute(Object caller) {
    log.getLogger().info(log.getMessage(caller, "Screenshot requisitada"));

    try {
      String screenshotPath = null;

      fileSystem.cleanupScreenshots();

      keyboard.type("F12");

      boolean hasFoundScreenshot = false;
      while (!hasFoundScreenshot) {
        screenshotPath = fileSystem.getLastModifiedScreenshot();

        if (screenshotPath != null) {
          hasFoundScreenshot = true;
        }
      }

      return ImageIO.read(new File(screenshotPath));
    } catch (IOException e) {
      log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e);
    }

    return null;
  }
}
