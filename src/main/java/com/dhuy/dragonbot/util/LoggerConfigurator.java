package com.dhuy.dragonbot.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerConfigurator {
  private Logger logger;

  public LoggerConfigurator() {
    logger = Logger.getLogger("global");
  }

  public void initialize() {
    try {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YY");
      SimpleFormatter simpleFormatter = new SimpleFormatter();

      File file = new File("logs");
      if (!(file.exists())) {
        file.mkdir();
      }

      FileHandler fileHandler = new FileHandler(
          "logs/log-" + simpleDateFormat.format(Calendar.getInstance().getTime()) + ".log", true);

      fileHandler.setFormatter(simpleFormatter);

      logger.setUseParentHandlers(false);
      logger.addHandler(fileHandler);
    } catch (SecurityException | IOException e) {
      logger.log(Level.SEVERE, null, e.getStackTrace());
    }
  }
}
