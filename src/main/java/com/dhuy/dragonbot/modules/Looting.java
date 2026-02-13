package com.dhuy.dragonbot.modules;

import java.util.logging.Level;
import com.dhuy.dragonbot.global.Log;
import com.dhuy.dragonbot.util.Character;
import com.dhuy.dragonbot.util.Keyboard;

public class Looting {
  private static final int MINOR_ACTIONS_DELAY = 40;

  private Log log;
  private Keyboard keyboard;
  private Character character;

  public Looting() {
    log = Log.getInstance();

    keyboard = new Keyboard();
    character = new Character();
  }

  public void execute() {
    keyboard.pressShift();

    character.moveMouseToCharacterNorthWest();
    keyboard.rightClick();
    delay(MINOR_ACTIONS_DELAY);

    character.moveMouseToCharacterNorth();
    keyboard.rightClick();
    delay(MINOR_ACTIONS_DELAY);

    character.moveMouseToCharacterNorthEast();
    keyboard.rightClick();
    delay(MINOR_ACTIONS_DELAY);

    character.moveMouseToCharacterEast();
    keyboard.rightClick();
    delay(MINOR_ACTIONS_DELAY);

    character.moveMouseToCharacterSouthEast();
    keyboard.rightClick();
    delay(MINOR_ACTIONS_DELAY);

    character.moveMouseToCharacterSouth();
    keyboard.rightClick();
    delay(MINOR_ACTIONS_DELAY);

    character.moveMouseToCharacterSouthWest();
    keyboard.rightClick();
    delay(MINOR_ACTIONS_DELAY);

    character.moveMouseToCharacterWest();
    keyboard.rightClick();
    delay(MINOR_ACTIONS_DELAY);

    keyboard.releaseShift();
  }

  private void delay(int ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e);
    }
  }
}
