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
  private String threadName;
  private boolean suspended;

  public PausableRunnable(String threadName) {
    log = Log.getInstance();

    this.threadName = threadName;
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
      log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e.getStackTrace());

      System.out.println("[" + threadName + "] Thread interrupted.");
    }

    System.out.println("[" + threadName + "] Thread exiting.");
  }

  public void start() {
    if (thread == null) {
      thread = new Thread(this, threadName);
      thread.start();

      System.out.println("[" + threadName + "] Thread started.");
    }
  }

  public void suspend() {
    suspended = true;

    System.out.println("[" + threadName + "] Thread suspended.");
  }

  public synchronized void resume() {
    suspended = false;

    notify();

    System.out.println("[" + threadName + "] Thread resumed.");
  }

  public boolean isSuspended() {
    return suspended;
  }
}
