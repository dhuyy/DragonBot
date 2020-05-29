package com.dhuy.dragonbot.global;

import java.sql.SQLException;
import java.util.logging.Level;
import com.dhuy.dragonbot.modules.Waypoint;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

public class KeyboardHook {
  private static KeyboardHook instance = new KeyboardHook();

  private GlobalKeyboardHook keyboardHook;
  private Waypoint waypoint;
  private GlobalKeyAdapter exitAppHook;
  private GlobalKeyAdapter enableCaptureWaypointHook;
  private DBConnection dbConnection;
  private Log log;

  private KeyboardHook() {
    keyboardHook = new GlobalKeyboardHook(false); // Use false here to switch to hook instead of raw
                                                  // input
    waypoint = new Waypoint();
    dbConnection = DBConnection.getInstance();
    log = Log.getInstance();

    setExitAppHook();
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

  public GlobalKeyAdapter getExitAppHook() {
    return exitAppHook;
  }

  private void setExitAppHook() {
    exitAppHook = new GlobalKeyAdapter() {
      @Override
      public void keyReleased(GlobalKeyEvent event) {
        if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_PAUSE) {
          try {
            if (dbConnection.getConnection() != null && !dbConnection.getConnection().isClosed()) {
              dbConnection.close();
            }
          } catch (SQLException e) {
            log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e.getStackTrace());
          }

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
          waypoint.captureWaypoint();
        }

        if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_END) {
          unregister(enableCaptureWaypointHook);
        }
      }
    };
  }
}
