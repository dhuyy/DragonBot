package com.dhuy.dragonbot.global;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

public class KeyboardHook {
  private static KeyboardHook instance = new KeyboardHook();
  private GlobalKeyboardHook keyboardHook;

  private KeyboardHook() {
    keyboardHook = new GlobalKeyboardHook(false); // Use false here to switch to hook instead of raw
                                                  // input
  }

  public static KeyboardHook getInstance() {
    return instance;
  }

  public void register(GlobalKeyAdapter keyAdapter) {
    keyboardHook.addKeyListener(keyAdapter);
  }

  public void unregister(GlobalKeyAdapter keyAdapter) {
    keyboardHook.removeKeyListener(keyAdapter);
  }

  public GlobalKeyAdapter getPauseAppHook() {
    return new GlobalKeyAdapter() {
      @Override
      public void keyReleased(GlobalKeyEvent event) {
        if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_PAUSE) {
          System.exit(0);
        }
      }
    };
  }
}
