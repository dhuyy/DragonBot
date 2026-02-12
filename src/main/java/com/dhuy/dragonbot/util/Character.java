package com.dhuy.dragonbot.util;

import java.awt.Rectangle;
import com.dhuy.dragonbot.global.Store;

public class Character {
  private Store store;
  private Mouse mouse;

  public Character() {
    store = Store.getInstance();

    mouse = new Mouse();
  }

  public void moveMouseToCharacterPosition() {
    Rectangle characterPosition = store.getCharacterPositionArea();

    int x = characterPosition.x + store.getWindowsTitleBarHeight();
    int y = characterPosition.y + store.getWindowsTitleBarHeight();

    mouse.move(x, y);
  }

  public void moveMouseToCharacterNorthWest() {
    Rectangle characterPosition = store.getCharacterPositionArea();

    int x = characterPosition.x - Store.SPACING_FROM_CHARACTER_POSITION_TO_CLOSEST_SQM;
    int y = (characterPosition.y + store.getWindowsTitleBarHeight())
        - Store.SPACING_FROM_CHARACTER_POSITION_TO_CLOSEST_SQM;

    mouse.move(x, y);
  }

  public void moveMouseToCharacterNorth() {
    Rectangle characterPosition = store.getCharacterPositionArea();

    int x = characterPosition.x;
    int y = (characterPosition.y + store.getWindowsTitleBarHeight())
        - Store.SPACING_FROM_CHARACTER_POSITION_TO_CLOSEST_SQM;

    mouse.move(x, y);
  }

  public void moveMouseToCharacterNorthEast() {
    Rectangle characterPosition = store.getCharacterPositionArea();

    int x = characterPosition.x + Store.SPACING_FROM_CHARACTER_POSITION_TO_CLOSEST_SQM;
    int y = (characterPosition.y + store.getWindowsTitleBarHeight())
        - Store.SPACING_FROM_CHARACTER_POSITION_TO_CLOSEST_SQM;

    mouse.move(x, y);
  }

  public void moveMouseToCharacterEast() {
    Rectangle characterPosition = store.getCharacterPositionArea();

    int x = characterPosition.x + Store.SPACING_FROM_CHARACTER_POSITION_TO_CLOSEST_SQM;
    int y = (characterPosition.y + store.getWindowsTitleBarHeight());

    mouse.move(x, y);
  }

  public void moveMouseToCharacterSouthEast() {
    Rectangle characterPosition = store.getCharacterPositionArea();

    int x = characterPosition.x + Store.SPACING_FROM_CHARACTER_POSITION_TO_CLOSEST_SQM;
    int y = (characterPosition.y + store.getWindowsTitleBarHeight())
        + Store.SPACING_FROM_CHARACTER_POSITION_TO_CLOSEST_SQM;

    mouse.move(x, y);
  }

  public void moveMouseToCharacterSouth() {
    Rectangle characterPosition = store.getCharacterPositionArea();

    int x = characterPosition.x;
    int y = (characterPosition.y + store.getWindowsTitleBarHeight())
        + Store.SPACING_FROM_CHARACTER_POSITION_TO_CLOSEST_SQM;

    mouse.move(x, y);
  }

  public void moveMouseToCharacterSouthWest() {
    Rectangle characterPosition = store.getCharacterPositionArea();

    int x = characterPosition.x - Store.SPACING_FROM_CHARACTER_POSITION_TO_CLOSEST_SQM;
    int y = (characterPosition.y + store.getWindowsTitleBarHeight())
        + Store.SPACING_FROM_CHARACTER_POSITION_TO_CLOSEST_SQM;

    mouse.move(x, y);
  }

  public void moveMouseToCharacterWest() {
    Rectangle characterPosition = store.getCharacterPositionArea();

    int x = characterPosition.x - Store.SPACING_FROM_CHARACTER_POSITION_TO_CLOSEST_SQM;
    int y = (characterPosition.y + store.getWindowsTitleBarHeight());

    mouse.move(x, y);
  }
}
