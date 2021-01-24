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
    // log.getLogger().info(log.getMessage(caller, "Screenshot requisitada"));

    try {
      String screenshotPath = null;

      int screenshotsAmount = fileSystem.getTibiaScreenshotsLength();

      keyboard.type("F12");

      while (true) {
        if (screenshotsAmount != fileSystem.getTibiaScreenshotsLength()) {
          screenshotPath = fileSystem.getTheNewestFile();

          break;
        }
      }

      BufferedImage currentScreenshot = ImageIO.read(new File(screenshotPath));

      fileSystem.deleteFile(screenshotPath);

      return currentScreenshot;
    } catch (IOException e1) {
      log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e1);

      return null;
    }
  }
}
