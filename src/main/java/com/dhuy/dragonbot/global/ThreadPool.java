package com.dhuy.dragonbot.global;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPool {
  private static ThreadPool instance = new ThreadPool();
  
  private ThreadPoolExecutor executor;

  private ThreadPool() {
    executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
  }

  public static ThreadPool getInstance() {
    if (instance == null) {
      synchronized (ThreadPool.class) {
        if (instance == null) {
          instance = new ThreadPool();
        }
      }
    }

    return instance;
  }

  public ThreadPoolExecutor getExecutor() {
    return executor;
  }
}
