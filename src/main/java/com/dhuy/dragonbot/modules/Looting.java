package com.dhuy.dragonbot.modules;

import java.util.logging.Level;
import com.dhuy.dragonbot.global.Log;
import com.dhuy.dragonbot.util.Character;
import com.dhuy.dragonbot.util.Keyboard;

public class Looting {
  private static final int MINOR_ACTIONS_DELAY = 75;

  private Log log;
  private Keyboard keyboard;
  private Character character;

  public Looting() {
    log = Log.getInstance();

    keyboard = new Keyboard();
    character = new Character();
  }

  public void execute() {
    character.moveMouseToCharacterNorthWest();
    keyboard.pressShiftAndBackButton();
    delay(MINOR_ACTIONS_DELAY);

    character.moveMouseToCharacterNorth();
    keyboard.pressShiftAndBackButton();
    delay(MINOR_ACTIONS_DELAY);

    character.moveMouseToCharacterNorthEast();
    keyboard.pressShiftAndBackButton();
    delay(MINOR_ACTIONS_DELAY);

    character.moveMouseToCharacterEast();
    keyboard.pressShiftAndBackButton();
    delay(MINOR_ACTIONS_DELAY);

    character.moveMouseToCharacterSouthEast();
    keyboard.pressShiftAndBackButton();
    delay(MINOR_ACTIONS_DELAY);

    character.moveMouseToCharacterSouth();
    keyboard.pressShiftAndBackButton();
    delay(MINOR_ACTIONS_DELAY);

    character.moveMouseToCharacterSouthWest();
    keyboard.pressShiftAndBackButton();
    delay(MINOR_ACTIONS_DELAY);

    character.moveMouseToCharacterWest();
    keyboard.pressShiftAndBackButton();
    delay(MINOR_ACTIONS_DELAY * 2);
  }

  private void delay(int ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e.getStackTrace());
    }
  }
}
