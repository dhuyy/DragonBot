package com.dhuy.dragonbot.templates;

import java.util.logging.Level;
import com.dhuy.dragonbot.global.Log;

// How to stop PausableRunnable:
//
// synchronized (ThreadClass) {
// while (Store.getInstance().getCavebotThread().isSuspended()) {
// ThreadClass.wait();
// }
// }

public class PausableRunnable implements Runnable {
  private Log log;
  private Thread thread;
  private boolean suspended;

  public PausableRunnable(String threadName) {
    log = Log.getInstance();

    suspended = false;
  }

  @Override
  public void run() {
    try {
      while (true) {
        // HERE COMES YOUR BUSINESS LOGIC

        // Let the thread sleep for a while.
        Thread.sleep(1000);

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
