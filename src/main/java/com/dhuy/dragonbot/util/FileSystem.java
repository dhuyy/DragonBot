package com.dhuy.dragonbot.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.logging.Level;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import com.dhuy.dragonbot.global.Log;
import com.dhuy.dragonbot.global.Store;

public class FileSystem {
  private Log log;

  public FileSystem() {
    log = Log.getInstance();
  }

  public String getLastModifiedScreenshot() {
    File dir = new File(Store.getInstance().getTibiaScreenshotAbsoluteDirectory());
    FileFilter fileFilter = new WildcardFileFilter("*.png");
    File[] files = dir.listFiles(fileFilter);

    if (files.length != 0) {
      return files[0].getPath();
    }

    return null;
  }

  public String[] getScriptFiles() {
    String[] scriptFiles = null;

    File dir = new File(Store.getInstance().getScriptsDirectory());
    FileFilter fileFilter = new WildcardFileFilter("*.db");
    File[] files = dir.listFiles(fileFilter);

    if (files.length != 0) {
      scriptFiles = new String[files.length];

      for (int i = 0; i < files.length; i++) {
        scriptFiles[i] = files[i].getPath().replace(Store.getInstance().getScriptsDirectory(), "")
            .replace(".mv.db", "");
      }
    }

    return scriptFiles;
  }

  public void cleanupScreenshots() {
    try {
      FileUtils.cleanDirectory(new File(Store.getInstance().getTibiaScreenshotAbsoluteDirectory()));
    } catch (IOException e) {
      log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e.getStackTrace());
    }
  }
}
