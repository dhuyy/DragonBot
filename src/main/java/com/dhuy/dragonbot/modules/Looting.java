package com.dhuy.dragonbot.modules;

import java.awt.Rectangle;
import java.util.logging.Level;
import com.dhuy.dragonbot.global.Log;
import com.dhuy.dragonbot.global.Store;
import com.dhuy.dragonbot.util.Keyboard;
import com.dhuy.dragonbot.util.Mouse;

public class Looting {
  private Store store = Store.getInstance();

  private Mouse mouse;
  private Keyboard keyboard;
  private Log log;

  public Looting() {
    mouse = new Mouse();
    keyboard = new Keyboard();
    log = Log.getInstance();
  }

  public void execute() {
    try {
      Rectangle characterPosition = store.getCharacterPositionArea();

      int DELAY = 75;

      mouse.move(characterPosition.x - 70, (characterPosition.y + 23) - 70);
      keyboard.collectLoot();
      Thread.sleep(DELAY);
      mouse.move(characterPosition.x, (characterPosition.y + 23) - 70);
      keyboard.collectLoot();
      Thread.sleep(DELAY);
      mouse.move(characterPosition.x + 70, (characterPosition.y + 23) - 70);
      keyboard.collectLoot();
      Thread.sleep(DELAY);
      mouse.move(characterPosition.x + 70, (characterPosition.y + 23));
      keyboard.collectLoot();
      Thread.sleep(DELAY);
      mouse.move(characterPosition.x + 70, (characterPosition.y + 23) + 70);
      keyboard.collectLoot();
      Thread.sleep(DELAY);
      mouse.move(characterPosition.x, (characterPosition.y + 23) + 70);
      keyboard.collectLoot();
      Thread.sleep(DELAY);
      mouse.move(characterPosition.x - 70, (characterPosition.y + 23) + 70);
      keyboard.collectLoot();
      Thread.sleep(DELAY);
      mouse.move(characterPosition.x - 70, (characterPosition.y + 23));
      keyboard.collectLoot();
      Thread.sleep(DELAY * 2);
    } catch (InterruptedException e) {
      log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e.getStackTrace());
    }
  }
}
