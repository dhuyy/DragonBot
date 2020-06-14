package com.dhuy.dragonbot.modules;

import java.util.logging.Level;
import com.dhuy.dragonbot.global.Log;

public class HealingThread implements Runnable {
  // A Knight takes 6 seconds to regenerate 2 of mana. Exura Ico consumes 40 of mana.
  private static final int USE_HEALING_SPELL_DELAY = 120000;

  private Log log;
  private Thread thread;
  private boolean suspended;
  private Healing healing;

  public HealingThread() {
    log = Log.getInstance();

    suspended = false;
    healing = new Healing();
  }

  @Override
  public void run() {
    try {
      while (true) {
        healing.execute();

        delay(USE_HEALING_SPELL_DELAY);

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
