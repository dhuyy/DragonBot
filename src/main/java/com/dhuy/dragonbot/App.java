package com.dhuy.dragonbot;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import com.dhuy.dragonbot.modules.Cavebot;
import com.dhuy.dragonbot.modules.Healing;

public class App {
  public static void main(String[] args) {
    ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();

    threadPool.submit(new Cavebot("Cavebot"));
    threadPool.submit(new Healing("Healing"));

    threadPool.shutdown();
  }
}
