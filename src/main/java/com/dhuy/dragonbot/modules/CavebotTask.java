package com.dhuy.dragonbot.modules;

public class CavebotTask implements Runnable {
  private Cavebot cavebotModule;

  public CavebotTask() {
    cavebotModule = new Cavebot();
  }

  @Override
  public void run() {
    while (true) {
      cavebotModule.execute();
    }
  }
}
