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
    keyboard.type("ENTER");

    log.getLogger().info(log.getMessage(this, "Calling spell caster"));
  }
}
