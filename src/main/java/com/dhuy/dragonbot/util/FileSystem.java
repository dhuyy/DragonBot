package com.dhuy.dragonbot.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;
import com.dhuy.dragonbot.global.Constants;

public class FileSystem {
  public Path getLastModifiedScreenshot() {
    Constants constants = Constants.getInstance();
    Path dir = Paths.get(constants.getTibiaScreenshotAbsoluteDirectory());

    Optional<Path> lastFilePath;
    try {
      lastFilePath = Files.list(dir) // Get the stream with full directory listing
          .filter(f -> !Files.isDirectory(f)) // Exclude sub-directories from listing
          .max(Comparator.comparingLong(f -> f.toFile().lastModified()));

      if (lastFilePath.isPresent()) // Folder may be empty
      {
        return lastFilePath.get();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }
}
