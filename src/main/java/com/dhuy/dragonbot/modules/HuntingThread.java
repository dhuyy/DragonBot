package com.dhuy.dragonbot.modules;

import java.util.logging.Level;
import com.dhuy.dragonbot.global.Log;

public class HuntingThread implements Runnable {
  private Log log;
  private Thread thread;
  private boolean suspended;
  private Hunting hunting;

  public HuntingThread() {
    log = Log.getInstance();

    suspended = false;
    hunting = new Hunting();
  }

  @Override
  public void run() {
    try {
      while (true) {
        hunting.execute();

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
}
