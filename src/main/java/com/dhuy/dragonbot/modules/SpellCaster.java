package com.dhuy.dragonbot.modules;

import com.dhuy.dragonbot.global.Log;
import com.dhuy.dragonbot.global.Store;
import com.dhuy.dragonbot.util.Keyboard;

public class SpellCaster {
  private Store store = Store.getInstance();

  private Log log;
  private Keyboard keyboard;

  public SpellCaster() {
    log = Log.getInstance();

    keyboard = new Keyboard();
  }

  public void execute() {
    keyboard.type(store.getSpellCasterHotkey());
    keyboard.type("F6");
    keyboard.type("F4");
    keyboard.type("F3");

    log.getLogger().info(log.getMessage(this, "Calling spell caster"));
  }
}
