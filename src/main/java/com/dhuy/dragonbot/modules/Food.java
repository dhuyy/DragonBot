package com.dhuy.dragonbot.modules;

import com.dhuy.dragonbot.global.Log;
import com.dhuy.dragonbot.util.Keyboard;

public class Food {
  private Log log;
  private Keyboard keyboard;

  public Food() {
    log = Log.getInstance();

    keyboard = new Keyboard();
  }

  public void execute() {
    keyboard.type("F9");

    log.getLogger().info(log.getMessage(this, "Comendo food"));
  }
}
