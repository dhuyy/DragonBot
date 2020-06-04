package com.dhuy.dragonbot.modules;

import com.dhuy.dragonbot.util.Keyboard;

public class Food {
  private Keyboard keyboard;

  public Food() {
    keyboard = new Keyboard();
  }

  public void execute() {
    keyboard.type("F9");
  }
}
