package com.dhuy.dragonbot.global;

import java.sql.SQLException;
import java.util.logging.Level;
import com.dhuy.dragonbot.modules.Waypoint;
import com.dhuy.dragonbot.util.Notification;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

public class KeyboardHook {
  private static KeyboardHook instance = new KeyboardHook();

  private Store store;
  private GlobalKeyboardHook keyboardHook;
  private Waypoint waypoint;
  private GlobalKeyAdapter exitAppHook;
  private GlobalKeyAdapter enableCaptureWaypointHook;
  private DBConnection dbConnection;
  private Log log;
  private Notification notification;

  private KeyboardHook() {
    store = Store.getInstance();
    log = Log.getInstance();
    dbConnection = DBConnection.getInstance();

    waypoint = new Waypoint();
    notification = new Notification();
    keyboardHook = new GlobalKeyboardHook(false); // Use false here to switch to hook instead of raw
                                                  // input

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
        if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_F5) {
          try {
            if (dbConnection.getConnection() != null && !dbConnection.getConnection().isClosed()) {
              dbConnection.close();
            }
          } catch (SQLException e) {
            log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e.getStackTrace());
          }

          notification.instantlyShow("bye bye!");

          delay(1000);

          keyboardHook.shutdownHook();
          System.exit(0);
        }
      }
    };
  }

  public GlobalKeyAdapter getEnableCaptureWaypointHook() {
    return enableCaptureWaypointHook;
  }

  private String getDirectionString(int direction) {
    switch (direction) {
      case 1:
        return "North [^]";
      case 2:
        return "East [›]";
      case 3:
        return "South [v]";
      case 4:
        return "West [‹]";
      default:
        return null;
    }
  }

  private void setEnableCaptureWaypointHook() {
    enableCaptureWaypointHook = new GlobalKeyAdapter() {
      @Override
      public void keyReleased(GlobalKeyEvent event) {
        final int HIDE_NOTIFICATION_AFTER = 3000;

        /**
         * Direction
         */
        if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_I) {
          store.setCurrentDirection(1);

          notification.show("Direção Alterada: ".concat(getDirectionString(1)),
              HIDE_NOTIFICATION_AFTER);
        }

        if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_L) {
          store.setCurrentDirection(2);

          notification.show("Direção Alterada: ".concat(getDirectionString(2)),
              HIDE_NOTIFICATION_AFTER);
        }

        if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_K) {
          store.setCurrentDirection(3);

          notification.show("Direção Alterada: ".concat(getDirectionString(3)),
              HIDE_NOTIFICATION_AFTER);
        }

        if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_J) {
          store.setCurrentDirection(4);

          notification.show("Direção Alterada: ".concat(getDirectionString(4)),
              HIDE_NOTIFICATION_AFTER);
        }

        /**
         * Rope
         */
        if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_PRIOR) {
          waypoint.captureRopeWaypoint();

          notification.show("______ Waypoint Adicionado ______ Tipo: ROPE",
              HIDE_NOTIFICATION_AFTER);
        }

        /**
         * Shovel
         */
        if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_NEXT) {
          waypoint.captureShovelWaypoint();

          notification.show("______ Waypoint Adicionado ______ Tipo: SHOVEL - Direção: "
              .concat(getDirectionString(store.getCurrentDirection())), HIDE_NOTIFICATION_AFTER);
        }

        /**
         * Ladder
         */
        if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_HOME) {
          waypoint.captureLadderWaypoint();

          notification.show("______ Waypoint Adicionado ______ Tipo: LADDER - Direção: "
              .concat(getDirectionString(store.getCurrentDirection())), HIDE_NOTIFICATION_AFTER);
        }

        /**
         * Hole/Ramp
         */
        if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_END) {
          waypoint.captureHoleRampWaypoint();

          notification.show("______ Waypoint Adicionado ______ Tipo: HOLE/RAMP - Direção: "
              .concat(getDirectionString(store.getCurrentDirection())), HIDE_NOTIFICATION_AFTER);
        }

        /**
         * Walk
         */
        if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_DELETE) {
          waypoint.captureWaypoint();

          notification.show("______ Waypoint Adicionado ______ Tipo: WALK - Map Base: "
              .concat(getDirectionString(store.getCurrentDirection())), HIDE_NOTIFICATION_AFTER);
        }
      }
    };
  }

  private void delay(int ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e.getStackTrace());
    }
  }
}
