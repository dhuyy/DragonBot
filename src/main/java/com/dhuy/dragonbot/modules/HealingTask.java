package com.dhuy.dragonbot.modules;

public class HealingTask implements Runnable {
  private Healing healingModule;

  public HealingTask() {
    healingModule = new Healing();
  }

  @Override
  public void run() {
    while (true) {
      healingModule.execute();
    }
  }
}
