package com.dhuy.dragonbot.modules;

import java.util.logging.Level;

import com.dhuy.dragonbot.global.Log;
import com.dhuy.dragonbot.util.Keyboard;

public class AntiLogout {
  private Log log;
  private Keyboard keyboard;

  public AntiLogout() {
    log = Log.getInstance();

    keyboard = new Keyboard();
  }

  public void execute() {
    try {
    	keyboard.pressShiftAndKey("UP");
			Thread.sleep(50);
			keyboard.pressShiftAndKey("DOWN");
		} catch (InterruptedException e) {
      log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e);
		}

    log.getLogger().info(log.getMessage(this, "Movimento anti logout"));
  }
}
