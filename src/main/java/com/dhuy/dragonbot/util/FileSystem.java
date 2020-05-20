package com.dhuy.dragonbot.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;
import org.apache.commons.io.FileUtils;
import com.dhuy.dragonbot.global.Store;

public class FileSystem {
  public FileSystem() {}

  public String getLastModifiedScreenshot() {
    Path dir = Paths.get(Store.getInstance().getTibiaScreenshotAbsoluteDirectory());

    Optional<Path> lastFilePath;
    try {
      lastFilePath = Files.list(dir) // Get the stream with full directory listing
          .filter(f -> !Files.isDirectory(f)) // Exclude sub-directories from listing
          .max(Comparator.comparingLong(f -> f.toFile().lastModified()));

      if (lastFilePath.isPresent()) // Folder may be empty
      {
        return lastFilePath.get().toString();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  public void cleanupScreenshots() {
    try {
      final File screenshotDirectory =
          new File(Store.getInstance().getTibiaScreenshotAbsoluteDirectory());

      FileUtils.cleanDirectory(screenshotDirectory);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void deleteFileIfExists(Path path) {
    try {
      Files.deleteIfExists(path);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
