package com.dhuy.dragonbot.global;

import java.util.logging.Logger;

public class Log {
  private static Log instance = new Log();

  private Log() {}

  public static Log getInstance() {
    if (instance == null) {
      synchronized (Log.class) {
        if (instance == null) {
          instance = new Log();
        }
      }
    }

    return instance;
  }

  public String getMessage(Object caller, String message) {
    String packageName = caller.getClass().getPackage().getName().concat(".");
    String className = caller.getClass().getName().replace(packageName, "");
    String logMessage = "[" + className + "]".concat(message != null ? " " + message : "");

    System.out.println(logMessage);

    return logMessage;
  }

  public Logger getLogger() {
    return Logger.getLogger("global");
  }
}
