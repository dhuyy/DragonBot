package com.dhuy.dragonbot.global;

import com.dhuy.dragonbot.modules.Screenshot;
import com.dhuy.dragonbot.modules.Waypoint;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

public class KeyboardHook {
  private static KeyboardHook instance = new KeyboardHook();

  private GlobalKeyboardHook keyboardHook;
  private Waypoint waypoint;
  private GlobalKeyAdapter pauseAppHook;
  private GlobalKeyAdapter enableCaptureWaypointHook;
  private DBConnection dbConnection;
  private Screenshot screenshotModule;

  private KeyboardHook() {
    keyboardHook = new GlobalKeyboardHook(false); // Use false here to switch to hook instead of raw
                                                  // input
    waypoint = new Waypoint();
    screenshotModule = new Screenshot();
    dbConnection = DBConnection.getInstance();

    setPauseAppHook();
    setEnableCaptureWaypointHook();
  }

  public static KeyboardHook getInstance() {
    if (instance == null) {
      synchronized (KeyboardHook.class) {
        if (instance == null) {
          instance = new KeyboardHook();
        }
      }
    }

    return instance;
  }

  public void register(GlobalKeyAdapter keyAdapter) {
    keyboardHook.addKeyListener(keyAdapter);
  }

  public void unregister(GlobalKeyAdapter keyAdapter) {
    keyboardHook.removeKeyListener(keyAdapter);
  }

  public GlobalKeyAdapter getPauseAppHook() {
    return pauseAppHook;
  }

  private void setPauseAppHook() {
    pauseAppHook = new GlobalKeyAdapter() {
      @Override
      public void keyReleased(GlobalKeyEvent event) {
        if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_PAUSE) {
          dbConnection.close();
          keyboardHook.shutdownHook();

          System.exit(0);
        }
      }
    };
  }

  public GlobalKeyAdapter getEnableCaptureWaypointHook() {
    return enableCaptureWaypointHook;
  }

  private void setEnableCaptureWaypointHook() {
    enableCaptureWaypointHook = new GlobalKeyAdapter() {
      @Override
      public void keyReleased(GlobalKeyEvent event) {
        if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_HOME) {
          screenshotModule.execute(this.getClass().getName());
          waypoint.captureWaypoint();
        }

        if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_END) {
          unregister(enableCaptureWaypointHook);
        }
      }
    };
  }
}
