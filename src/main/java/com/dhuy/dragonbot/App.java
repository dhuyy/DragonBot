package com.dhuy.dragonbot;

import java.util.concurrent.ThreadPoolExecutor;
import com.dhuy.dragonbot.global.KeyboardHook;
import com.dhuy.dragonbot.global.ThreadPool;
import com.dhuy.dragonbot.modules.CavebotTask;
import com.dhuy.dragonbot.modules.HealingTask;
import com.dhuy.dragonbot.modules.ScreenshotTask;

public class App {
  public static void main(String[] args) {
    KeyboardHook keyboardHook = KeyboardHook.getInstance();
    keyboardHook.register(keyboardHook.getPauseAppHook());

    ThreadPoolExecutor threadPoolExecutor = ThreadPool.getInstance().getExecutor();
    threadPoolExecutor.submit(new ScreenshotTask());
    threadPoolExecutor.submit(new HealingTask());
    threadPoolExecutor.submit(new CavebotTask());
  }
}
