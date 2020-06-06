package com.dhuy.dragonbot.modules;

import com.dhuy.dragonbot.global.Log;
import com.dhuy.dragonbot.util.Keyboard;

public class Healing {
  private Log log;
  private Keyboard keyboard;

  public Healing() {
    log = Log.getInstance();

    keyboard = new Keyboard();
  }

  public void execute() {
    keyboard.type("F8");

    log.getLogger().info(log.getMessage(this, "Usando magia de healing"));
  }
}
