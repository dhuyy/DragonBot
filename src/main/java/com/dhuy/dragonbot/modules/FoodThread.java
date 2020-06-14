package com.dhuy.dragonbot.modules;

import java.util.logging.Level;
import com.dhuy.dragonbot.global.Log;

public class FoodThread implements Runnable {
  private static final int EAT_FOOD_DELAY = 30000;

  private Log log;
  private Thread thread;
  private boolean suspended;
  private Food food;

  public FoodThread() {
    log = Log.getInstance();

    suspended = false;
    food = new Food();
  }

  @Override
  public void run() {
    try {
      while (true) {
        food.execute();

        delay(EAT_FOOD_DELAY);

        synchronized (this) {
          while (suspended) {
            wait();
          }
        }
      }
    } catch (InterruptedException e) {
      log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e);

      log.getLogger().info(log.getMessage(this, "Thread interrupted"));
    }

    log.getLogger().info(log.getMessage(this, "Thread exiting"));
  }

  public void start() {
    if (thread == null) {
      thread = new Thread(this);
      thread.start();

      log.getLogger().info(log.getMessage(this, "Thread started"));
    }
  }

  public void suspend() {
    suspended = true;

    log.getLogger().info(log.getMessage(this, "Thread suspended"));
  }

  public synchronized void resume() {
    suspended = false;

    notify();

    log.getLogger().info(log.getMessage(this, "Thread resumed"));
  }

  public boolean isSuspended() {
    return suspended;
  }

  private void delay(int ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e);
    }
  }
}
